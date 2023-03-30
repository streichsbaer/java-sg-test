// Cipher modes with HMAC
public class Main {
    public static void main() {
        // ok: deprecated-cipher-mode
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
    }

    // Cipher modes without HMAC
    public static void main2() {
        // ruleid: deprecated-cipher-mode
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(javax.crypto.Cipher.ENCRYPT_MODE, publicKey);
        // ruleid: deprecated-cipher-mode
        Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    }
    public static Cipher getCipher() {
        if (cipher == null) {
            try {
                // ruleid: deprecated-cipher-mode
                cipher = javax.crypto.Cipher.getInstance(
                        "RSA/ECB/OAEPWithSHA-512AndMGF1Padding", "SunJCE");
                // Prepare the cipher to encrypt
                java.security.KeyPairGenerator keyGen =
                        java.security.KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(4096);
                java.security.PublicKey publicKey = keyGen.genKeyPair().getPublic();
                cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, publicKey);
            } catch (NoSuchAlgorithmException
                    | NoSuchProviderException
                    | NoSuchPaddingException
                    | InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return cipher;
    }
}