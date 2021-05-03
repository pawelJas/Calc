package calc.evaluate.parser;

import calc.evaluate.parser.expression.ArithmeticExpression;
import calc.evaluate.parser.expression.Expression;
import calc.evaluate.parser.expression.NumericExpression;
import calc.evaluate.parser.symbol.Symbol;

import java.util.*;

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
            System.out.println("Expression building failed with error: " + error);
            error = "Expression building Error: " + error;
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
            error = "Empty expression list";
            return false;
        }
        Iterator<Symbol> it = symbolParser.getSymbols().iterator();
        Symbol currentSymbol;
        Stack<Expression> expressions = new Stack<>();
        while(it.hasNext()) {
            currentSymbol = it.next();
            if (currentSymbol.isNumeric()) {
                expressions.push(new NumericExpression(currentSymbol.getVal()));
            }
            else if (currentSymbol.isArithmetic()){
                try {
                    Expression e1 = expressions.pop();
                    Expression e2 = expressions.pop();
                    expressions.push(new ArithmeticExpression(currentSymbol.getVal(), e2, e1));
                } catch(EmptyStackException e) {
                    error = "Not enough parameters for arithmetic operation";
                    return false;
                } }
        }
        try {
            rootExpression = expressions.pop();
        } catch (EmptyStackException e) {
            error = "Root Expression is missing";
            return false;
        }
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
