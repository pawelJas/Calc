package calc.evaluate.parser;

import calc.evaluate.parser.symbol.Symbol;
import calc.evaluate.parser.symbol.SymbolType;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SymbolParser {
    String input;
    ArrayList<Symbol> symbols;
    String error;

    private static final Set<Character> arithmeticOperations = new HashSet<>(Arrays.asList('+', '-', '*', '/', '='));
    private static final Set<String> trigonometricOperations = new HashSet<>(Arrays.asList("sin", "cos", "tan", "ctan"));
    private static final Set<String> logarithmsOperations = new HashSet<>(Arrays.asList("log", "ln"));
    private static final Set<String> constValue = new HashSet<>(Arrays.asList("Pi", "pi", "e"));
    private static final Set<String> variable = new HashSet<>(Arrays.asList("x", "y"));
    private static final Set<String> allKeys = new HashSet<>() {{
        addAll(trigonometricOperations);
        addAll(logarithmsOperations);
        addAll(constValue);
        addAll(variable);
    }};

    public SymbolParser(String input) {
        this.input = input.toLowerCase().strip();
        symbols = new ArrayList<>();
    }

    public ArrayList<Symbol> getSymbols() {
        return symbols;
    }

    public String getError(){
        return error;
    }

    public boolean parse() {
        System.out.println("Symbol parsing started");
        CharacterIterator it = new StringCharacterIterator(input);
        int errorStatus = 0;
        while(it.current() != CharacterIterator.DONE && errorStatus == 0) {
            errorStatus = readNext(it);
        }
        if(errorStatus > 0) {
            System.out.println("Symbol parsing error: " + error);
            return false;
        }
        return true;
    }

    int readNext(CharacterIterator it) {
        Symbol symbol;
        if(Character.isWhitespace(it.current())) {
            it.next();
            return 0;
        }
        if(it.current() == '('){
            symbol = getParentheses(it);
        }
        else if(Character.isDigit(it.current())){
            symbol = getNumericSymbol(it);
        }
        else if(arithmeticOperations.contains(it.current())){
            symbol = getArithmeticSymbol(it);
        }
        else if(Character.isAlphabetic(it.current())) {
            symbol = getAlphabeticSymbol(it);
        }
        else {
            error = "Unknown object in input";
            return 1;
        }
        if(symbol == null){
            return 1;
        }
        symbols.add(symbol);
        return 0;
    }

    Symbol getParentheses(CharacterIterator it) {
        StringBuilder sectionString = new StringBuilder();
        it.next();
        int depth = 1;
        while(it.current() != CharacterIterator.DONE) {
            if(it.current() == '(') {
                depth++;
            }
            else if(it.current() == ')') {
                depth--;
            }
            if(depth == 0) {
                break;
            }
            sectionString.append(it.current());
            it.next();
        }
        it.next();
        if(depth != 0) {
            error = "Invalid parentheses";
            return null;
        }
        return new Symbol(SymbolType.PARENTHESIS, sectionString.toString());
    }

    Symbol getNumericSymbol(CharacterIterator it) {
        int dots = 0;
        StringBuilder numberAsString = new StringBuilder(Character.toString(it.current()));
        it.next();
        while(it.current() != CharacterIterator.DONE &&
                (Character.isDigit(it.current()) || it.current() == '.')) {
            if (it.current() == '.') {
                dots++;
                if(dots>1) {
                    error = "Invalid number";
                    return null;
                }
            }

            numberAsString.append(it.current());
            it.next();
        }
        if(Character.isAlphabetic(it.current())) {
            String symbol = findKeyword(it);
            if (symbol == null) {
                return null;
            }
            if(variable.contains(symbol)) {
                return new Symbol(SymbolType.QUICK_MULTIPLICATION, numberAsString.toString() + " " + symbol);
            }
            else if(constValue.contains(symbol)) {
                return new Symbol(SymbolType.QUICK_MULTIPLICATION, numberAsString.toString() + " " + symbol);
            }
            else {
                error = "Illegal phrase after the number";
                return null;
            }
        }
        return new Symbol(SymbolType.NUMBER, numberAsString.toString());
    }

    Symbol getArithmeticSymbol(CharacterIterator it) {
        String symbolStr = Character.toString(it.current());
        it.next();
        return new Symbol(SymbolType.ARITHMETIC, symbolStr);
    }

    String findKeyword(CharacterIterator it) {
        StringBuilder alphabeticSymbol = new StringBuilder(Character.toString(it.current()));
        it.next();
        while(!allKeys.contains(alphabeticSymbol.toString())) {
            if(it.current() == CharacterIterator.DONE) {
                error = "Invalid keyword";
                return null;
            }
            alphabeticSymbol.append(it.current());
            it.next();
        }
        return alphabeticSymbol.toString();
    }

    Symbol addFunction(String funcName, CharacterIterator it) {
        Symbol secondSymbol = null;
        if(it.current() != CharacterIterator.DONE && Character.isDigit(it.current())) {
            secondSymbol = getNumericSymbol(it);
            if(secondSymbol == null) {
                return null;
            }
            else if (secondSymbol.isQuickMul()) {
                error = "Illegal phrase after the function";
                return null;
            }
        }
        else if(it.current() != CharacterIterator.DONE && Character.isAlphabetic(it.current())) {
            secondSymbol = getAlphabeticSymbol(it);
            if(secondSymbol == null) {
                return null;
            }
            else if (!secondSymbol.isConst()) {
                error = "Illegal phrase after the function";
                return null;
            }
        }
        if(secondSymbol != null) {
            if(trigonometricOperations.contains(funcName)) {
                return new Symbol(SymbolType.TRIG_WITH_PARAM, funcName + " " + secondSymbol.getVal());
            }
            else if(funcName.equals("ln")){
                return new Symbol(SymbolType.LN_WITH_PARAM, funcName + " " + secondSymbol.getVal());
            }
            else if(funcName.equals("log") && it.current() != CharacterIterator.DONE && it.current() == '('){
                return new Symbol(SymbolType.LOG_WITH_BASE, funcName + " " + secondSymbol.getVal());
            }
            else  {
                return new Symbol(SymbolType.LOG_WITH_PARAM, funcName + " " + secondSymbol.getVal());
            }
        }
        if(trigonometricOperations.contains(funcName)) {
            return new Symbol(SymbolType.TRIG, funcName);
        }
        else {
            return new Symbol(SymbolType.LOG, funcName);
        }
    }

    Symbol getAlphabeticSymbol(CharacterIterator it) {
        String symbol = findKeyword(it);
        if (symbol == null) {
            return null;
        }
        if(variable.contains(symbol)) {
            return new Symbol(SymbolType.VARIABLE, symbol);
        }
        else if(constValue.contains(symbol)) {
            return new Symbol(SymbolType.CONST, symbol);
        }
        return addFunction(symbol, it);
    }
}
