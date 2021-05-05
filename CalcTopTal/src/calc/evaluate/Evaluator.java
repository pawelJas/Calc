package calc.evaluate;

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
            return rpnParser.getRootExpression().toString();
        }
        return rpnParser.getError();
    }
}
