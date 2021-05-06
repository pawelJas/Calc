package calc.evaluate.parser;

import calc.evaluate.parser.expression.ArithmeticExpression;
import calc.evaluate.parser.expression.LogExpression;
import calc.evaluate.parser.expression.TrigExpression;
import calc.evaluate.parser.symbol.Symbol;
import calc.evaluate.parser.symbol.SymbolType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class InfixParserTest {
    InfixParser infixParser;
    SymbolParser rootSymbolParser = mock(SymbolParser.class);
    SymbolParser symbolParser = mock(SymbolParser.class);
    ArrayList<Symbol> symbols, rootSymbols;

    private final static double EPSILON = 0.00001;

    @Before
    public void setUp() throws Exception {
        infixParser = spy(new InfixParser("ignored input"));
        when(infixParser.createSymbolParser(Mockito.anyString())).thenReturn(rootSymbolParser, symbolParser);
        when(symbolParser.parse()).thenReturn(true);
        symbols = new ArrayList<>();
        rootSymbols = new ArrayList<>();
        rootSymbols.add(new Symbol(SymbolType.PARENTHESIS,"ignored input"));
        when(rootSymbolParser.getSymbols()).thenReturn(rootSymbols);
        when(symbolParser.getSymbols()).thenReturn(symbols);
    }

    @Test
    public void parseSymbolParsingError() {
        infixParser = spy(new InfixParser("s24"));
        Assert.assertFalse(infixParser.parse());
        Assert.assertEquals("Parsing Error: Invalid keyword", infixParser.getError());
        infixParser = spy(new InfixParser("3 * 5 + (3d - 3) *  4"));
        Assert.assertFalse(infixParser.parse());
        Assert.assertEquals("Parsing Error: Invalid keyword", infixParser.getError());
    }

    @Test
    public void parseSimpleAdd() {
        infixParser = spy(new InfixParser("2 + 2"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof ArithmeticExpression);
        Assert.assertEquals(4d, infixParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseSimpleArithmetical() {
        infixParser = spy(new InfixParser("1 + 5 * 5 + 10 / 10 - 2"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof ArithmeticExpression);
        Assert.assertEquals(25d, infixParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseSimpleEquation() {
        infixParser = spy(new InfixParser("1 * 2 + 5x + 3 = 20 + 2 * 10x"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof ArithmeticExpression);
        Assert.assertEquals(-1d, infixParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseTrig() {
        infixParser = spy(new InfixParser("cos(pi)"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof TrigExpression);
        Assert.assertEquals(-1d, infixParser.getRootExpression().getValue(), EPSILON);
        infixParser = spy(new InfixParser("sin(1.5*pi)"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof TrigExpression);
        Assert.assertEquals(-1d, infixParser.getRootExpression().getValue(), EPSILON);
        infixParser = spy(new InfixParser("cos(pi) + Sin0 - sin(0.5Pi) + sin(1.5pi)"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof ArithmeticExpression);
        Assert.assertEquals(-3d, infixParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseLog() {
        infixParser = spy(new InfixParser("log2(8)"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof LogExpression);
        Assert.assertEquals(3d, infixParser.getRootExpression().getValue(), EPSILON);
        infixParser = spy(new InfixParser("lne"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof LogExpression);
        Assert.assertEquals(1d, infixParser.getRootExpression().getValue(), EPSILON);
        infixParser = spy(new InfixParser("ln(e*e)"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof LogExpression);
        Assert.assertEquals(2d, infixParser.getRootExpression().getValue(), EPSILON);
        infixParser = spy(new InfixParser("log10 + Log(10) + log10(10) + Log100(10)"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof ArithmeticExpression);
        Assert.assertEquals(3.5d, infixParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseNestedArithmetical() {
        infixParser = spy(new InfixParser("(3+(4-1))*5"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof ArithmeticExpression);
        Assert.assertEquals(30d, infixParser.getRootExpression().getValue(), EPSILON);
    }

    @Test
    public void parseNestedEquation() {
        infixParser = spy(new InfixParser("2 * x + 1 = 2*(1-x)"));
        Assert.assertTrue(infixParser.parse());
        Assert.assertTrue(infixParser.getRootExpression() instanceof ArithmeticExpression);
        Assert.assertEquals(0.25, infixParser.getRootExpression().getValue(), EPSILON);
    }
}
