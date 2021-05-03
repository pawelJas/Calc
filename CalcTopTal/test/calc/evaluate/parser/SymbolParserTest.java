package calc.evaluate.parser;

import calc.evaluate.parser.SymbolParser;
import calc.evaluate.parser.symbol.Symbol;
import org.junit.Assert;
import java.util.ArrayList;

public class SymbolParserTest {
    SymbolParser symbolParser;

    @org.junit.Test
    public void parseEmptyString() {
        symbolParser = new SymbolParser("");
        Assert.assertTrue(symbolParser.parse());
        Assert.assertTrue(symbolParser.getSymbols().isEmpty());
    }

    @org.junit.Test
    public void parseNumericValue() {
        symbolParser = new SymbolParser("  22.34 33   0  22.1 33.4444444 0.3      ");
        String[] expected = {"22.34", "33", "0", "22.1", "33.4444444", "0.3"};
        Assert.assertTrue(symbolParser.parse());
        ArrayList<Symbol> symbols = symbolParser.getSymbols();
        Assert.assertEquals(expected.length, symbolParser.getSymbols().size());
        symbols.forEach(symbol ->
                Assert.assertTrue(symbol.isNumeric())
        );
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], symbols.get(i).getVal());
        }
    }

    @org.junit.Test
    public void parseArithmeticOperations() {
        symbolParser = new SymbolParser("  //**++--  -++ --* ");
        String[] expected = {"/", "/", "*", "*", "+", "+", "-", "-", "-", "+", "+", "-", "-", "*"};
        Assert.assertTrue(symbolParser.parse());
        ArrayList<Symbol> symbols = symbolParser.getSymbols();
        Assert.assertEquals(expected.length, symbolParser.getSymbols().size());
        symbols.forEach(symbol ->
                Assert.assertTrue(symbol.isArithmetic())
        );
        for(int i=0; i< expected.length; i++) {
                    Assert.assertEquals(expected[i],symbols.get(i).getVal());
        }
    }

    @org.junit.Test
    public void parse() {
        symbolParser = new SymbolParser("0 0 22.3+3*1     / ");
        String[] expected = {"0", "0", "22.3", "+", "3", "*", "1", "/"};
        Assert.assertTrue(symbolParser.parse());
        ArrayList<Symbol> symbols = symbolParser.getSymbols();
        Assert.assertEquals(expected.length, symbolParser.getSymbols().size());
        for(int i=0; i< expected.length; i++) {
            Assert.assertEquals(expected[i],symbols.get(i).getVal());
        }
    }

    @org.junit.Test
    public void parseNumberError() {
        symbolParser = new SymbolParser("0.33.0");
        Assert.assertFalse(symbolParser.parse());
        Assert.assertEquals("Invalid number", symbolParser.getError());
    }
}
