import javax.servlet.http.Cookie;

// No setting of HttpOnly
public class Main{
    public static void main() {
        Cookie cookie = new Cookie("email",userName);
        // ruleid: cookie-missing-httponly
        response.addCookie(cookie);
    }
}

// Setting of HttpOnly
public class Main{
    public static void main() {
        Cookie cookie = new Cookie("email",userName);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        // ok: cookie-missing-httponly
        response.addCookie(cookie);
    }
}