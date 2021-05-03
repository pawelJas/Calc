package calc.evaluate.parser;

import calc.evaluate.parser.expression.ArithmeticExpression;
import calc.evaluate.parser.expression.Expression;
import calc.evaluate.parser.expression.NumericExpression;
import calc.evaluate.parser.symbol.Symbol;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class RpnParser {
    String input;
    Expression rootExpression = null;
    SymbolParser symbolParser;
    String error;

    public RpnParser(String input) {
        this.input = input;
    }

    public boolean parse() {
        System.out.println("Rpn parsing started");
        symbolParser = createSymbolParser();
        if(!symbolParser.parse()) {
            error = "Parsing Error: " + symbolParser.getError();
            System.out.println("Symbol parsing failed with error: " + symbolParser.getError());
            return false;
        }

        if(!buildExpression()) {
            System.out.println("Expression building failed with error " + error);
            error = "Parsing Error: " + error;
            return false;
        }
        return true;
    }

    public Expression getRootExpression() {
        return rootExpression;
    }

    public String getError() {
        return error;
    }

    boolean buildExpression() {
        if(symbolParser.getSymbols().isEmpty()) {
            error = "Empty Expression";
            return false;
        }
        Iterator<Symbol> it = symbolParser.getSymbols().iterator();
        Symbol currentSymbol;
        Queue<Expression> expressions = new LinkedList<Expression>();
        while(it.hasNext()) {
            currentSymbol = it.next();
            if (currentSymbol.isNumeric()) {
                expressions.add(new NumericExpression(currentSymbol.getVal()));
            }
            else if (currentSymbol.isArithmetic()){
                Expression e1 = expressions.poll();
                Expression e2 = expressions.poll();
                expressions.add(new ArithmeticExpression(currentSymbol.getVal(), e1, e2));
            }
        }
        rootExpression = expressions.poll();
        if (!expressions.isEmpty()) {
            error = "Too many symbols given";
            return false;
        }
        return true;
    }

    SymbolParser createSymbolParser() {
        return new SymbolParser(input);
    }
}
