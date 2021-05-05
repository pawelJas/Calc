package calc.evaluate;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EvaluatorTest {
    Evaluator evaluator;
    private final static double EPSILON = 0.00001;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void evalSimple() {
        evaluator = new Evaluator("2");
        Assert.assertEquals("2.0", evaluator.eval());
    }

    @Test
    public void evalUnknownSymbolError() {
        evaluator = new Evaluator("^^");
        Assert.assertEquals("Parsing Error: Unknown object in input", evaluator.eval());
    }

    @Test
    public void evalUnknownKeywordError() {
        evaluator = new Evaluator("2 + LAMA");
        Assert.assertEquals("Parsing Error: Invalid keyword", evaluator.eval());
    }

    @Test
    public void evalInvalidNumberError() {
        evaluator = new Evaluator("2 + 55.4.3");
        Assert.assertEquals("Parsing Error: Invalid number", evaluator.eval());
    }

    @Test
    public void evalDivByZero() {
        evaluator = new Evaluator("2 0 /");
        Assert.assertEquals("Expression evaluation error: DIVISION_BY_ZERO", evaluator.eval());
    }

    @Test
    public void evalLogZero() {
        evaluator = new Evaluator("2 0 1 - / ln");
        Assert.assertEquals("Expression evaluation error: INVALID_ALGORITHM", evaluator.eval());
    }

    @Test
    public void evalSinCos() {
        evaluator = new Evaluator("pi sin pi cos +");
        Assert.assertEquals(-1d, Double.parseDouble(evaluator.eval()), EPSILON);
    }

    @Test
    public void evalTanCtan() {
        evaluator = new Evaluator("5 tan 5 ctan *");
        Assert.assertEquals(1d, Double.parseDouble(evaluator.eval()), EPSILON);
    }

    @Test
    public void evalLog() {
        evaluator = new Evaluator("1000 log e ln e e /++");
        Assert.assertEquals(5d, Double.parseDouble(evaluator.eval()), EPSILON);
    }

    @Test
    public void evalVariable() {
        evaluator = new Evaluator("2 x * 5 + 3x =");
        Assert.assertEquals("x = 5.0", evaluator.eval());
        evaluator = new Evaluator("2 x * 10 + 5 =");
        Assert.assertEquals("x = -2.5", evaluator.eval());
    }
}
