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

    public SymbolParser(String input) {
        this.input = input.strip();
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
        return (errorStatus == 0);
    }

    int readNext(CharacterIterator it) {
        if(Character.isWhitespace(it.current())) {
            it.next();
            return 0;
        }
        if(Character.isDigit(it.current())){
            return getNumericSymbol(it);
        }
        else if(arithmeticOperations.contains(it.current())){
            return getArithmeticSymbol(it);
        }
        return 1;
    }

    int getNumericSymbol(CharacterIterator it) {
        int dots = 0;
        StringBuilder numberAsString = new StringBuilder(Character.toString(it.current()));
        it.next();
        while(it.current() != CharacterIterator.DONE &&
                (Character.isDigit(it.current()) || it.current() == '.')) {
            if (it.current() == '.') {
                dots++;
                if(dots>1) {
                    error = "Invalid number";
                    return 1;
                }
            }

            numberAsString.append(it.current());
            it.next();
        }
        symbols.add(new Symbol(SymbolType.NUMBER, numberAsString.toString()));
        return 0;
    }

    int getArithmeticSymbol(CharacterIterator it) {
        symbols.add(new Symbol(SymbolType.ARITHMETIC, Character.toString(it.current())));
        it.next();
        return 0;
    }
}
