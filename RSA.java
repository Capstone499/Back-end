import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class RSA {
    private PublicKey publicKey;
    private static final String PUBLIC_KEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCI3B+PssYrepils+IU5YgKfsgrvhYr6CVSbbpxFyOrXobdusbkYI/SkQeHL8KDKwnySQXx+nX9zX5PGVbgrEymrrhSKjOhS8wm8XgH5/i2xrUnG00JcCQJCzYE6AeZsluxxE5yAGAfPM2MJjk2EwI1xY2NDxcLpXI/WnkjhFX3xwIDAQAB";

    public void init() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();
            publicKey = pair.getPublic();
        } catch (Exception ignored) {
        }
    }

    public void initFromStrings() {
        try {
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRING));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpecPublic);
        } catch (Exception ignored) {
        }
    }

    public String encrypt(String message) throws Exception {
        byte[] messageToBytes = message.getBytes();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageToBytes);
        return encode(encryptedBytes);
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    // Used for testing encryption and decryption. also for setting keys when
    // needed.
    public static void main(String[] args) {
        RSA rsa = new RSA();
        rsa.initFromStrings();
        try {
            String encryptedMessage = rsa.encrypt("Checking functionality\n");
            System.err.println("\nEncrypted: " + encryptedMessage);
        } catch (Exception ignored) {
        }
    }
}
