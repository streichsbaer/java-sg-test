// Use of unencrypted socket
public class Main {
    private WEBPAGE = "www.google.com"
    private HTTP_PORT = 80
    public static void main() {
        // ruleid: unencrypted-socket
        Socket soc = new Socket(WEBPAGE, HTTP_PORT);
    }
}

// Use of encrypted socket
public class Main {
    private WEBPAGE = "www.google.com"
    private HTTPS_PORT = 443
    public static void main() {
        // ok: unencrypted-socket
        Socket soc = SSLSocketFactory.getDefault().createSocket(WEBPAGE, HTTPS_PORT);
    }
}