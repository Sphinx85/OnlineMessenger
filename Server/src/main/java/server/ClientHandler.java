package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Controller server;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ClientHandler(Controller server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());

            new Thread(() ->{
                try {
                    while (true){
                        String message = inputStream.readUTF();
                        if (message.equals("/end")){
                            break;
                        }
                        server.consoleMessage("Клиент отправил сообщение: " + message);
                        server.broadcastMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message){
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {

        server.unsubscribe(this);

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
