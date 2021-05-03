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
        return Double.toString(val);
    }

    abstract void eval();

    void evalOnce() {
        if (!wasEvaluated) {
            eval();
            wasEvaluated = true;
        }
    }
}
