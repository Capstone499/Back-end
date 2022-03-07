import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class AES1 {
    private SecretKey key;
    private byte[] IV;

    public void init() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        key = generator.generateKey();
    }

    public String encrypt(String message) throws Exception {
        byte[] messageInBytes = message.getBytes();
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
        IV = encryptionCipher.getIV();
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] messageInBytes = decode(encryptedMessage);
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, IV);
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    private void exportKeys() {
        System.err.println("Secret key: " + encode(key.getEncoded()));
        System.err.println("IV: " + IV);

    }

    public static void main(String[] args) {
        try {
            AES1 aes = new AES1();
            aes.init();
            String encryptedMessage = aes.encrypt("da baby");
            String decryptedMessage = aes.decrypt(encryptedMessage);

            System.err.println("\nEncrypted message: " + encryptedMessage);
            System.err.println("Decrypted Message: " + decryptedMessage + "\n");
            aes.exportKeys();
        } catch (Exception ignored) {
        }
    }
}
