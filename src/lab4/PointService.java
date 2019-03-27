package lab4;

import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import java.util.List;
import  java.util.ArrayList;

@Stateful
public class PointService {
    private DataSource  ds;
    public PointService(){
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

    public void savePoint(Point point)
    {
        try {
            this.executeUpdate("insert into points(id, x, y, r, hit) " +
                    "values (NEXTVAL('point_seq'), " + point.getX()+ ", " +
                                                    point.getY()+ ", " +
                                                    point.getR()+ ", " +
                                                    point.isHit()+ ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Point> getAllPoints()
    {
        List<Point> resultList = new ArrayList<Point>();
        try {
            ResultSet rs = this.executeQuery("select * from points");

            while (rs.next() ) {
                Point point = new Point();
                point.setX(rs.getDouble("x"));
                point.setY(rs.getDouble("y"));
                point.setR(rs.getDouble("r"));
                point.setHit(rs.getBoolean("hit"));
                resultList.add(point);
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
