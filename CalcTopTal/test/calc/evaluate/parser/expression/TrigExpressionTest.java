package calc.evaluate.parser.expression;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrigExpressionTest {
    TrigExpression trigExpression;
    NumericExpression param = mock(NumericExpression.class);
    private final static double EPSILON = 0.00001;



    @Before
    public void setUp() throws Exception {
        when(param.getErrorCode()).thenReturn(ExpressionError.NO_ERROR);

        when(param.getValue()).thenReturn(Math.PI);
    }

    @Test
    public void evalEscalateError() {
        when(param.getErrorCode()).thenReturn(ExpressionError.UNKNOWN_ERROR);
        trigExpression = new TrigExpression("cos", param);
        Assert.assertEquals(ExpressionError.UNKNOWN_ERROR, trigExpression.getErrorCode());
    }

    @Test
    public void evalSin() {
        trigExpression = new TrigExpression("sin", param);
        Assert.assertEquals(0.0, trigExpression.getValue(), EPSILON);
    }

    @Test
    public void evalCos() {
        trigExpression = new TrigExpression("cos", param);
        Assert.assertEquals(-1.0, trigExpression.getValue(), EPSILON);
    }

    @Test
    public void evalTan() {
        trigExpression = new TrigExpression("tan", param);
        when(param.getValue()).thenReturn(3* Math.PI / 4.0);
        Assert.assertEquals(-1.0, trigExpression.getValue(), EPSILON);
    }

    @Test
    public void evalCtan() {
        trigExpression = new TrigExpression("ctan", param);
        when(param.getValue()).thenReturn(3* Math.PI / 4.0);
        Assert.assertEquals(-1.0, trigExpression.getValue(), EPSILON);
    }
}
