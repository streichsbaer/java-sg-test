// Standard use of MD5
public class Main {
    public static void main() {
        // ruleid: deprecated-md5
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        // ruleid: deprecated-md5
        byte[] hashValue = DigestUtils.getMd5Digest().digest(password.getBytes());
        // ok: deprecated-md5
        MessageDigest sha256Digest = MessageDigest.getInstance("SHA256");
    }
}