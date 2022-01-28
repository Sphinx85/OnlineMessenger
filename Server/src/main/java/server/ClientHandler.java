package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private String nickName;
    private Server server;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public String getNickName() {
        return nickName;
    }

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());

            new Thread(() ->{
                try {
                    while (true){
                        String message = inputStream.readUTF();
                        if (message.startsWith("/auth ")){
                            String [] tokens = message.split("\\s");
                            String nick = server.getAuthService().getNickName(tokens[1],tokens[2]);

                            if (nick != null && !server.nickIsBusy(nick)){
                                nickName = nick;
                                server.subscribe(this);
                                server.consoleMessage("Клиент " + nickName + " авторизовался");
                                break;
                            } else sendMessage("Данное имя занято, либо не зарегистрировано");
                        }
                        if (message.startsWith("/register ")){
                            String [] tokens = message.split("\\s");
                            if (!server.nickIsBusy(tokens[1])){
                                server.getAuthService().registration(tokens[1], tokens[2],tokens[3]);
                                server.consoleMessage("Новый клиент " + tokens[1] + " зарегистрировался");
                            } else {
                                try {
                                    outputStream.writeUTF("Данное имя пользователя занято");
                                } catch (IOException o){
                                    o.printStackTrace();
                                }
                            }
                        }

                    }

                    while (true){
                        String message = inputStream.readUTF();
                        if (message.equals("/end")){
                            break;
                        }
                        server.broadcastMessage(nickName + ": " + message);
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
        server.consoleMessage("Клиент " + nickName + " отключился от сервера");
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