// Standard use of SHA-1
public class Main {
    public static void main() {
        // ruleid: deprecated-sha-1
        MessageDigest sha1Digest = MessageDigest.getInstance("SHA1");
        // ruleid: deprecated-sha-1
        byte[] hashValue = DigestUtils.getSha1Digest().digest(password.getBytes());
        // ok: deprecated-sha-1
        MessageDigest sha256Digest = MessageDigest.getInstance("SHA256");
    }
}