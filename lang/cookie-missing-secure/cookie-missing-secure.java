import javax.servlet.http.Cookie;

// No setting of secure flag
public class Main{
    public static void main() {
        Cookie cookie = new Cookie("userName",userName);
        // ruleid: cookie-missing-secure
        response.addCookie(cookie);
    }
}

// Setting of secure flag
public class Main{
    public static void main() {
        Cookie cookie = new Cookie("email",userName);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        // ok: cookie-missing-secure
        response.addCookie(cookie);
    }
}