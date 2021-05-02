package calc.evaluate.parser.expression;

public class NumericExpression extends Expression {
    String numericInput;

    public NumericExpression(String numericInput) {
        this.numericInput = numericInput;
    }

    @Override
    void eval() {
        val = Double.parseDouble(numericInput);
    }
}
