package server;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

public class Controller implements Initializable {

    @FXML
    TextArea textArea;

    private Vector<ClientHandler> clients;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread thread = new Thread(() ->{
            try(ServerSocket serverSocket = new ServerSocket(8189)) {
                clients = new Vector<>();
                System.out.println("Сервер включен на порту 8189");
                consoleMessage("Сервер включен на порту 8189");


                while (true){
                    Socket socket = serverSocket.accept();
                    subscribes(new ClientHandler(this,socket));
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            System.out.println("Сервер завершил работу");
        });
        thread.setDaemon(true);
        thread.start();


    }

    protected void consoleMessage(String message) {
        textArea.appendText(message + "\n");
    }

    private void subscribes(ClientHandler clientHandler) {
        clients.add(clientHandler);
        consoleMessage("Зашел новый пользователь");
        System.out.println("Зашел новый пользователь");
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client: clients){
            client.sendMessage(message);
        }
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Пользователь вышел");
        consoleMessage("Пользователь вышел");
    }
}
