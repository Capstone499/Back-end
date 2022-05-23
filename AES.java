import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// Encryption method AES is implemented in this file as a public class
public class AES {
    private String one = "cNY1I3M05D7jyjNCv2NdFQ==";
    private String two = "oLFQ3dPSDv02xD1S";
    private SecretKey key;
    private byte[] IV;

    // This generates a secret key for AES; the keysize is 128 bytes
    public void init() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        key = generator.generateKey();
    }

    // This function returns a secret key from the recieved byte array.
    public void initFromStrings() {
        key = new SecretKeySpec(decode(one), "AES");
        this.IV = decode(two);
    }

    // This function returns an encrypted string from given string input
    public String encrypt(String message) throws Exception {
        byte[] messageInBytes = message.getBytes();
        // Store the transformed "data" into a Cipher class object
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, IV);
        // AES uses a a key and a set of algorithm parameters ^
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }

    //  This function returns an encoded string from bytes
    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    // This function returns a decrypted string from given string input
    public String decrypt(String encryptedMessage) throws Exception {
        byte[] messageInBytes = decode(encryptedMessage);
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, IV);
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes);
    }

    // This returns a new byte array after decoding a Base64 encoded string
    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    // This function prints the encoded SecretKey and byte array
    private void exportKeys() {
        System.err.println("SecretKey : " + encode(key.getEncoded()));
        System.err.println("IV : " + encode(IV));
    }

    // Used for testing, encryption, decryption, and is used to set the keys too
    public static void main(String[] args) {
        try {
            AES aes = new AES();
            aes.initFromStrings();
            String encryptedMessage = aes.encrypt("checking functionality");
            String decryptedMessage = aes.decrypt(encryptedMessage);

            System.err.println("\nEncrypted message: " + encryptedMessage);
            System.err.println("Decrypted Message: " + decryptedMessage + "\n");
            aes.exportKeys();
        } catch (Exception ignored) {
        }
    }
}
