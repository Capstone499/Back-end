import java.net.*;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Server {
	// initialize socket and input stream
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream in = null;
	private String yabe = "cNY1I3M05D7jyjNCv2NdFQ==";
	private String eyeBee = "oLFQ3dPSDv02xD1S";
	private SecretKey key;
	private byte[] IV;
	private int current_mode = 0;

	public void initFromStringsAES() {
		key = new SecretKeySpec(decodeAES(yabe), "AES");
		this.IV = decodeAES(eyeBee);
	}

	public String decryptAES(String encryptedMessage) throws Exception {
		byte[] messageInBytes = decodeAES(encryptedMessage);
		Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
		GCMParameterSpec spec = new GCMParameterSpec(128, IV);
		decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
		byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
		return new String(decryptedBytes);
	}

	private byte[] decodeAES(String data) {
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
						UEline = "";
						current_mode = 0;
					}
					if (UEline.equals("turn off RSA")) {
						System.out.println("RSA encryption off!");
						// out.writeUTF("RSA encryption off!");
						UEline = "";
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
					// if (current_mode == 2) {
					// 	try {
					// 		RSA rsa = new RSA();
					// 		rsa.initFromStrings();
					// 		line = in.readUTF();
					// 		UEline = rsa.decrypt(line);
					// 		System.out.println("decrypted message: " + UEline);
					// 		if (UEline.equals("Over")) {
					// 			break;
					// 		}
					// 		System.out.println("encrypted message: " + line);
					// 	} catch (Exception ignored) {
					// 	}
					// } 
					else {
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
