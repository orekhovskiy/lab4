package lab4;
public class User {

    private int id;
    private String login;
    private int password;

    public User(){}

    public User(String login, String password){
        this.login = login;
        this.password = password.hashCode();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password.hashCode();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
