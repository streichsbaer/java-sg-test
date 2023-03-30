import javax.servlet.http.HttpServletResponse;

public class Main {
    public static void main(HttpServletRequest request, HttpServletResponse response) {
        Connection conn = DriverManager.getConnection();
        // ruleid: taint-backend-connection-setschema-setcatalog
        conn.setCatalog(request.getParameter("catalog"));

        String vuln = request.getParameter("schema");
        // ruleid: taint-backend-connection-setschema-setcatalog
        conn.setSchema(vuln);
        
        String notvuln = "fixed value";
        // ok: taint-backend-connection-setschema-setcatalog
        conn.setSchema(notvuln);
    }
}