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
    }
}
