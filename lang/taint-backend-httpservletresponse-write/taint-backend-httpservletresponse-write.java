import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// No sanitization or encoding
public class Main {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");
        // ruleid: taint-backend-httpservletresponse-write
        resp.getWriter().write(input1);
        PrintWriter writer = resp.getWriter();
        // ruleid: taint-backend-httpservletresponse-write
        writer.write(input1);

    }
}

public class Main {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");
        // ok: taint-backend-httpservletresponse-write
        resp.getWriter().write(Encode.forHtml(input1));

        bar = (7 * 42) - num > 200 ? "This should never happen" : input1;
        // todook: taint-backend-httpservletresponse-write
        resp.getWriter().write(bar.toCharArray());
    }
}