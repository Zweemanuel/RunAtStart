module com.runatstart {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.runatstart to javafx.fxml;
    exports com.runatstart;
}
