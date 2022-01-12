package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TextArea textArea;

    @FXML
    TextField textField;

    @FXML
    Button sendButton;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void sendMessage() {
        try {
            dataOutputStream.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("10.100.12.45",8189);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            Thread thread = new Thread(() -> {
               try {
                   while (true){
                       String message = dataInputStream.readUTF();
                       textArea.appendText(message + "\n");
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               } finally {
                   closeConnection();
               }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {

    }

    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
