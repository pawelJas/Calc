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
    double val1 = 2.5;
    double val2 = 0.75;
    private final static double EPSILON = 0.00001;

    @Before
    public void setUp() throws Exception {
        when(e1.getErrorCode()).thenReturn(ExpressionError.NO_ERROR);
        when(e2.getErrorCode()).thenReturn(ExpressionError.NO_ERROR);

        when(e1.getValue()).thenReturn(val1);
        when(e2.getValue()).thenReturn(val2);
    }



    @Test
    public void evalEscalateErrorsInOrderOfElements() {
        when(e2.getErrorCode()).thenReturn(ExpressionError.UNKNOWN_ERROR);
        arithmeticExpression = new ArithmeticExpression("+", e1, e2);
        Assert.assertEquals(ExpressionError.UNKNOWN_ERROR, arithmeticExpression.getErrorCode());
        when(e1.getErrorCode()).thenReturn(ExpressionError.DIVIDE_BY_ZERO);
        arithmeticExpression = new ArithmeticExpression("+", e1, e2);
        Assert.assertEquals(ExpressionError.DIVIDE_BY_ZERO, arithmeticExpression.getErrorCode());
    }

    @Test
    public void evalCacheErrorResults() {
        arithmeticExpression = new ArithmeticExpression("+", e1, e2);
        when(e2.getErrorCode()).thenReturn(ExpressionError.UNKNOWN_ERROR);
        Assert.assertEquals(ExpressionError.UNKNOWN_ERROR, arithmeticExpression.getErrorCode());
        when(e1.getErrorCode()).thenReturn(ExpressionError.NO_ERROR);
        when(e2.getErrorCode()).thenReturn(ExpressionError.DIVIDE_BY_ZERO);
        Assert.assertEquals(ExpressionError.UNKNOWN_ERROR, arithmeticExpression.getErrorCode());
    }

    @Test
    public void evalSum() {
        arithmeticExpression = new ArithmeticExpression("+", e1, e2);
        Assert.assertEquals(val1 + val2, arithmeticExpression.getValue(), EPSILON);
    }

    @Test
    public void evalSub() {
        arithmeticExpression = new ArithmeticExpression("-", e1, e2);
        Assert.assertEquals(val1 - val2, arithmeticExpression.getValue(), EPSILON);
    }

    @Test
    public void evalMulti() {
        arithmeticExpression = new ArithmeticExpression("*", e1, e2);
        Assert.assertEquals(val1 * val2, arithmeticExpression.getValue(), EPSILON);
    }

    @Test
    public void evalDiv() {
        arithmeticExpression = new ArithmeticExpression("/", e1, e2);
        Assert.assertEquals(val1 / val2, arithmeticExpression.getValue(), EPSILON);
    }

    @Test
    public void evalDivBy0() {
        when(e2.getValue()).thenReturn(0d);
        arithmeticExpression = new ArithmeticExpression("/", e1, e2);
        Assert.assertEquals(ExpressionError.DIVIDE_BY_ZERO, arithmeticExpression.getErrorCode());
    }
}
