package calc.evaluate.parser;

import calc.evaluate.parser.expression.*;
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
            if (currentSymbol.isNumeric() || currentSymbol.isConst()) {
                expressions.push(new NumericExpression(currentSymbol.getVal()));
            }
            else if (currentSymbol.isArithmetic()){
                try {
                    Expression param1 = expressions.pop();
                    Expression param2 = expressions.pop();
                    expressions.push(new ArithmeticExpression(currentSymbol.getVal(), param2, param1));
                } catch(EmptyStackException e) {
                    error = "Not enough parameters for arithmetic operation";
                    return false;
                }
            }
            else if (currentSymbol.isTrig() || currentSymbol.isLog()) {
                try {
                    Expression param = expressions.pop();
                    if (currentSymbol.isTrig()) {
                        expressions.push(new TrigExpression(currentSymbol.getVal(), param));
                    } else {
                        expressions.push(new LogExpression(currentSymbol.getVal(), param));
                    }
                } catch(EmptyStackException e) {
                    error = "Not enough parameters for arithmetic operation";
                    return false;
                }
            }
//            else if (currentSymbol.isVariable()) {
//
//            }
        }
        try {
            rootExpression = expressions.pop();
        } catch (EmptyStackException e) {
            error = "Root Expression is missing";
            return false;
        }
        if (!expressions.isEmpty()) {
            System.out.println(expressions);
            error = "Too many symbols given";
            return false;
        }
        return true;
    }

    SymbolParser createSymbolParser() {
        return new SymbolParser(input);
    }
}
