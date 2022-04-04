import java.net.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

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
	private SecretKey key;
	private byte[] IV;
	private int current_mode = 0;

	public void initFromStringsAES() {
		key = new SecretKeySpec(decodeAES(yabe), "AES");
		this.IV = decodeAES(eyeBee);
	}

	public String encryptAES(String message) throws Exception {
		byte[] messageInBytes = message.getBytes();
		Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec spec = new GCMParameterSpec(128, IV);
		encryptionCipher.init(Cipher.ENCRYPT_MODE, key, spec);
		byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
		return encodeAES(encryptedBytes);
	}

	private String encodeAES(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	private byte[] decodeAES(String data) {
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
				// if (current_mode == 2) {
				// try {
				// RSA rsa = new RSA();
				// rsa.initFromStrings();
				// line = input.readLine();
				// out.writeUTF(rsa.encrypt(line));
				// } catch (Exception ignored) {
				// }
				// }
				else {
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
