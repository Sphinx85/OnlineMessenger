package server;

public interface AuthService {
    String getNickName(String login, String password);

    void registration(String token, String token1, String token2);
}
