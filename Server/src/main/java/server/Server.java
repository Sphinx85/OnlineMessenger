package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

    public static Callback consoleMessage;

    static {
        consoleMessage = args -> { };
    }

    public static void setConsoleMessage(Callback consoleMessage) {
        Server.consoleMessage = consoleMessage;
    }

    private final Vector<ClientHandler> clients;
    private final AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        clients = new Vector<>();
        authService = new AuthentificationService();

        try(ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен на порту 8189");
            consoleMessage("Сервер включен на порту 8189");
            while (true){
                Socket socket = serverSocket.accept();
                new ClientHandler(this,socket);
                consoleMessage("Подключился новый клиент");
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            consoleMessage("Сервер завершил работу");
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void broadcastClientList(){
        StringBuilder list = new StringBuilder();
        list.append("/clientList ");
        for (ClientHandler client : clients){
            list.append(client.getNickName()).append(" ");
        }
        list.setLength(list.length() - 1);
        String clientList = list.toString();
        broadcastMessage(clientList);
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client: clients){
            client.sendMessage(message);
        }
    }

    public void privateMessage(ClientHandler sender, String received, String message){
        if (sender.getNickName().equals(received)){
            sender.sendMessage("Заметка: " + message);
            return;
        }
        for (ClientHandler client: clients){
            if (client.getNickName().equals(received)){
                client.sendMessage("От " + sender.getNickName() + ": " + message);
                sender.sendMessage("Для " + received + ": " + message);
                return;
            }
        }
        sender.sendMessage("Клиент " + received + " не найден!");
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public void consoleMessage(String message) {
        consoleMessage.callback(message);
    }

    public boolean nickIsBusy(String nickName, String login) {
        for (ClientHandler o : clients){
            if (o.getNickName().equals(nickName) || o.getLoginName().equals(login)){
                return true;
            }
        }
        return false;
    }
}