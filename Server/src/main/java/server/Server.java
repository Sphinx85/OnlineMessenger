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
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client: clients){
            client.sendMessage(message);
        }
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public void consoleMessage(String message) {
        consoleMessage.callback(message);
    }

    public boolean nickIsBusy(String nickName) {
        for (ClientHandler o : clients){
            if (o.getNickName().equals(nickName)){
                return true;
            }
        }
        return false;
    }
}