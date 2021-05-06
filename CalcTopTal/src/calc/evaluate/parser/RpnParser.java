package calc.evaluate.parser;

import calc.evaluate.parser.expression.*;
import calc.evaluate.parser.symbol.Symbol;

import java.util.*;

public class RpnParser {
    String input;
    Expression rootExpression = null;
    SymbolParser symbolParser;
    String error;
    ArrayList<Symbol> symbols = null;
    boolean isInfixMode = false;

    public RpnParser(String input) {
        this.input = input;
    }

    public RpnParser(ArrayList<Symbol>  symbols) {
        this.symbols = symbols;
        isInfixMode = true;
    }

    public boolean parse() {
        System.out.println("Rpn parsing started");
        symbolParser = createSymbolParser();
        if(!symbolParser.parse()) {
            error = "Parsing Error: " + symbolParser.getError();
            System.out.println("Rpn Error: Symbol parsing failed with error: " + symbolParser.getError());
            return false;
        }
        symbols = symbolParser.getSymbols();
        if(!buildExpression()) {
            System.out.println("Rpn Error: Expression building failed with error: " + error);
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

    public boolean buildExpression() {
        if(symbols.isEmpty()) {
            error = "Empty expression list";
            return false;
        }
        Iterator<Symbol> it = symbols.iterator();
        Symbol currentSymbol;
        Stack<Expression> expressions = new Stack<>();
        while(it.hasNext()) {
            currentSymbol = it.next();
            if (currentSymbol.isNumeric() || currentSymbol.isConst() || currentSymbol.isVariable()) {
                expressions.push(new NumericExpression(currentSymbol.getVal()));
            }
            else if (currentSymbol.isQuickMul()) {
                expressions.push(new ArithmeticExpression(currentSymbol.getVal()));
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
            else if (currentSymbol.isTrig_with_param()) {
                expressions.push(new TrigExpression(currentSymbol.getVal()));
            }
            else if (currentSymbol.isLn_with_param()) {
                expressions.push(new LogExpression(currentSymbol.getVal()));
            }
            else if (currentSymbol.isLog_with_param() && isInfixMode) {
                expressions.push(new LogExpression(currentSymbol.getVal()));
            }
            else if (currentSymbol.isTrig() || currentSymbol.isLog() || currentSymbol.isLog_with_base() || currentSymbol.isLog_with_param()) {
                try {
                    Expression param = expressions.pop();
                    if (currentSymbol.isTrig()) {
                        expressions.push(new TrigExpression(currentSymbol.getVal(), param));
                    } else if (currentSymbol.isLog()) {
                        expressions.push(new LogExpression(currentSymbol.getVal(), param));
                    } else {
                        expressions.push(new LogExpression(currentSymbol.getVal(), param, true));
                    }
                } catch(EmptyStackException e) {
                    error = "Not enough parameters for arithmetic operation";
                    return false;
                }
            } else {
                error = "Unknown symbol";
                return false;
            }
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
