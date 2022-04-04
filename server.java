import java.net.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Server {
	// initialize socket and input stream
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream in = null;
	private String yabe = "cNY1I3M05D7jyjNCv2NdFQ==";
	private String eyeBee = "oLFQ3dPSDv02xD1S";
	private static final String PRIVATE_KEY_STRING = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIjcH4+yxit6mKWz4hTliAp+yCu+FivoJVJtunEXI6teht26xuRgj9KRB4cvwoMrCfJJBfH6df3Nfk8ZVuCsTKauuFIqM6FLzCbxeAfn+LbGtScbTQlwJAkLNgToB5myW7HETnIAYB88zYwmOTYTAjXFjY0PFwulcj9aeSOEVffHAgMBAAECgYAf2kOKOUyAEA94+W3T+Tv5XVqPO7WDUItnLNyot374wo5XCsKBoqu2kUSURRxbVOgCuNYmZGmTwYD1PeuHbPKFvYr+MmJEImezEMXmKqUdwGVemlvqBgN9L17HZeSICPp06sip4F2zPqs7DHB2zMDSK1EQJFS0bpin6kLsQlfzeQJBAMPBPyDlXNwsX/c6xLRGj8VPtjVe41JsNq+2uqwjn7Ha8XU6B5oqeKnBTuFAt0JIGwdJeHaPXMD6WQtl9kLpJyMCQQCy+sVn7H6Hj3BdxDyykzasN7Bg0+jkCoLW0texBjuq5f3VX0r62iLmp9g831UwO194Pa2fNmMxhTNQOAqkskkNAkAAsiowSsB2w+2famUSowGV2P+z1t+GBn53R3YIcKP7tOSQ3yDxyl7dc6N9J4a/RJRcBUXZXg8dXIZ+hOFIQZ3zAkEAkonTesUcy6zbWUpEUAlMKDDoTj7yXVNl0LGMO7pYvBHWhA6je0OCc8tUtnI8c2MJRY9qSgLjsDXYz4My46m9OQJAOI+Bp8J2FvOrd+tXqZlIJuDrO+P2I3wKQCzAyZk7GcgKOsKTVS39p00w9t1PmKlsEb7fu0PSU/g+AUVKPIeVgQ==";
	private SecretKey key;
	private PrivateKey privateKey;
	private byte[] IV;
	private int current_mode = 0;

	public void initFromStringsAES() {
		key = new SecretKeySpec(decode(yabe), "AES");
		this.IV = decode(eyeBee);
	}

	public void initPrivateKeyRSA() {
		try {
			PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRING));

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			privateKey = keyFactory.generatePrivate(keySpecPrivate);
		} catch (Exception ignored) {
		}

	}

	public String decryptAES(String encryptedMessage) throws Exception {
		byte[] messageInBytes = decode(encryptedMessage);
		Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec spec = new GCMParameterSpec(128, IV);
		decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
		byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
		return new String(decryptedBytes);
	}

	public String decryptRSA(String encryptedMessage) throws Exception {
		byte[] encryptedBytes = decode(encryptedMessage);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
		return new String(decryptedMessage, "UTF8");
	}

	private byte[] decode(String data) {
		return Base64.getDecoder().decode(data);
	}

	// constructor with port
	public Server(int port) {
		// starts server and waits for a connection
		try {
			server = new ServerSocket(port);
			System.out.println("Server started");

			System.out.println("Waiting for a client ...");

			socket = server.accept();
			System.out.println("Client accepted");

			// takes input from the client socket
			in = new DataInputStream(
					new BufferedInputStream(socket.getInputStream()));

			String line = "";
			String UEline = "";

			// // old method of reading inputs/outputs
			// while (!line.equals("Over")||!UEline.equals("Over")) {
			// try {
			// if(line.equals("AES1"))
			// {
			// System.out.println("AES encryption on!")
			// out.writeUTF("AES encryption on!")
			// current_mode = 1;
			// }
			// if(line.equals("turn off AES1"))
			// {
			// System.out.println("AES encryption off!")
			// out.writeUTF("AES encryption off!")
			// }
			// if(current_mode == 1)
			// {
			// line = in.readUTF();
			// System.out.println("encrypted message: "+line);
			// UEline = aes.decrypt(line);
			// System.out.println(UEline);
			// }
			// else{
			// line = in.readUTF();
			// System.out.println(line);
			// }

			// } catch (IOException i) {
			// System.out.println(i);
			// }
			// }

			// reads message from client until "Over" is sent (testing do while)

			while (!line.equals("Over") || !UEline.equals("Over")) {
				try {
					System.out.println(current_mode);
					if (line.equals("AES")) {
						System.out.println("AES encryption on!");
						// out.writeUTF("AES encryption on!");
						current_mode = 1;
					}
					if (line.equals("RSA")) {
						System.out.println("RSA encryption on!");
						// out.writeUTF("AES encryption on!");
						current_mode = 2;
					}
					if (UEline.equals("turn off AES")) {
						System.out.println("AES encryption off!");
						// out.writeUTF("AES encryption off!");
						UEline = " ";
						current_mode = 0;
					}
					if (UEline.equals("turn off RSA")) {
						System.out.println("RSA encryption off!");
						// out.writeUTF("RSA encryption off!");
						UEline = " ";
						current_mode = 0;
					}
					if (current_mode == 1) {
						try {
							initFromStringsAES();
							line = in.readUTF();
							UEline = decryptAES(line);
							System.out.println("decrypted message: " + UEline);
							if (UEline.equals("Over")) {
								break;
							}
							System.out.println("encrypted message: " + line);
						} catch (Exception ignored) {
						}
					}
					if (current_mode == 2) {
						try {
							initPrivateKeyRSA();
							line = in.readUTF();
							UEline = decryptRSA(line);
							System.out.println("decrypted message: " + UEline);
							if (UEline.equals("Over")) {
								break;
							}
							System.out.println("encrypted message: " + line);
						} catch (Exception ignored) {
						}
					} else {
						line = in.readUTF();
						System.out.println(line);
					}

				} catch (IOException i) {
					System.out.println(i); // without break runs infinitely. debugg plz
					break;
				}
			}

			System.out.println("Closing connection");

			// close connection
			socket.close();
			in.close();
		} catch (IOException i) {
			System.out.println(i);
		}
	}

	public static void main(String args[]) {
		Server server = new Server(5000);
		System.out.println(server);
	}
}

class ClientHandler extends Thread {
	Socket socket;
	DataInputStream in;
	DataOutputStream out;
	String strrecieved = "", strsent = "", name;

	ClientHandler(Socket socket, String name, DataInputStream in, DataOutputStream out) {
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.name = name;
	}

	public void run() {
		try {
			while (true) {
				out.writeUTF("What is your name? Written already write Exit.");
				strrecieved = in.readUTF();
				if (strrecieved.equals("Exit")) {
					System.out.println("Client " + this.socket + " EXITED");
					this.socket.close();
					this.in.close();
					this.out.close();
					break;
				}
				System.out.println("Client Messaged " + strrecieved);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
