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

    public boolean isArithmetic() {
        return type == SymbolType.ARITHMETIC;
    }

    public boolean isVariable() {
        return type == SymbolType.VARIABLE;
    }

    public boolean isConst() {
        return type == SymbolType.CONST;
    }

    public boolean isTrig() {
        return type == SymbolType.TRIG;
    }

    public boolean isLog() {
        return type == SymbolType.LOG;
    }
}
