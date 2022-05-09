import java.io.*;
import java.net.*;
import java.util.*;

public class BetaClient {
	
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

			// recs for encryption/message sending
			String line = null;
			int faze = 0;

			while (!"authorized".equalsIgnoreCase(line)) {
				System.out.println("Enter username: ");
				line = sc.nextLine();
				out.println(line);
				System.out.println("Enter password: ");
				line = sc.nextLine();
				out.println(line);
				line = in.readLine();
			}

			while (!"exit".equalsIgnoreCase(line)) {

				// reading from user
				line = sc.nextLine();

				// AES Mode
				if (faze == 1) {
					try {
						out.println(aes.encrypt(line));
						out.flush(); // clear the stream of any element

						// displaying server reply
						System.out.println("Server replied (AES): "
								+ in.readLine());
					} catch (Exception ignored) {
					}
				}
				// RSA Mode
				if (faze == 2) {
					try {
						out.println(rsa.encrypt(line));
						out.flush(); // clear the stream of any element

						// displaying server reply
						System.out.println("Server replied (RSA): "
								+ in.readLine());
					} catch (Exception ignored) {
					}

				}
				if (faze == 0) {
					// sending the user input to server
					// displaying server reply
					out.println(line);
					out.flush(); // clear the stream of any element

					System.out.println("Server replied "
							+ in.readLine());
				}

				if ("aes on".equalsIgnoreCase(line)) {
					faze++;
				}
				if ("rsa on".equalsIgnoreCase(line)) {
					faze = 2;
				}
				if ("ec off".equalsIgnoreCase(line)) {
					faze = 0;
				}
			}

			// closing the scanner object
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
