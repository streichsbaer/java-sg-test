// Use of DefaultHttpClient
public class Main {
    public static void main() {
        // ruleid: deprecated-defaulthttpclient
        HttpClient client = new DefaultHttpClient();
    }
}

// Use of SystemDefaultHttpClient
public class Main {
    public static void main() {
        // ok: deprecated-defaulthttpclient
        HttpClient client = new SystemDefaultHttpClient();
    }
}