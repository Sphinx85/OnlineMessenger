package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {
    public static CallBack registrationCallback;
    public static CallBack sendMessageCallback;
    public static CallBack connectionCallback;
    public static CallBack authorizationCallback;
    public static CallBack warningCallback;
    public static CallBack alertCallback;

    static {
        CallBack empty = args -> {};
        registrationCallback = empty;
        sendMessageCallback = empty;
        connectionCallback = empty;
        authorizationCallback = empty;
        warningCallback = empty;
        alertCallback = empty;
    }

    public static void setRegistrationCallback(CallBack registrationCallback) {
        Network.registrationCallback = registrationCallback;
    }

    public static void setSendMessageCallback(CallBack sendMessageCallback) {
        Network.sendMessageCallback = sendMessageCallback;
    }

    public static void setConnectionCallback(CallBack connectionCallback) {
        Network.connectionCallback = connectionCallback;
    }

    public static void setAuthorizationCallback(CallBack authorizationCallback) {
        Network.authorizationCallback = authorizationCallback;
    }

    public static void setWarningCallback(CallBack warningCallback) {
        Network.warningCallback = warningCallback;
    }

    public static void setAlertCallback(CallBack alertCallback) {
        Network.alertCallback = alertCallback;
    }

    private static Socket socket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    public static boolean sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean connect(String address, String port) {
            try {
                socket = new Socket(address, Integer.parseInt(port));
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());

                Thread thread = new Thread(() -> {
                    try {
                        while (true){
                            String message = inputStream.readUTF();
                            sendMessageCallback.callback(message);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
                thread.setDaemon(true);
                thread.start();
                return true;
            } catch (IOException e){
                e.printStackTrace();
                return false;
            }
    }

    private static void closeConnection() {
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static boolean registration(String nickName, String login, String password) {
        try {
            outputStream.writeUTF("/register " + nickName + " " + login + " " + password);
            registrationCallback.callback();
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean authorization(String login, String password) {
        try {
            outputStream.writeUTF("/auth " + login + " " + password);
            authorizationCallback.callback();
            return true;

        } catch (IOException e){
            e.printStackTrace();
            return false;
        }

    }
}