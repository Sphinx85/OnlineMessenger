package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TextArea textArea;

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

    @FXML
    ListView<String> clientList;

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
        textArea.clear();
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
        } else setAuthentificated(false);
    }
    public void authorizationTest(){
        Network.authorization("admin","admin");
    }

    private void callbacksLinks(){
        Network.setClientListCall(args -> {
            clientList.getItems().clear();
            String message = args[0].toString();
            String[] tokens = message.split("\\s");
            for (int i = 1; i < tokens.length; i++) {
                clientList.getItems().add(tokens[i]);

            }
        });
        Network.setAuthorizationCallback(args -> {
            if (args[0].equals(true)) {
                setAuthentificated(true);
            } else setAuthentificated(false);
        });
        Network.setConnectionCallback(args -> setConnected(true));
        Network.setSendMessageCallback(args -> {
            String message = args[0].toString();
            textArea.appendText(message + "\n");
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setConnected(false);
        clientList.setOnMouseClicked(event -> {
            String nickName = clientList.getSelectionModel().getSelectedItem();
            messageField.appendText("/wisp " + nickName + " ");
            messageField.requestFocus();
            messageField.selectEnd();
        });
        callbacksLinks();
    }
}