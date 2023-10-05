module brandon.secondscreen {
    requires javafx.controls;
    requires javafx.fxml;
    exports Controller to javafx.fxml;
    opens Controller to javafx.fxml;
    opens brandon.secondscreen to javafx.fxml;
    exports brandon.secondscreen;
}