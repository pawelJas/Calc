package calc.evaluate.parser.expression;

public class TrigExpression extends Expression   {
    String operation;
    Expression param;

    public TrigExpression(String operation, Expression param) {
        this.operation = operation;
        this.param = param;
    }

    @Override
    void eval() {
        System.out.println("Trigonometric evaluation of " + operation);
        if((errorCode = param.getErrorCode()) != ExpressionError.NO_ERROR) {
            return;
        }
        double radians = param.getValue();
        switch(operation) {
            case "sin":
                val = Math.sin(radians);
                break;
            case "cos":
                val = Math.cos(radians);
                break;
            case "tan":
                val = Math.tan(radians);
                break;
            case "ctan":
                val = 1.0 / Math.tan(radians);
                break;
        }
    }
}
