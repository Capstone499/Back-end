import java.net.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

// public class 

// public void handleRequest (Socket socket) public {
//     try (BufferedReader in = new BufferedReader (new InputStreamReader (socket.getInputStream()));){
//             // string headerline = in.readLine();
//             // new StringTokenizer(headerLine);
//             // String httpMethod = tokenizer.nextToken();
//     } 
//     catch (Exception e) {
//         e.printStackTrace();
//     }
// }

public class Client {
	// initialize socket and input output streams
	private Socket socket = null;
	private DataInputStream input = null;
	private DataOutputStream out = null;
	private String yabe = "cNY1I3M05D7jyjNCv2NdFQ==";
	private String eyeBee = "oLFQ3dPSDv02xD1S";
	private static final String PUBLIC_KEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCI3B+PssYrepils+IU5YgKfsgrvhYr6CVSbbpxFyOrXobdusbkYI/SkQeHL8KDKwnySQXx+nX9zX5PGVbgrEymrrhSKjOhS8wm8XgH5/i2xrUnG00JcCQJCzYE6AeZsluxxE5yAGAfPM2MJjk2EwI1xY2NDxcLpXI/WnkjhFX3xwIDAQAB";
	private SecretKey key;
	private PublicKey publicKey;
	private byte[] IV;

	private int current_mode = 0;

	public void initPublicKeyRSA() {
		try {
			X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRING));

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			publicKey = keyFactory.generatePublic(keySpecPublic);
		} catch (Exception ignored) {
		}
	}

	public void initFromStringsAES() {
		key = new SecretKeySpec(decode(yabe), "AES");
		this.IV = decode(eyeBee);
	}

	public String encryptRSA(String message) throws Exception {
		byte[] messageToBytes = message.getBytes();
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedBytes = cipher.doFinal(messageToBytes);
		return encode(encryptedBytes);
	}

	public String encryptAES(String message) throws Exception {
		byte[] messageInBytes = message.getBytes();
		Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec spec = new GCMParameterSpec(128, IV);
		encryptionCipher.init(Cipher.ENCRYPT_MODE, key, spec);
		byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
		return encode(encryptedBytes);
	}

	private String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	private byte[] decode(String data) {
		return Base64.getDecoder().decode(data);
	}

	// constructor to put ip address and port
	public Client(String address, int port) {
		// establish a connection
		try {
			socket = new Socket(address, port);
			System.out.println("Connected");

			// takes input from terminal
			input = new DataInputStream(System.in);

			// sends output to the socket
			out = new DataOutputStream(socket.getOutputStream());
		}
		// error handling
		catch (UnknownHostException u) {
			System.out.println(u);
		} catch (IOException i) {
			System.out.println(i);
		}

		// string to read message from input
		String line = "";

		// keep reading until "Over" is input
		while (!line.equals("Over")) {
			try {
				System.out.println(current_mode);
				if (line.equals("AES")) {
					current_mode = 1;
					// serverLines = serverinput.readUTF();
					// System.err.println(serverLines);
				}
				if (line.equals("RSA")) {
					current_mode = 2;
					// serverLines = serverinput.readUTF();
					// System.err.println(serverLines);
				}
				if (line.equals("turn off AES")) {
					current_mode = 0;
				}
				if (line.equals("turn off RSA")) {
					current_mode = 0;
				}
				if (current_mode == 1) {
					try {
						initFromStringsAES();
						line = input.readLine();
						out.writeUTF(encryptAES(line));
					} catch (Exception ignored) {
					}
				}
				if (current_mode == 2) {
					try {
						initPublicKeyRSA();
						line = input.readLine();
						out.writeUTF(encryptRSA(line));
					} catch (Exception ignored) {
					}
				} else {
					line = input.readLine();
					out.writeUTF(line);
				}

			} catch (IOException i) {
				System.out.println(i);
			}
		}

		// close the connection
		try {
			input.close();
			out.close();
			socket.close();
		} catch (IOException i) {
			System.out.println(i);
		}
	}

	public static void main(String args[]) // main function
	{
		Client client = new Client("127.0.0.1", 5000); // define ip address and port number
	}
}
