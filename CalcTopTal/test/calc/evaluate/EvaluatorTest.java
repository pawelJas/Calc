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
        Assert.assertEquals("2", evaluator.evalRpn());
        Assert.assertEquals("2", evaluator.evalInfix());
    }

    @Test
    public void evalUnknownSymbolError() {
        evaluator = new Evaluator("^^");
        Assert.assertEquals("Parsing Error: Unknown object in input", evaluator.evalRpn());
        Assert.assertEquals("Parsing Error: Unknown object in input", evaluator.evalInfix());
    }

    @Test
    public void evalUnknownKeywordError() {
        evaluator = new Evaluator("2 + LAMA");
        Assert.assertEquals("Parsing Error: Invalid keyword", evaluator.evalRpn());
        Assert.assertEquals("Parsing Error: Invalid keyword", evaluator.evalInfix());
    }

    @Test
    public void evalInvalidNumberError() {
        evaluator = new Evaluator("2 + 55.4.3");
        Assert.assertEquals("Parsing Error: Invalid number", evaluator.evalRpn());
        Assert.assertEquals("Parsing Error: Invalid number", evaluator.evalInfix());
    }

    @Test
    public void evalDivByZero() {
        evaluator = new Evaluator("2 0 /");
        Assert.assertEquals("Expression evaluation error: DIVISION_BY_ZERO", evaluator.evalRpn());
        evaluator = new Evaluator("2 / 0");
        Assert.assertEquals("Expression evaluation error: DIVISION_BY_ZERO", evaluator.evalInfix());
    }

    @Test
    public void evalLogZero() {
        evaluator = new Evaluator("2 0 1 - / ln");
        Assert.assertEquals("Expression evaluation error: INVALID_ALGORITHM", evaluator.evalRpn());
        evaluator = new Evaluator("ln(2 /( 0 - 1 ))");
        Assert.assertEquals("Expression evaluation error: INVALID_ALGORITHM", evaluator.evalInfix());
    }

    @Test
    public void evalSinCos() {
        evaluator = new Evaluator("pi sin pi cos +");
        Assert.assertEquals(-1d, Double.parseDouble(evaluator.evalRpn()), EPSILON);
        evaluator = new Evaluator("Sinpi + cos(Pi)");
        Assert.assertEquals(-1d, Double.parseDouble(evaluator.evalInfix()), EPSILON);
        evaluator = new Evaluator("cos0 cospi -");
        Assert.assertEquals(2d, Double.parseDouble(evaluator.evalRpn()), EPSILON);
        evaluator = new Evaluator("cos0 -cospi");
        Assert.assertEquals(2d, Double.parseDouble(evaluator.evalInfix()), EPSILON);
    }

    @Test
    public void evalTanCtan() {
        evaluator = new Evaluator("5 tan ctan5 *");
        Assert.assertEquals(1d, Double.parseDouble(evaluator.evalRpn()), EPSILON);
        evaluator = new Evaluator("tan5 5 ctan *");
        Assert.assertEquals(1d, Double.parseDouble(evaluator.evalRpn()), EPSILON);
        evaluator = new Evaluator("tan5 * ctan5");
        Assert.assertEquals(1d, Double.parseDouble(evaluator.evalInfix()), EPSILON);
        evaluator = new Evaluator("tan(5) *ctan(5)");
        Assert.assertEquals(1d, Double.parseDouble(evaluator.evalInfix()), EPSILON);
    }

    @Test
    public void evalLog() {
        evaluator = new Evaluator("1000 log e ln e e /++");
        Assert.assertEquals(5d, Double.parseDouble(evaluator.evalRpn()), EPSILON);
        evaluator = new Evaluator("log1000 + lne + e/e");
        Assert.assertEquals(5d, Double.parseDouble(evaluator.evalInfix()), EPSILON);
        evaluator = new Evaluator("8 log2 lne +");
        Assert.assertEquals(4d, Double.parseDouble(evaluator.evalRpn()), EPSILON);
        evaluator = new Evaluator("log2(8) + ln(e)");
        Assert.assertEquals(4d, Double.parseDouble(evaluator.evalInfix()), EPSILON);
    }

    @Test
    public void evalVariable() {
        evaluator = new Evaluator("2 x * 5 + 3x =");
        Assert.assertEquals("x = 5", evaluator.evalRpn());
        evaluator = new Evaluator("2 * x + 5 = 3x");
        Assert.assertEquals("x = 5", evaluator.evalInfix());
        evaluator = new Evaluator("2 x * 10 + 5 =");
        Assert.assertEquals("x = -2.5", evaluator.evalRpn());
        evaluator = new Evaluator("2 * x + 10 = 5");
        Assert.assertEquals("x = -2.5", evaluator.evalInfix());
    }

    @Test
    public void exampleTests() {
        evaluator = new Evaluator("(3+(4-1))*5");
        Assert.assertEquals("30", evaluator.evalInfix());
        evaluator = new Evaluator("2 * x + 0.5 = 1");
        Assert.assertEquals("x = 0.25", evaluator.evalInfix());
        evaluator = new Evaluator("2 * x + 1 = 2*(1-x)");
        Assert.assertEquals("x = 0.25", evaluator.evalInfix());
        evaluator = new Evaluator("Log(10)");
        Assert.assertEquals("1", evaluator.evalInfix());
        evaluator = new Evaluator("Log10");
        Assert.assertEquals("1", evaluator.evalInfix());
        evaluator = new Evaluator("Log100(10)");
        Assert.assertEquals("0.5", evaluator.evalInfix());
        evaluator = new Evaluator("sin(pi)");
        Assert.assertEquals("0", evaluator.evalInfix());
        evaluator = new Evaluator("sinpi");
        Assert.assertEquals("0", evaluator.evalInfix());
        evaluator = new Evaluator("sin(1.5pi)");
        Assert.assertEquals("-1", evaluator.evalInfix());
        evaluator = new Evaluator("sin(1.5*pi)");
        Assert.assertEquals("-1", evaluator.evalInfix());
    }
}
