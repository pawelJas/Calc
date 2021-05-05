package calc.evaluate.parser.expression;

public class NumericExpression extends Expression {
    String input;

    public NumericExpression(String input) {
        this.input = input;
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
            case 'x':
            case 'y':
                val = 0d;
                variableName = input.charAt(0);
                variableVal = 1d;
                break;
            default:
                val = Double.parseDouble(input);
        }
    }
}
