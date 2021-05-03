package calc.evaluate.parser.expression;

public abstract class Expression {
    double val;
    boolean wasEvaluated = false;
    ExpressionError errorCode = ExpressionError.NO_ERROR;

    public ExpressionError getErrorCode() {
        evalOnce();
        return errorCode;
    }

    public double getValue() {
        evalOnce();
        return val;
    }

    @Override
    public String toString() {
        evalOnce();
        if(errorCode == ExpressionError.NO_ERROR) {
            return Double.toString(val);
        }
        else {
            return "Expression evaluation error: "+ errorCode.toString();
        }
    }

    abstract void eval();

    void evalOnce() {
        if (!wasEvaluated) {
            eval();
            wasEvaluated = true;
        }
    }
}
