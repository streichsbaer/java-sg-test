import javax.servlet.http.HttpServletResponse;

// No sanitization
public class Main {
    Logger log = new Logger();
    public static void main(HttpServletRequest request, HttpServletResponse response) {
        String val = request.getParameter("user");
        if(authenticated) {
            // ruleid: taint-backend-log-injection-logger
            log.info("User " + val + ") was authenticated successfully");
        }
        else {
            // ruleid: taint-backend-log-injection-logger
            log.info("User " + val + ") was not authenticated");
        }
    }
}

// With sanitization
public class Main {
    Logger log = new Logger();
    public static void main(HttpServletRequest request, HttpServletResponse response) {
        String val = request.getParameter("user");
        val = sanitize(val);
        if(authenticated) {
            // ok: taint-backend-log-injection-logger
            log.info("User " + val + ") was authenticated successfully");
        }
        else {
            // ok: taint-backend-log-injection-logger
            log.info("User " + val + ") was not authenticated");
        }
    }
}

// With sanitization by side effect
public class Main {
    Logger log = new Logger();
    public static void main(HttpServletRequest request, HttpServletResponse response) {
        String val = request.getParameter("user");
        sanitize(val);
        if(authenticated) {
            // ok: taint-backend-log-injection-logger
            log.info("User " + val + ") was authenticated successfully");
        }
        else {
            // ok: taint-backend-log-injection-logger
            log.info("User " + val + ") was not authenticated");
        }
    }
}