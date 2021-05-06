package calc.evaluate;

import calc.evaluate.parser.InfixParser;
import calc.evaluate.parser.RpnParser;

public class Evaluator {
    String input;

    public Evaluator(String input) {
        this.input = input;
    }

    public String evalRpn() {
        RpnParser rpnParser = new RpnParser(input);
        boolean rpnParsingStatus = rpnParser.parse();
        if(rpnParsingStatus) {
            return format(rpnParser.getRootExpression().toString());
        }
        return rpnParser.getError();
    }

    public String evalInfix() {
        InfixParser infixParser = new InfixParser(input);
        boolean infixParserStatus = infixParser.parse();
        if(infixParserStatus) {
            return format(infixParser.getRootExpression().toString());
        }
        return infixParser.getError();
    }

    String format(String input) {
        if (input.endsWith(".0")) {
            return input.substring(0, input.length()-2);
        }
        return input;
    }
}
