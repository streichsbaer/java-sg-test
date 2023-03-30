// Use of Null Cipher
public class Main {
    public static void main() {
        // ruleid: use-of-null-cipher
        Cipher c = new NullCipher();
        byte[] cipherText = c.doFinal(plainText);
    }
}

// Use of AES
public class Main {
    public static void main() {
        // ok: use-of-null-cipher
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, k, iv);
        byte[] cipherText = c.doFinal(plainText);
    }
}