package server;

import java.util.ArrayList;
import java.util.List;

public class AuthentificationService implements AuthService{

    private List<UserData> users;

    public void setUsers(List users) {
        this.users = users;
    }

    public AuthentificationService() {
        this.users = new ArrayList<>();
        users.add(new UserData("Administrator", "admin", "admin"));
    }

    @Override
    public String getNickName(String login, String password) {
        for (UserData user : users){
            if (user.getLogin().equals(login) && user.getPassword().equals(password)){
                return user.getNickName();
            }
        }
        return null;
    }

    public void registration(String nickName, String login, String password) {
        users.add(new UserData(nickName,login,password));

    }
}