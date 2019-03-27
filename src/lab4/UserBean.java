package lab4;

import javax.ejb.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import javax.servlet.http.Cookie;


@Singleton
@Path(value = "/user")
public class UserBean{
    @EJB
    private UserService service = new UserService();

    public UserBean()
    {
    }


    private boolean validateUser(String login, String password){
        if (login.length() < 5 || login.length() > 20) return false;
        if (password.length() < 5 || password.length() > 20) return false;
        return true;
    }

    @Path("/signup")
    @POST
    public void newUser(@FormParam("login") String login,
                        @FormParam("password") String password,
                        @Context HttpServletResponse resp,
                        @Context HttpServletRequest req) {
        try
        {
            User user = new User(login, password);
            if (validateUser(login, password) && service.checkLogin(login))
            {
                service.saveUser(user);
                req.getSession().setAttribute("login", login);
                req.getSession().setAttribute("points", new ArrayList<Point>());
                resp.sendRedirect("http://localhost:4200/main?login=" + login +"&password=" + password);
            }
            else
            {
                resp.sendRedirect("http://localhost:4200/signup");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/login")
    public void checkAuth(@FormParam("login") String login,
                          @FormParam("password") String password,
                          @Context HttpServletResponse resp,
                          @Context HttpServletRequest req) {
        try {
            if (validateUser(login, password) && service.checkUser(login, password)) {
                req.getSession().setAttribute("login", login);
                req.getSession().setAttribute("points", new ArrayList<Point>());
                /*esp.addHeader("Set-Cookie", "login=" + login);
                Cookie myCookie = new Cookie("login", login);
                resp.addCookie(myCookie);*/
                resp.sendRedirect("http://localhost:4200/main?login=" + login +"&password=" + password);
            } else {
                resp.sendRedirect("http://localhost:4200/login");
            }
        } catch (Exception e) {
            System.out.println("Login error! (or data is incorrect)");
            e.printStackTrace();
        }
    }

    @POST
    @Path("/logout")
    public void logOut(@Context HttpServletRequest req,
                       @Context HttpServletResponse resp) {
        try {
            req.getSession().invalidate();
            resp.sendRedirect("http://localhost:4200/login");
        } catch (Exception e) { e.printStackTrace(); }
    }
    @GET
    @Path("/checkUser")
    public Response checkUser(@QueryParam("login") String login, @QueryParam("password") String password, @Context HttpServletRequest req)
    {
            if (!service.checkUser(login, password)) {
                return  Response.status(Response.Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
            }
            else {
                return Response.ok().header("Access-Control-Allow-Origin", "*").build();
            }
    }
}
