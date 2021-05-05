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
        this.customBase = true;
    }

    public LogExpression(String operationAndParam) {
        String[] split = operationAndParam.split(" ");
        this.operation = split[0];
        this.param = new NumericExpression(split[1]);
        this.base = 10d;
    }

    public LogExpression(String operationAndBase, Expression param, boolean customBase) {
        String[] split = operationAndBase.split(" ");
        this.operation = split[0];
        this.param = param;
        this.base = new NumericExpression(split[1]).getValue();
        this.customBase = customBase;
    }

    @Override
    void eval() {
        System.out.println("Logarithms evaluation of " + operation);
        if((errorCode = param.getErrorCode()) != ExpressionError.NO_ERROR) {
            return;
        }
        if(param.isComplex()) {
            errorCode = ExpressionError.ILLEGAL_OPERATION_ON_VARIABLE;
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
