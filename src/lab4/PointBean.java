package lab4;

import java.util.List;

import javax.ejb.*;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Stateful
@SessionScoped
@Path("/point")
public class PointBean
{
    @EJB
    private PointService service = new PointService();

    private boolean checkHit(Point p)
    {
        double  x = p.getX();
        double  y = p.getY();
        double  r = p.getR();
        if (x >= 0 && y <= 0 && -y <= r / 2 && x <= r) return true;
        if (x <=0 && y <= 0 && x*x + y*y <= r*r / 4) return true;
        if (x <= 0 && y >=0 && -x + y <= r) return true;
        return false;
    }

    @POST
    @Path("/check")
    public void check(@FormParam("X") double x, @FormParam("Y") double y, @FormParam("R") double r,
                      @Context HttpServletRequest req, @Context HttpServletResponse resp)
    {
        try
        {
            Point point = new Point(x, y, r);
            boolean hit = checkHit(point);
            point.setHit(hit);

            service.savePoint(point);

            resp.sendRedirect("http://localhost:4200/main");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/getpoints")
    public Response getPoints(@Context HttpServletRequest req)
    {
            return Response.ok(service.getAllPoints()).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("/addpoint")
    public Response addPoint(@QueryParam("x") double x, @QueryParam("y") double y, @QueryParam("r") double r)
    {
        try
        {
            Point point = new Point(x, y, r);
            boolean hit = checkHit(point);
            point.setHit(hit);

            service.savePoint(point);
            return Response.ok().header("Access-Control-Allow-Origin", "*").build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").build();
    }
}
