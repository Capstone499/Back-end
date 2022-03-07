import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class RSA {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private static final String PRIVATE_KEY_STRING = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIjcH4+yxit6mKWz4hTliAp+yCu+FivoJVJtunEXI6teht26xuRgj9KRB4cvwoMrCfJJBfH6df3Nfk8ZVuCsTKauuFIqM6FLzCbxeAfn+LbGtScbTQlwJAkLNgToB5myW7HETnIAYB88zYwmOTYTAjXFjY0PFwulcj9aeSOEVffHAgMBAAECgYAf2kOKOUyAEA94+W3T+Tv5XVqPO7WDUItnLNyot374wo5XCsKBoqu2kUSURRxbVOgCuNYmZGmTwYD1PeuHbPKFvYr+MmJEImezEMXmKqUdwGVemlvqBgN9L17HZeSICPp06sip4F2zPqs7DHB2zMDSK1EQJFS0bpin6kLsQlfzeQJBAMPBPyDlXNwsX/c6xLRGj8VPtjVe41JsNq+2uqwjn7Ha8XU6B5oqeKnBTuFAt0JIGwdJeHaPXMD6WQtl9kLpJyMCQQCy+sVn7H6Hj3BdxDyykzasN7Bg0+jkCoLW0texBjuq5f3VX0r62iLmp9g831UwO194Pa2fNmMxhTNQOAqkskkNAkAAsiowSsB2w+2famUSowGV2P+z1t+GBn53R3YIcKP7tOSQ3yDxyl7dc6N9J4a/RJRcBUXZXg8dXIZ+hOFIQZ3zAkEAkonTesUcy6zbWUpEUAlMKDDoTj7yXVNl0LGMO7pYvBHWhA6je0OCc8tUtnI8c2MJRY9qSgLjsDXYz4My46m9OQJAOI+Bp8J2FvOrd+tXqZlIJuDrO+P2I3wKQCzAyZk7GcgKOsKTVS39p00w9t1PmKlsEb7fu0PSU/g+AUVKPIeVgQ==";
    private static final String PUBLIC_KEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCI3B+PssYrepils+IU5YgKfsgrvhYr6CVSbbpxFyOrXobdusbkYI/SkQeHL8KDKwnySQXx+nX9zX5PGVbgrEymrrhSKjOhS8wm8XgH5/i2xrUnG00JcCQJCzYE6AeZsluxxE5yAGAfPM2MJjk2EwI1xY2NDxcLpXI/WnkjhFX3xwIDAQAB";

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
        try {
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRING));
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRING));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpecPublic);
            privateKey = keyFactory.generatePrivate(keySpecPrivate);
        } catch (Exception ignored) {
        }

    }

    public void printKeys() {
        System.err.println("\npublic key: " + encode(publicKey.getEncoded()) + "\n");
        System.err.println("private key: " + encode(privateKey.getEncoded()) + "\n");
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
        rsa.initFromStrings();
        try {
            String encryptedMessage = rsa.encrypt("Checking functionality\n");
            String dMessage = rsa.decrypt(encryptedMessage);

            System.err.println("\nEncrypted: " + encryptedMessage);
            System.err.println("\nDecrypted: " + dMessage);

            rsa.printKeys();
        } catch (Exception ignored) {
        }
    }
}
