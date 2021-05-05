package calc.evaluate.parser;
;
import calc.evaluate.parser.expression.ArithmeticExpression;
import calc.evaluate.parser.expression.LogExpression;
import calc.evaluate.parser.expression.NumericExpression;
import calc.evaluate.parser.expression.TrigExpression;
import calc.evaluate.parser.symbol.Symbol;
import calc.evaluate.parser.symbol.SymbolType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RpnParserTest {
    RpnParser rpnParser;
    SymbolParser symbolParser = mock(SymbolParser.class);
    ArrayList<Symbol> symbols;

    private final static double EPSILON = 0.00001;


    @Before
    public void setUp() throws Exception {
        rpnParser = spy(new RpnParser("ignored input"));
        when(rpnParser.createSymbolParser()).thenReturn(symbolParser);
        when(symbolParser.parse()).thenReturn(true);
        symbols = new ArrayList<>();
        when(symbolParser.getSymbols()).thenReturn(symbols);
    }

    @Test
    public void parseEscalateSymbolParserError() {
        when(symbolParser.parse()).thenReturn(false);
        when(symbolParser.getError()).thenReturn("Test");
        Assert.assertFalse(rpnParser.parse());
        Assert.assertEquals("Parsing Error: Test", rpnParser.getError());
    }

    @Test
    public void parseNoSymbols() {
        Assert.assertFalse(rpnParser.parse());
        Assert.assertEquals("Expression building Error: Empty expression list", rpnParser.getError());
    }

    @Test
    public void parseTooManySymbols() {
        symbols.add(new Symbol(SymbolType.NUMBER, "22"));
        symbols.add(new Symbol(SymbolType.NUMBER, "22"));
        symbols.add(new Symbol(SymbolType.NUMBER, "111"));
        Assert.assertFalse(rpnParser.parse());
        Assert.assertEquals("Expression building Error: Too many symbols given", rpnParser.getError());
    }
    @Test
    public void parseTooFewParametersForArithmeticOperation() {
        symbols.add(new Symbol(SymbolType.NUMBER, "22"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "+"));
        Assert.assertFalse(rpnParser.parse());
        Assert.assertEquals(
                "Expression building Error: Not enough parameters for arithmetic operation",
                rpnParser.getError());
    }

    @Test
    public void parseNotSupportedSymbol() {
        symbols.add(new Symbol(SymbolType.PARENTHESIS, "22"));
        Assert.assertFalse(rpnParser.parse());
        Assert.assertEquals(
                "Expression building Error: Unknown symbol",
                rpnParser.getError());
    }

    @Test
    public void parseSingleNumber() {
        symbols.add(new Symbol(SymbolType.NUMBER, "22"));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof NumericExpression);
        Assert.assertEquals(22d, rpnParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseSimpleArithmetic() {
        symbols.add(new Symbol(SymbolType.NUMBER, "e"));
        symbols.add(new Symbol(SymbolType.NUMBER, "Pi"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "+"));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof ArithmeticExpression);
        Assert.assertEquals(Math.E + Math.PI, rpnParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseNestedArithmetic() {
        symbols.add(new Symbol(SymbolType.NUMBER, "100"));
        symbols.add(new Symbol(SymbolType.NUMBER, "200"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "+"));
        symbols.add(new Symbol(SymbolType.NUMBER, "3"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "*"));
        symbols.add(new Symbol(SymbolType.NUMBER, "5"));
        symbols.add(new Symbol(SymbolType.NUMBER, "10"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "-"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "/"));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof ArithmeticExpression);
        Assert.assertEquals(-180d, rpnParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseNestedTrig() {
        symbols.add(new Symbol(SymbolType.NUMBER, "3"));
        symbols.add(new Symbol(SymbolType.NUMBER, "Pi"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "*"));
        symbols.add(new Symbol(SymbolType.NUMBER, "4"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "/"));
        symbols.add(new Symbol(SymbolType.TRIG, "ctan"));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof TrigExpression);
        Assert.assertEquals(-1d, rpnParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseTrigWithParam() {
        symbols.add(new Symbol(SymbolType.TRIG_WITH_PARAM, "cos pi"));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof TrigExpression);
        Assert.assertEquals(-1d, rpnParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseNestedLog() {
        symbols.add(new Symbol(SymbolType.NUMBER, "10"));
        symbols.add(new Symbol(SymbolType.NUMBER, "100"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "*"));
        symbols.add(new Symbol(SymbolType.NUMBER, "100"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "*"));
        symbols.add(new Symbol(SymbolType.LOG, "log"));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof LogExpression);
        Assert.assertEquals(5d, rpnParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseLogWithBase() {
        symbols.add(new Symbol(SymbolType.NUMBER, "100"));
        symbols.add(new Symbol(SymbolType.NUMBER, "100"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "*"));
        symbols.add(new Symbol(SymbolType.NUMBER, "100"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "*"));
        symbols.add(new Symbol(SymbolType.LOG_WITH_BASE, "log 1000"));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof LogExpression);
        Assert.assertEquals(2d, rpnParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseLogWithParam() {                            //same behaviour as with Base iin RPN
        symbols.add(new Symbol(SymbolType.NUMBER, "32"));
        symbols.add(new Symbol(SymbolType.LOG_WITH_PARAM, "log 2"));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof LogExpression);
        Assert.assertEquals(5d, rpnParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseNestedLn() {
        symbols.add(new Symbol(SymbolType.NUMBER, "e"));
        symbols.add(new Symbol(SymbolType.NUMBER, "e"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "*"));
        symbols.add(new Symbol(SymbolType.LOG, "ln"));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof LogExpression);
        Assert.assertEquals(2d, rpnParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseLnWithParam() {
        symbols.add(new Symbol(SymbolType.LN_WITH_PARAM, "ln e"));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof LogExpression);
        Assert.assertEquals(1d, rpnParser.getRootExpression().getValue(), EPSILON);
    }
    @Test
    public void parseEquation() {
        symbols.add(new Symbol(SymbolType.QUICK_MULTIPLICATION, "2 x"));
        symbols.add(new Symbol(SymbolType.NUMBER, "2"));
        symbols.add(new Symbol(SymbolType.NUMBER, "x"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "*"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "+"));
        symbols.add(new Symbol(SymbolType.NUMBER, "16"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "-"));
        symbols.add(new Symbol(SymbolType.NUMBER, "0"));
        symbols.add(new Symbol(SymbolType.ARITHMETIC, "="));
        Assert.assertTrue(rpnParser.parse());
        Assert.assertTrue(rpnParser.getRootExpression() instanceof ArithmeticExpression);
        Assert.assertEquals(4d, rpnParser.getRootExpression().getValue(), EPSILON);
    }
}
