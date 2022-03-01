import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class RSA {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private static final String PRIVATE_KEY_STRING = " ";
    private static final String PUBLIC_KEY_STRING = " ";

    public void init() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception ignored) {
        }
    }

    public void initFromStrings() {
        X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRING));
    }

    public void printKeys() {
        System.err.println("public key: " + encode(publicKey.getEncoded())+"\n");
        System.err.println("private key: " + encode(privateKey.getEncoded())+"\n");
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

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage, "UTF8");
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    // Used for testing encryption and decryption. also for setting keys when
    // needed.
    public static void main(String[] args) {
        RSA rsa = new RSA();
        rsa.init();
        try {
            String encryptedMessage = rsa.encrypt("Checking functionality\n");
            String dMessage = rsa.decrypt(encryptedMessage);

            System.err.println("\nEncrypted: " + encryptedMessage);
            System.err.println("\nDecrypted: " + dMessage);

            rsa.printKeys();
        } catch (Exception ignored) {
        }    }
}
