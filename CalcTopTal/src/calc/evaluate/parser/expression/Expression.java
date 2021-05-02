package calc.evaluate.parser.expression;

public abstract class Expression {
    double val;
    boolean wasEvaluated = false;

    public int getErrorCode() {
        evalOnce();
        return errorCode;
    }

    int errorCode = 0;

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
