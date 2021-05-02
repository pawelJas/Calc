package calc.evaluate.parser.symbol;

public class Symbol {
    SymbolType type;
    String val;

    public Symbol(SymbolType type, String val) {
        this.type = type;
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public boolean isNumeric() {
        return type == SymbolType.NUMBER;
    }

    public boolean isArithmetic () {
        return type == SymbolType.ARITHMETIC;
    }
}
