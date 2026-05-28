module yacute.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.net.http;
    requires org.json;

    opens com.yacute.app to javafx.fxml;
    exports com.yacute.app;
}