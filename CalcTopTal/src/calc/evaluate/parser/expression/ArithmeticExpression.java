package calc.evaluate.parser.expression;

public class ArithmeticExpression extends Expression  {
    String operation;
    Expression e1;
    Expression e2;

    public ArithmeticExpression(String operation, Expression e1, Expression e2) {
        this.operation = operation;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    void eval() {
        if((errorCode = e1.getErrorCode()) != ExpressionError.NO_ERROR) {
            return;
        }
        if((errorCode = e2.getErrorCode()) != ExpressionError.NO_ERROR) {
            return;
        }
        switch(operation) {
            case "+":
                val = e1.getValue() + e2.getValue();
                break;
            case "-":
                val = e1.getValue() - e2.getValue();
                break;
            case "*":
                val = e1.getValue() * e2.getValue();
                break;
            case "/":
                if(e2.getValue() == 0d){
                    errorCode = ExpressionError.DIVIDE_BY_ZERO;
                    break;
                }
                val = e1.getValue() / e2.getValue();
                break;
        }
    }
}
