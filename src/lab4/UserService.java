package lab4;

import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

@Stateful
public class UserService {
    private DataSource  ds;
    public UserService(){
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("jdbc/lab4");
        }
        catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private void executeUpdate(String query) throws SQLException{
        java.sql.Connection con = null;
        try {
            con = ds.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);

        } finally {
            if (con != null) con.close();
        }
    }
    private  ResultSet executeQuery (String query) throws SQLException{
        java.sql.Connection con = null;
        try {
            con = ds.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } finally {
            if (con != null) con.close();
        }
    }

    public void saveUser (User user){
        try {
            this.executeUpdate("insert into users(id, login, password) " +
                    "values (NEXTVAL('user_seq'), '" + user.getLogin()+ "', " + user.getPassword() +")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean checkUser(String login, String password){
        try
        {
            int pass;
            ResultSet rs = executeQuery("select * from users where login='" + login + "'");
            if (rs.next()) {
                pass = rs.getInt("password");
                if(pass == password.hashCode()) return true;
                if (rs.wasNull()) {
                    return false;
                }
            }
            return false;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean checkLogin(String login){
        try
        {
            ResultSet rs = executeQuery("select * from users where login='" + login + "'");
            if (rs.next()) {
                if (rs.wasNull()) {
                    return true;
                }
                return false;
            }
            return true;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

}
