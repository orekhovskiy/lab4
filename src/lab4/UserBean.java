package lab4;

import javax.ejb.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


@Singleton
@Path(value = "/user")
public class UserBean{
    @EJB
    private UserService service = new UserService();

    public UserBean() {}

    private boolean validateUser(String login, String password){
        if (login.length() < 5 || login.length() > 20) return false;
        if (password.length() < 5 || password.length() > 20) return false;
        return true;
    }

    @GET
    @Path("/signup")
    public Response newUser(@QueryParam("login") String login,
                        @QueryParam("password") String password,
                        @Context HttpServletResponse resp,
                        @Context HttpServletRequest req) {
        try
        {
            User user = new User(login, password);
            if (validateUser(login, password) && service.checkLogin(login))
            {
                service.saveUser(user);
                return Response.ok().header("Access-Control-Allow-Origin", "*").build();
            }
            else
            {
                return  Response.status(Response.Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  Response.status(Response.Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/login")
    public Response checkAuth(@QueryParam("login") String login,
                          @QueryParam("password") String password,
                          @Context HttpServletResponse resp,
                          @Context HttpServletRequest req) {
        try {
            if (validateUser(login, password) && service.checkUser(login, password)) {
                return Response.ok().header("Access-Control-Allow-Origin", "*").build();
            } else {
                return  Response.status(Response.Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
            }
        } catch (Exception e) {
            System.out.println("Login error! (or data is incorrect)");
            e.printStackTrace();
        }
        return  Response.status(Response.Status.FORBIDDEN).header("Access-Control-Allow-Origin", "*").build();
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
}
