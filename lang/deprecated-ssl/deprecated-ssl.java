import javax.net.ssl.SSLContext;

// Use of SSL
public class Main {
    public static void main_insecure_1() {
        // ruleid: deprecated-ssl
        SSLContext sslContext = SSLContext.getInstance("SSL");
    }

    public static void main_insecure_2() {
        // https://stackoverflow.com/questions/29841610/will-sslcontext-getinstancetls-supports-tls-v1-1-and-tls-v1-2-also
        // ruleid: deprecated-ssl
        SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
    }

    public static void main_insecure_3() {
        SSLContext sslContext = new SSLContext();
        // ruleid: deprecated-ssl
        sslContext = sslContext.getInstance("TLS");
    }
    public static void main_secure() {
        // ok: deprecated-ssl
        SSLContext sslContext = new SSLContext();
        sslContext = sslContext.getInstance("TLS1.2");
    }
}