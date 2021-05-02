package calc;

import calc.evaluate.Evaluator;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    public TextField inputText;
    public Label resultLabel;
    public Label resultTitle;

    public void evaluate(ActionEvent actionEvent) {
        Evaluator evaluator = new Evaluator(inputText.getText());
        resultLabel.setText(evaluator.eval());
    }
}

