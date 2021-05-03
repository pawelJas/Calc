package calc.evaluate.parser.expression;

public class LogExpression extends Expression   {
    String operation;
    Expression param;

    public LogExpression(String operation, Expression param) {
        this.operation = operation;
        this.param = param;
    }

    @Override
    void eval() {
        System.out.println("Logarithms evaluation of " + operation);
    }
}
