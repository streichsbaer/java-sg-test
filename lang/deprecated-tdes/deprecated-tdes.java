// Use of Triple DES
public class Main {
    public static void main() {
        // ruleid: deprecated-tdes
        Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, k, iv);
        byte[] cipherText = c.doFinal(plainText);
    }
}

// Use of AES
public class Main {
    public static void main() {
        // ok: deprecated-tdes
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, k, iv);
        byte[] cipherText = c.doFinal(plainText);
    }
}