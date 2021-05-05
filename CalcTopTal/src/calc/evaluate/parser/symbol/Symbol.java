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

    public boolean isQuickMul() {
        return type == SymbolType.QUICK_MULTIPLICATION;
    }

    public boolean isTrig() {
        return type == SymbolType.TRIG;
    }

    public boolean isTrig_with_param() {
        return type == SymbolType.TRIG_WITH_PARAM;
    }

    public boolean isLog() {
        return type == SymbolType.LOG;
    }

    public boolean isLog_with_param() {
        return type == SymbolType.LOG_WITH_PARAM;
    }

    public boolean isLn_with_param() {
        return type == SymbolType.LN_WITH_PARAM;
    }

    public boolean isLog_with_base() {
        return type == SymbolType.LOG_WITH_BASE;
    }

    public boolean isParenthesis() {
        return type == SymbolType.PARENTHESIS;
    }
}
