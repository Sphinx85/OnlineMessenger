package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TextArea textArea, clientList;

    @FXML
    TextField messageField,
            serverAddress,
            serverPort,
            nickNameField,
            loginField,
            authLoginField;

    @FXML
    Button sendButton,
            connectButtonTest,
            authButtonTest,
            connectButton,
            registerButton,
            authButton;

    @FXML
    PasswordField passwordField,
            authPasswordField;

    @FXML
    VBox connectionPanel,
            authPanel;

    @FXML
    HBox messagePanel;

    private boolean connected;
    private boolean authentificated;

    public void setConnected (Boolean connected){
        this.connected = connected;
        messagePanel.setVisible(false);
        messagePanel.setManaged(false);
        connectionPanel.setVisible(!connected);
        connectionPanel.setManaged(!connected);
        authPanel.setVisible(false);
        authPanel.setManaged(false);
        clientList.setVisible(false);
    }

    public void setAuthentificated(Boolean authentificated){
        this.authentificated = authentificated;
        messagePanel.setVisible(authentificated);
        messagePanel.setManaged(authentificated);
        connectionPanel.setVisible(false);
        connectionPanel.setManaged(false);
        authPanel.setVisible(!authentificated);
        authPanel.setManaged(!authentificated);
        clientList.setVisible(authentificated);
    }

    public void sendMessage() {
        if (Network.sendMessage(messageField.getText())){
            messageField.clear();
            messageField.requestFocus();
        }
    }

    public void connect(){
        textArea.appendText("Уточните у администратора адрес и порт сервера\n");
        if (Network.connect(serverAddress.getText(), serverPort.getText())){
            setAuthentificated(false);
            serverAddress.clear();
            serverPort.clear();
            textArea.clear();
        } else textArea.appendText("Не удалось подключиться к серверу \n");
    }

    /**
     * Подключение для тестовой кнопки
     */
    public void locCon(){
        Network.connect("localhost","8189");
        setAuthentificated(false);
    }

    public void registration(){
        if (Network.registration(nickNameField.getText(), loginField.getText(), passwordField.getText())){
            nickNameField.clear();
            loginField.clear();
            passwordField.clear();
            authLoginField.requestFocus();
        }
    }

    public void authorization(){
        if (Network.authorization(authLoginField.getText(),authPasswordField.getText())){
            authLoginField.clear();
            authPasswordField.clear();
            messageField.requestFocus();
        }
    }
    public void authorizationTest(){
        Network.authorization("admin","admin");
    }

    private void callbacksLinks(){
        Network.setAuthorizationCallback(args -> setAuthentificated(true));
        Network.setConnectionCallback(args -> setConnected(true));
        Network.setSendMessageCallback(args -> {
            String message = args[0].toString();
            textArea.appendText(message + "\n");
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setConnected(false);
        callbacksLinks();
    }
}