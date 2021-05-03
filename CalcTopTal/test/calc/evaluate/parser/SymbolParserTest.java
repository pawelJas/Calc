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
    public void parseVariable() {
        symbolParser = new SymbolParser("xxyyx   xx");
        String[] expected = {"x", "x", "y", "y", "x", "x", "x"};
        Assert.assertTrue(symbolParser.parse());
        ArrayList<Symbol> symbols = symbolParser.getSymbols();
        Assert.assertEquals(expected.length, symbolParser.getSymbols().size());
        symbols.forEach(symbol ->
                Assert.assertTrue(symbol.isVariable())
        );
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], symbols.get(i).getVal());
        }
    }

    @org.junit.Test
    public void parseConsts() {
        symbolParser = new SymbolParser(" piPieePi   e");
        String[] expected = {"pi", "Pi", "e", "e", "Pi", "e"};
        Assert.assertTrue(symbolParser.parse());
        ArrayList<Symbol> symbols = symbolParser.getSymbols();
        Assert.assertEquals(expected.length, symbolParser.getSymbols().size());
        symbols.forEach(symbol ->
                Assert.assertTrue(symbol.isConst())
        );
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], symbols.get(i).getVal());
        }
    }

    @org.junit.Test
    public void parseTryg() {
        symbolParser = new SymbolParser("sin cos tan ctan ctan");
        String[] expected = {"sin", "cos", "tan", "ctan", "ctan"};
        Assert.assertTrue(symbolParser.parse());
        ArrayList<Symbol> symbols = symbolParser.getSymbols();
        Assert.assertEquals(expected.length, symbolParser.getSymbols().size());
        symbols.forEach(symbol ->
                Assert.assertTrue(symbol.isTrig())
        );
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], symbols.get(i).getVal());
        }
    }

    @org.junit.Test
    public void parseLog() {
        symbolParser = new SymbolParser("log ln ln");
        String[] expected = {"log", "ln", "ln"};
        Assert.assertTrue(symbolParser.parse());
        ArrayList<Symbol> symbols = symbolParser.getSymbols();
        Assert.assertEquals(expected.length, symbolParser.getSymbols().size());
        symbols.forEach(symbol ->
                Assert.assertTrue(symbol.isLog())
        );
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], symbols.get(i).getVal());
        }
    }

    @org.junit.Test
    public void parseNumberError() {
        symbolParser = new SymbolParser("0.33.0");
        Assert.assertFalse(symbolParser.parse());
        Assert.assertEquals("Invalid number", symbolParser.getError());
    }

    @org.junit.Test
    public void parseUnkownObjectError() {
        symbolParser = new SymbolParser("0.33+^ 11");
        Assert.assertFalse(symbolParser.parse());
        Assert.assertEquals("Unknown object in input", symbolParser.getError());
    }

    @org.junit.Test
    public void parseUnkownKeywordError() {
        symbolParser = new SymbolParser("0.3 + siin 11");
        Assert.assertFalse(symbolParser.parse());
        Assert.assertEquals("Invalid keyword", symbolParser.getError());
    }

    @org.junit.Test
    public void parseEasy() {
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
    public void parseHarder() {
        symbolParser = new SymbolParser("log x ln+3.0  3 sin    / ");
        String[] expected = {"log", "x", "ln", "+", "3.0", "3", "sin", "/"};
        Assert.assertTrue(symbolParser.parse());
        ArrayList<Symbol> symbols = symbolParser.getSymbols();
        Assert.assertEquals(expected.length, symbolParser.getSymbols().size());
        for(int i=0; i< expected.length; i++) {
            Assert.assertEquals(expected[i],symbols.get(i).getVal());
        }
    }
}
