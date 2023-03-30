// Properly configured API
public class Main {
    public static void main() {
        // ok: insecure-esapi-config
        Encryptor.CipherText.useMAC=true;
        Encryptor.EncryptionAlgorithm=AES;
        // ok: insecure-esapi-config
        Encryptor.CipherTransformation=AES/GCM/NoPadding;
        // ok: insecure-esapi-config
        Encryptor.cipher_modes.additional_allowed=null;
    }
}

// Badly configured API
public class Main {
    public static void main() {
        // ruleid: insecure-esapi-config
        Encryptor.CipherText.useMAC=false;
        Encryptor.EncryptionAlgorithm=AES;
        // ruleid: insecure-esapi-config
        Encryptor.CipherTransformation=AES/CBC/PKCS5Padding;
        // ruleid: insecure-esapi-config
        Encryptor.cipher_modes.additional_allowed=CBC;
    }
}