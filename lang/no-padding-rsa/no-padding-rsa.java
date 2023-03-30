// No padding on RSA
public class Main {
    public static void main() {
        // ruleid: no-padding-rsa
        Cipher c = Cipher.getInstance("RSA/NONE/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, k, iv);
        byte[] cipherText = c.doFinal(plainText);
    }
}

// OAEP padding on RSA
public class Main {
    public static void main() {
        // ok: no-padding-rsa
        Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithMD5AndMGF1Padding");
        c.init(Cipher.ENCRYPT_MODE, k, iv);
        byte[] cipherText = c.doFinal(plainText);
    }
}