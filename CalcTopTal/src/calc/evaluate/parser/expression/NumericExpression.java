package calc.evaluate.parser.expression;

public class NumericExpression extends Expression {
    String input;

    public NumericExpression(String numericInput) {
        this.input = numericInput;
    }

    @Override
    void eval() {
        switch(input.charAt(0)) {
            case 'p':
            case 'P':
                val = Math.PI;
                break;
            case 'e':
                val = Math.E;
                break;
            default:
                val = Double.parseDouble(input);
        }
    }
}
