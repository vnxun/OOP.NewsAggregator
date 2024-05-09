module com.xun {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.xun to javafx.fxml;
    exports com.xun;
}
