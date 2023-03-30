// Usage of Integer.toHexString on a hash
public class Main {
    public static void main() {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] resultBytes = md.digest(password.getBytes("UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        for(byte b :resultBytes) {
            // ruleid: insecure-hexadecimal-conversion
            stringBuilder.append( Integer.toHexString( b & 0xFF ) );
        }
    }
}

// Usage of String.format on a hash
public class Main {
    public static void main() {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] resultBytes = md.digest(password.getBytes("UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        for(byte b :resultBytes) {
            // ok: insecure-hexadecimal-conversion
            stringBuilder.append( String.format( "%02X", b & 0xFF ) );
        }
    }
}