package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class StartServer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/console.fxml")));
        primaryStage.setTitle("Sphinx Messenger Server");
        primaryStage.setScene(new Scene(parent,400,400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
