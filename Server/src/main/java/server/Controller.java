package server;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TextArea textArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread thread = new Thread(() ->{
            linkCallback();
            new Server();
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void linkCallback() {
        Server.setConsoleMessage(args -> {
            String message = args[0].toString();
            textArea.appendText(message + "\n");
        });
    }
}