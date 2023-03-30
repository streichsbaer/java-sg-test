import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Request parameter is first passed into another function
public class Main {
    public static void main(HttpServletRequest request, HttpServletResponse response) {
        String returnURL = request.getParameter("returnURL");
        String a = sanitize(returnURL);
        // ok: taint-backend-file-disclosure-requestdispatcher
        request.getRequestDispatcher(a).include(request, response);
        // ok: taint-backend-file-disclosure-requestdispatcher
        request.getRequestDispatcher("success").include(request, response);
    }

    // Request parameter passed straight into function
    public static void doGet(HttpServletRequest request, HttpServletResponse response) {
        String jspFile = request.getParameter("jspFile");
        // ruleid: taint-backend-file-disclosure-requestdispatcher
        request.getRequestDispatcher("/WEB-INF/jsps/" + jspFile + ".jsp").include(request, response);
        // ok: taint-backend-file-disclosure-requestdispatcher
        request.getRequestDispatcher("/pathtraver-00/BenchmarkTest00001.html");
    }
}