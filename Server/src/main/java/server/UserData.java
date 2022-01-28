package server;

public class UserData {
    private String nickName;
    private String login;
    private String password;

    public UserData(String nickName, String login, String password) {
        this.nickName = nickName;
        this.login = login;
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
