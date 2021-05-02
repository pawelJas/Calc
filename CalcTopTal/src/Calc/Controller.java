package Calc;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    public TextField inputText;
    public Label resultLabel;
    public Label resultTitle;

    public void evaluate(ActionEvent actionEvent) {
        resultLabel.setText("4");
    }
}

