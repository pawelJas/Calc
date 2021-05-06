package calc.evaluate.parser;

import calc.evaluate.parser.expression.Expression;
import calc.evaluate.parser.symbol.Symbol;
import calc.evaluate.parser.symbol.SymbolType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class InfixParser {
    String input;
    Expression rootExpression = null;
    String error;

    public InfixParser(String input) {
        this.input = input;
    }

    public Expression getRootExpression() {
        return rootExpression;
    }

    public String getError() {
        return error;
    }

    public boolean parse() {
        System.out.println("Infix parsing started");
        Symbol rootSymbol = new Symbol(SymbolType.PARENTHESIS, input);
        ArrayList<Symbol> rpnSymbols = evalParenthesisSymbol(rootSymbol);
        if(rpnSymbols == null) {
            System.out.println("Infix Error: Symbol parsing failed with error: " + error);
            return false;
        }
        RpnParser rpnParser = new RpnParser(rpnSymbols);
        rpnParser.buildExpression();
        if(!rpnParser.buildExpression()) {
            System.out.println("Infix Error: Expression building expression with Rpn: " + rpnParser.getError());
            error = "Expression building Error: " + rpnParser.getError();
            return false;
        }
        rootExpression = rpnParser.getRootExpression();
        return true;
    }

    ArrayList<Symbol> evalParenthesisSymbol(Symbol symbol) {
        SymbolParser symbolParser = createSymbolParser(symbol.getVal());
        if(!symbolParser.parse()) {
            error = "Parsing Error: " + symbolParser.getError();
            System.out.println("Infix Error: Symbol parsing failed with error: " + symbolParser.getError());
            return null;
        }
        return evalSymbolList(symbolParser.getSymbols());
    }

    ArrayList<Symbol> evalSymbolList(ArrayList<Symbol> symbols) {
        if(symbols.isEmpty()) {
            error = "Empty expression list";
            return null;
        }
        Symbol equals = null;
        ArrayList<Symbol> rpnList = new ArrayList<>();
        Stack<Symbol> arithmeticOperations = new Stack<>();
        boolean arithmeticExpected = false;

        Iterator<Symbol> it = symbols.iterator();
        Symbol currentSymbol;
        while(it.hasNext()) {
            currentSymbol = it.next();
            if(arithmeticExpected) {
                if(currentSymbol.isArithmetic()) {
                    addArithmeticToRpnList(rpnList, arithmeticOperations, currentSymbol.getVal());
                } else {
                     error = "Unexpected symbol, expected arithmetical";
                     return null;
                }

                if(currentSymbol.getVal().equals("=")){
                    equals = currentSymbol;
                }
                else {
                    arithmeticOperations.add(currentSymbol);
                }
                arithmeticExpected = false;
            }
            else {
                if(currentSymbol.isParenthesis()) {
                    ArrayList<Symbol> nestedList = evalParenthesisSymbol(currentSymbol);
                    if(nestedList == null) {
                        return null;
                    }
                    rpnList.addAll(nestedList);
                }
                else if(currentSymbol.evaluatesToValue()) {
                    rpnList.add(currentSymbol);
                }
                else if(currentSymbol.isTrig() || currentSymbol.isLog() || currentSymbol.isLog_with_base()) {
                    Symbol next = null;
                    if(it.hasNext()) {
                        next = it.next();
                    }
                    if(next == null || !next.isParenthesis()) {
                        error = "Unexpected symbol, expected parenthesis";
                        return null;
                    }
                    ArrayList<Symbol> nestedList = evalParenthesisSymbol(next);
                    if(nestedList == null) {
                        return null;
                    }
                    rpnList.addAll(nestedList);
                    rpnList.add(currentSymbol);
                }
                else {
                    error = "Unexpected symbol";
                    return null;
                }
                arithmeticExpected = true;
            }
        }
        while(!arithmeticOperations.empty()) {
            rpnList.add(arithmeticOperations.pop());
        }
        if(equals != null) {
            rpnList.add(equals);
        }

        return rpnList;
    }

    boolean isMulOrDiv(String val) {
        return val.equals("*") || val.equals("/");
    }

    private void addArithmeticToRpnList(ArrayList<Symbol> rpnList, Stack<Symbol> arithmeticOperations, String val) {
        if(!arithmeticOperations.empty() && isMulOrDiv(arithmeticOperations.peek().getVal())) {
            rpnList.add(arithmeticOperations.pop());
        }
        if(!isMulOrDiv(val)) {
           while(!arithmeticOperations.empty()) {
               rpnList.add(arithmeticOperations.pop());
           }
        }
    }

    SymbolParser createSymbolParser(String string) {
        return new SymbolParser(string);
    }
}
