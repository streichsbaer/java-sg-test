// Allow all origins
public class Main {
    public void main() {
        HttpServletResponse response = new HttpServletResponse();
        // ruleid: permissive-cors-policy
        response.addHeader("Access-CoNtrol-Allow-Origin", "*");
    }
}

// Allow specific origin
public class Main {
    public void main() {
        HttpServletResponse response = new HttpServletResponse();
        // ok: permissive-cors-policy
        response.addHeader("Access-ControL-Allow-Origin", "https://www.google.com");
    }
}