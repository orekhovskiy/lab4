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

            List<Point> list = (List<Point>) req.getSession().getAttribute("points");
            list.add(point);

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

    @HEAD
    @Path("/addpoint")
    public void addPoint(@QueryParam("X") double x, @QueryParam("Y") double y, @QueryParam("R") double r)
    {
        try
        {
            Point point = new Point(x, y, r);
            boolean hit = checkHit(point);
            point.setHit(hit);

            service.savePoint(point);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
