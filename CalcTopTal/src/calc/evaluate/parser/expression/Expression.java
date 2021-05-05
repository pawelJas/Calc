package calc.evaluate.parser.expression;

public abstract class Expression {
    String operation = "";
    double val;
    double variableVal = 0d;
    char variableName = 0;
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

    public double getVariableValue() {
        evalOnce();
        return variableVal;
    }

    public char getVariableName() {
        evalOnce();
        return variableName;
    }

    public boolean isComplex() {
        evalOnce();
        return variableVal != 0d;
    }

    @Override
    public String toString() {
        evalOnce();
        if(errorCode == ExpressionError.NO_ERROR) {
            if(operation.equals("=")) {
                return variableName + " = " + Double.toString(val);
            }
            if(isComplex()) {
                return Double.toString(variableVal) + variableName + " = " + Double.toString(val);
            }
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
