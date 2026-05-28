package com.yacute.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private ImageView imageView;
    @FXML private TableView<LogEntry> logTable;
    @FXML private TableColumn<LogEntry, String> dateTimeColumn;

    private final SupabaseService db = new SupabaseService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(getClass().getResourceAsStream("/com/yacute/app/image.png"));
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(250);

        dateTimeColumn.setCellValueFactory(cellData ->
                cellData.getValue().dateTimeProperty()
        );

        try {
            ObservableList<LogEntry> data = FXCollections.observableArrayList(db.fetchLogs());
            logTable.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }
}