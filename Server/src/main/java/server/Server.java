/*
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class Server {

    //private Vector<ClientHandler> clients;


    public Server() {
        clients = new Vector<>();
        try(ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен на порту 8189");
            while (true){
                Socket socket = serverSocket.accept();
                subscribe(new ClientHandler(this,socket));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }



    private void subscribe(ClientHandler clientHandler) {
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
}
*/
