import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;


// Request parameter is first passed into another function
public class Main {
    public static void main(HttpServletRequest request, HttpServletResponse response) {
        String returnURL = request.getParameter("returnURL");
        String a = sanitize(returnURL);
        // ok: taint-backend-url-redirect-actionforward
        return new ActionForward(a);
    }
}

// Request parameter passed straight into function
public class Main {
    public static void main(HttpServletRequest request, HttpServletResponse response) {
        String returnURL = request.getParameter("returnURL");
        // ruleid: taint-backend-url-redirect-actionforward
        a = new ActionForward(returnURL);
        
        // ruleid: taint-backend-url-redirect-actionforward
        b = new ActionForward(returnURL, true);
        
        // ruleid: taint-backend-url-redirect-actionforward
        c = new ActionForward("success", returnURL, true);
        
        // wrong method signature
        // ok: taint-backend-url-redirect-actionforward
        d = new ActionForward("success", returnURL, true, false, false);
        
        // ok: taint-backend-url-redirect-actionforward
        e = new ActionForward("/static/url");
        // todoruleid: taint-backend-url-redirect-actionforward
        e.setPath(returnURL);

        // ruleid: taint-backend-url-redirect-actionforward
        return new ActionForward("success", returnURL, true, false);
    }
}