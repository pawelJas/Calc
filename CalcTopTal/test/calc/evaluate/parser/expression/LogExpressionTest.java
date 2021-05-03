package calc.evaluate.parser.expression;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LogExpressionTest {
    LogExpression logExpression;
    NumericExpression param = mock(NumericExpression.class);
    private final static double EPSILON = 0.00001;

    @Before
    public void setUp() throws Exception {
        when(param.getErrorCode()).thenReturn(ExpressionError.NO_ERROR);

        when(param.getValue()).thenReturn(123d);
    }

    @Test
    public void evalEscalateError() {
        when(param.getErrorCode()).thenReturn(ExpressionError.UNKNOWN_ERROR);
        logExpression = new LogExpression("ln", param);
        Assert.assertEquals(ExpressionError.UNKNOWN_ERROR, logExpression.getErrorCode());
    }

    @Test
    public void evalInvalidLogValue() {
        when(param.getValue()).thenReturn(-2.0);
        logExpression = new LogExpression("ln", param);
        Assert.assertEquals(ExpressionError.INVALID_ALGORITHM, logExpression.getErrorCode());
    }

    @Test
    public void evalInvalidLogBase() {
        logExpression = new LogExpression("ln", param, 0.0);
        Assert.assertEquals(ExpressionError.INVALID_ALGORITHM, logExpression.getErrorCode());
    }

    @Test
    public void evalLn() {
        logExpression = new LogExpression("ln", param);
        Assert.assertEquals(ExpressionError.NO_ERROR, logExpression.getErrorCode());
        Assert.assertEquals(4.81218435, logExpression.getValue(), EPSILON);
        when(param.getValue()).thenReturn(Math.E);
        logExpression = new LogExpression("ln", param);
        Assert.assertEquals(1, logExpression.getValue(), EPSILON);
    }

    @Test
    public void evalLog10() {
        logExpression = new LogExpression("log", param);
        Assert.assertEquals(2.08990511, logExpression.getValue(), EPSILON);
        when(param.getValue()).thenReturn(10d);
        logExpression = new LogExpression("log", param);
        Assert.assertEquals(1, logExpression.getValue(), EPSILON);
        logExpression = new LogExpression("log", param, 10);
        Assert.assertEquals(1, logExpression.getValue(), EPSILON);
        when(param.getValue()).thenReturn(100d);
        logExpression = new LogExpression("log", param);
        Assert.assertEquals(2, logExpression.getValue(), EPSILON);
        logExpression = new LogExpression("log", param, 10);
        Assert.assertEquals(2, logExpression.getValue(), EPSILON);
    }

    @Test
    public void evalLogAny() {
        logExpression = new LogExpression("log", param, 5);
        Assert.assertEquals(2.98997825, logExpression.getValue(), EPSILON);
        when(param.getValue()).thenReturn(256d);
        logExpression = new LogExpression("log", param,4);
        Assert.assertEquals(4, logExpression.getValue(), EPSILON);
        when(param.getValue()).thenReturn(121d);
        logExpression = new LogExpression("log", param,11);
        Assert.assertEquals(2, logExpression.getValue(), EPSILON);
    }
}
