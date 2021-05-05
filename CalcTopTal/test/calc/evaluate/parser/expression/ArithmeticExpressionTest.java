package calc.evaluate.parser.expression;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ArithmeticExpressionTest {
    ArithmeticExpression arithmeticExpression;
    NumericExpression e1 = mock(NumericExpression.class);
    NumericExpression e2 = mock(NumericExpression.class);
    NumericExpression eComplex1 = mock(NumericExpression.class);
    NumericExpression eComplex2 = mock(NumericExpression.class);
    double val1 = 2.5;
    double val2 = 0.75;
    double complexVal1 = 1.0;
    double complexVal1var = 3.0;
    double complexVal2 = 2;
    double complexVal2var = 3.5;
    private final static double EPSILON = 0.00001;

    @Before
    public void setUp() throws Exception {
        when(e1.getErrorCode()).thenReturn(ExpressionError.NO_ERROR);
        when(e2.getErrorCode()).thenReturn(ExpressionError.NO_ERROR);
        when(eComplex1.getErrorCode()).thenReturn(ExpressionError.NO_ERROR);
        when(eComplex2.getErrorCode()).thenReturn(ExpressionError.NO_ERROR);
        when(e1.isComplex()).thenReturn(false);
        when(e2.isComplex()).thenReturn(false);
        when(eComplex1.isComplex()).thenReturn(true);
        when(eComplex2.isComplex()).thenReturn(true);
        when(e1.getValue()).thenReturn(val1);
        when(e2.getValue()).thenReturn(val2);
        when(e1.getVariableValue()).thenReturn(0d);
        when(e2.getVariableValue()).thenReturn(0d);
        when(eComplex1.getValue()).thenReturn(complexVal1);
        when(eComplex2.getValue()).thenReturn(complexVal2);
        when(eComplex1.getVariableName()).thenReturn('x');
        when(eComplex2.getVariableName()).thenReturn('x');
        when(eComplex1.getVariableValue()).thenReturn(complexVal1var);
        when(eComplex2.getVariableValue()).thenReturn(complexVal2var);
    }



    @Test
    public void evalEscalateErrorsInOrderOfElements() {
        when(e2.getErrorCode()).thenReturn(ExpressionError.UNKNOWN_ERROR);
        arithmeticExpression = new ArithmeticExpression("+", e1, e2);
        Assert.assertEquals(ExpressionError.UNKNOWN_ERROR, arithmeticExpression.getErrorCode());
        when(e1.getErrorCode()).thenReturn(ExpressionError.DIVISION_BY_ZERO);
        arithmeticExpression = new ArithmeticExpression("+", e1, e2);
        Assert.assertEquals(ExpressionError.DIVISION_BY_ZERO, arithmeticExpression.getErrorCode());
    }

    @Test
    public void evalCacheErrorResults() {
        arithmeticExpression = new ArithmeticExpression("+", e1, e2);
        when(e2.getErrorCode()).thenReturn(ExpressionError.UNKNOWN_ERROR, ExpressionError.NO_ERROR);
        Assert.assertEquals(ExpressionError.UNKNOWN_ERROR, arithmeticExpression.getErrorCode());
        Assert.assertEquals(ExpressionError.UNKNOWN_ERROR, arithmeticExpression.getErrorCode());
    }

    @Test
    public void evalMultipleVariablesError() {
        arithmeticExpression = new ArithmeticExpression("+", eComplex1, eComplex2);
        when(eComplex1.getVariableName()).thenReturn('x');
        when(eComplex2.getVariableName()).thenReturn('y');
        Assert.assertEquals(ExpressionError.MULTIPLE_VARIABLES, arithmeticExpression.getErrorCode());
    }

    @Test
    public void evalNotLinearError() {
        arithmeticExpression = new ArithmeticExpression("*", eComplex1, eComplex2);
        Assert.assertEquals(ExpressionError.ONLY_LINEAR_EQUATIONS, arithmeticExpression.getErrorCode());
        arithmeticExpression = new ArithmeticExpression("/", eComplex1, eComplex2);
        Assert.assertEquals(ExpressionError.ONLY_LINEAR_EQUATIONS, arithmeticExpression.getErrorCode());
        arithmeticExpression = new ArithmeticExpression("/", e1, eComplex2);
        Assert.assertEquals(ExpressionError.ONLY_LINEAR_EQUATIONS, arithmeticExpression.getErrorCode());
    }

    @Test
    public void evalDivBy0Error() {
        when(e2.getValue()).thenReturn(0d);
        arithmeticExpression = new ArithmeticExpression("/", e1, e2);
        Assert.assertEquals(ExpressionError.DIVISION_BY_ZERO, arithmeticExpression.getErrorCode());
    }

    @Test
    public void evalAdd() {
        arithmeticExpression = new ArithmeticExpression("+", e1, e2);
        Assert.assertEquals(val1 + val2, arithmeticExpression.getValue(), EPSILON);
        Assert.assertFalse(arithmeticExpression.isComplex());
    }

    @Test
    public void evalAddComplex() {
        arithmeticExpression = new ArithmeticExpression("+", e1, eComplex1);
        Assert.assertEquals(val1 + complexVal1, arithmeticExpression.getValue(), EPSILON);
        Assert.assertTrue(arithmeticExpression.isComplex());
        Assert.assertEquals(complexVal1var, arithmeticExpression.getVariableValue(), EPSILON);
        arithmeticExpression = new ArithmeticExpression("+", eComplex2, e2);
        Assert.assertEquals(val2 + complexVal2, arithmeticExpression.getValue(), EPSILON);
        Assert.assertTrue(arithmeticExpression.isComplex());
        Assert.assertEquals(complexVal2var, arithmeticExpression.getVariableValue(), EPSILON);
        arithmeticExpression = new ArithmeticExpression("+", eComplex2, eComplex1);
        Assert.assertEquals(complexVal1 + complexVal2, arithmeticExpression.getValue(), EPSILON);
        Assert.assertTrue(arithmeticExpression.isComplex());
        Assert.assertEquals(complexVal1var + complexVal2var, arithmeticExpression.getVariableValue(), EPSILON);
    }

    @Test
    public void evalSub() {
        arithmeticExpression = new ArithmeticExpression("-", e1, e2);
        Assert.assertEquals(val1 - val2, arithmeticExpression.getValue(), EPSILON);
        Assert.assertFalse(arithmeticExpression.isComplex());
    }

    @Test
    public void evalSubComplex() {
        arithmeticExpression = new ArithmeticExpression("-", e1, eComplex1);
        Assert.assertEquals(val1 - complexVal1, arithmeticExpression.getValue(), EPSILON);
        Assert.assertTrue(arithmeticExpression.isComplex());
        Assert.assertEquals(0 - complexVal1var, arithmeticExpression.getVariableValue(), EPSILON);
        arithmeticExpression = new ArithmeticExpression("-", eComplex2, e2);
        Assert.assertEquals(complexVal2 - val2, arithmeticExpression.getValue(), EPSILON);
        Assert.assertTrue(arithmeticExpression.isComplex());
        Assert.assertEquals(complexVal2var, arithmeticExpression.getVariableValue(), EPSILON);
        arithmeticExpression = new ArithmeticExpression("-", eComplex2, eComplex1);
        Assert.assertEquals(complexVal2 -  complexVal1, arithmeticExpression.getValue(), EPSILON);
        Assert.assertTrue(arithmeticExpression.isComplex());
        Assert.assertEquals(complexVal2var - complexVal1var, arithmeticExpression.getVariableValue(), EPSILON);
    }

    @Test
    public void evalMulti() {
        arithmeticExpression = new ArithmeticExpression("*", e1, e2);
        Assert.assertEquals(val1 * val2, arithmeticExpression.getValue(), EPSILON);
        Assert.assertFalse(arithmeticExpression.isComplex());
    }

    @Test
    public void evalMultiComplex() {
        arithmeticExpression = new ArithmeticExpression("*", e1, eComplex2);
        Assert.assertEquals(val1 * complexVal2, arithmeticExpression.getValue(), EPSILON);
        Assert.assertTrue(arithmeticExpression.isComplex());
        Assert.assertEquals(val1 * complexVal2var, arithmeticExpression.getVariableValue(), EPSILON);
        arithmeticExpression = new ArithmeticExpression("*", eComplex1, e2);
        Assert.assertEquals(val2 * complexVal1, arithmeticExpression.getValue(), EPSILON);
        Assert.assertTrue(arithmeticExpression.isComplex());
        Assert.assertEquals(val2 * complexVal1var, arithmeticExpression.getVariableValue(), EPSILON);
    }

    @Test
    public void evalDiv() {
        arithmeticExpression = new ArithmeticExpression("/", e1, e2);
        Assert.assertEquals(val1 / val2, arithmeticExpression.getValue(), EPSILON);
        Assert.assertFalse(arithmeticExpression.isComplex());
    }

    @Test
    public void evalDivComplex() {
        arithmeticExpression = new ArithmeticExpression("/", eComplex1, e2);
        Assert.assertEquals(complexVal1 / val2, arithmeticExpression.getValue(), EPSILON);
        Assert.assertTrue(arithmeticExpression.isComplex());
        Assert.assertEquals(complexVal1var / val2, arithmeticExpression.getVariableValue(), EPSILON);
    }
}
