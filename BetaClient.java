import java.io.*;
import java.net.*;
import java.util.*;
// import javax.crypto.Cipher;
// import javax.crypto.SecretKey;
// import javax.crypto.spec.GCMParameterSpec;
// import javax.crypto.spec.SecretKeySpec;
// import java.util.Base64;
// import java.security.KeyFactory;
// import java.security.PublicKey;
// import java.security.spec.X509EncodedKeySpec;

public class BetaClient {
	// private String yabe = "cNY1I3M05D7jyjNCv2NdFQ==";
	// private String eyeBee = "oLFQ3dPSDv02xD1S";
	// private static final String PUBLIC_KEY_STRING =
	// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCI3B+PssYrepils+IU5YgKfsgrvhYr6CVSbbpxFyOrXobdusbkYI/SkQeHL8KDKwnySQXx+nX9zX5PGVbgrEymrrhSKjOhS8wm8XgH5/i2xrUnG00JcCQJCzYE6AeZsluxxE5yAGAfPM2MJjk2EwI1xY2NDxcLpXI/WnkjhFX3xwIDAQAB";
	// private SecretKey key;
	// private PublicKey publicKey;
	// private byte[] IV;
	// private int current_mode = 0;

	// public void initPublicKeyRSA() {
	// try {
	// X509EncodedKeySpec keySpecPublic = new
	// X509EncodedKeySpec(decode(PUBLIC_KEY_STRING));

	// KeyFactory keyFactory = KeyFactory.getInstance("RSA");

	// publicKey = keyFactory.generatePublic(keySpecPublic);
	// } catch (Exception ignored) {
	// }
	// }

	// public void initFromStringsAES() {
	// key = new SecretKeySpec(decode(yabe), "AES");
	// this.IV = decode(eyeBee);
	// }

	// public String encryptRSA(String message) throws Exception {
	// byte[] messageToBytes = message.getBytes();
	// Cipher cipher = Cipher.getInstance("RSA");
	// cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	// byte[] encryptedBytes = cipher.doFinal(messageToBytes);
	// return encode(encryptedBytes);
	// }

	// public String encryptAES(String message) throws Exception {
	// byte[] messageInBytes = message.getBytes();
	// Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
	// GCMParameterSpec spec = new GCMParameterSpec(128, IV);
	// encryptionCipher.init(Cipher.ENCRYPT_MODE, key, spec);
	// byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
	// return encode(encryptedBytes);
	// }

	// private String encode(byte[] data) {
	// return Base64.getEncoder().encodeToString(data);
	// }

	// private byte[] decode(String data) {
	// return Base64.getDecoder().decode(data);
	// }

	// driver code
	public static void main(String[] args) {
		RSA rsa = new RSA();
		AES aes = new AES();
		aes.initFromStrings();
		rsa.initFromStrings();
		// establish a connection by providing host and port
		// number
		try (Socket socket = new Socket("localhost", 1234)) {

			// writing to server
			PrintWriter out = new PrintWriter( // converts characters into bytes
					socket.getOutputStream(), true);

			// reading from server
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			// object of scanner class
			Scanner sc = new Scanner(System.in); // converts bytes into characters
			String line = null;

			while (!"exit".equalsIgnoreCase(line)) {

				// reading from user
				line = sc.nextLine();

				// sending the user input to server
				out.println(line);
				out.flush(); // clear the stream of any element

				// displaying server reply
				System.out.println("Server replied "
						+ in.readLine());

				// AES Mode
				try {
					out.println(aes.encrypt(line));
					out.flush(); // clear the stream of any element

					// displaying server reply
					System.out.println("Server replied (AES): "
							+ in.readLine());
				} catch (Exception ignored) {
				}

				// RSA Mode
				try {
					out.println(rsa.encrypt(line));
					out.flush(); // clear the stream of any element

					// displaying server reply
					System.out.println("Server replied (RSA): "
							+ in.readLine());
				} catch (Exception ignored) {
				}
			}

			// closing the scanner object
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
