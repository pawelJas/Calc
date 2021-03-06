package calc.evaluate.parser.expression;

public class ArithmeticExpression extends Expression  {
    Expression e1;
    Expression e2;

    public ArithmeticExpression(String operation, Expression e1, Expression e2) {
        this.operation = operation;
        this.e1 = e1;
        this.e2 = e2;
    }

    public ArithmeticExpression(String quickMultiplication) {
        String[] split = quickMultiplication.split(" ");
        this.operation = "*";
        this.e1 = new NumericExpression(split[0]);
        this.e2 = new NumericExpression(split[1]);
    }

    @Override
    void eval() {
        System.out.println("Arithmetic evaluation of " + operation);
        if((errorCode = e1.getErrorCode()) != ExpressionError.NO_ERROR) {
            return;
        }
        if((errorCode = e2.getErrorCode()) != ExpressionError.NO_ERROR) {
            return;
        }
        if(e1.isComplex() && e2.isComplex() && (e1.getVariableName() != e2.getVariableName())){
            errorCode = ExpressionError.MULTIPLE_VARIABLES;
            return;
        }
        switch(operation) {
            case "+":
                if(e1.isComplex() || e2.isComplex()){
                    variableVal = e1.getVariableValue() + e2.getVariableValue();
                }
                 val = e1.getValue() + e2.getValue();
                break;
            case "-":
                if(e1.isComplex() || e2.isComplex()){
                    variableVal = e1.getVariableValue() - e2.getVariableValue();
                }
                val = e1.getValue() - e2.getValue();
                break;
            case "*":
                multiple();
                break;
            case "/":
                divide();
                break;
            case "=":
                equals();
                break;
        }
        variableName = e1.isComplex() ? e1.getVariableName() : e2.getVariableName();
    }

    void multiple() {
        if(e1.isComplex() && e2.isComplex()) {
            errorCode = ExpressionError.ONLY_LINEAR_EQUATIONS;
            return;
        }
        if(e1.isComplex()){
            variableVal = e1.getVariableValue() * e2.getValue();
        }
        else if(e2.isComplex()) {
            variableVal = e2.getVariableValue() * e1.getValue();
        }
        val = e1.getValue() * e2.getValue();
    }

    void divide() {
        if(e2.isComplex()) {
            errorCode = ExpressionError.ONLY_LINEAR_EQUATIONS;
            return;
        }
        if(e2.getValue() == 0d){
            errorCode = ExpressionError.DIVISION_BY_ZERO;
            return;
        }
        if(e1.isComplex()){
            variableVal = e1.getVariableValue() / e2.getValue();
        }
        val = e1.getValue() / e2.getValue();
    }

    void equals() {
        if(e1.getVariableValue() == e2.getVariableValue()){
            errorCode = ExpressionError.INVALID_EQUATION;
            return;
        }
        val = (e2.getValue() - e1.getValue()) / (e1.getVariableValue() - e2.getVariableValue());
    }
}
