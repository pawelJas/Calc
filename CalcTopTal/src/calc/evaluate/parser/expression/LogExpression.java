package calc.evaluate.parser.expression;

public class LogExpression extends Expression   {
    String operation;
    Expression param;
    double base;
    boolean customBase = false;

    public LogExpression(String operation, Expression param) {
        this.operation = operation;
        this.param = param;
        this.base = 10d;
    }

    public LogExpression(String operation, Expression param, double base) {
        this.operation = operation;
        this.base = base;
        this.param = param;
        customBase = true;
    }

    @Override
    void eval() {
        System.out.println("Logarithms evaluation of " + operation);
        if((errorCode = param.getErrorCode()) != ExpressionError.NO_ERROR) {
            return;
        }
        if (param.getValue() <= 0.0 || base <= 0.0) {
            errorCode = ExpressionError.INVALID_ALGORITHM;
            return;
        }
        if(operation.equals("ln")) {
            val = Math.log(param.getValue());
        }
        else if(!customBase) {
            val = Math.log10(param.getValue());
        }
        else {
            val = Math.log(param.getValue())/Math.log(base);
        }
    }
}
