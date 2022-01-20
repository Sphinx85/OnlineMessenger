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
                            if (nick != null || !server.nickIsBusy(nick)){
                                sendMessage("/autok ");
                                nickName = nick;
                                server.subscribe(this);
                                server.consoleMessage("Клиент " + nickName + " авторизовался");
                                break;
                            }
                        }
                        if (message.startsWith("/register ")){
                            String [] tokens = message.split("\\s");
                            server.getAuthService().registration(tokens[1], tokens[2],tokens[3]);
                            server.consoleMessage("Новый клиент " + tokens[1] + " зарегистрировался");
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