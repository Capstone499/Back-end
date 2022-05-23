import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

	// driver code
	public static void main(String[] args) {
		RSA rsa = new RSA();
		AES aes = new AES();
		aes.initFromStrings();
		rsa.initFromStrings();
		int current_mode = 0;
		int attempt_counter = 0;
		// establish a connection by providing host and port
		// number
		try (Socket socket = new Socket("localhost", 25565)) {

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
			String read_out_loud = "";

			while (!"authorized".equalsIgnoreCase(line)) {
				System.out.println("Enter username: ");
				line = sc.nextLine();
				try {
					out.println(rsa.encrypt(line));
				} catch (Exception ignored) {
				}
				System.out.println("Enter password: ");
				line = sc.nextLine();
				try {
					out.println(rsa.encrypt(line));
				} catch (Exception ignored) {
				}
				line = in.readLine();
				attempt_counter++;
				if (attempt_counter > 4) {
					break;
				}
				System.out.print("Login attempt #" + attempt_counter + " " + line + "\n");
			}
			if (attempt_counter > 5) {
				line = "exit";
				System.out.print("Login failed. Closing program");

			} else {
				read_out_loud = in.readLine();
				if (!"authorized".equalsIgnoreCase(read_out_loud)) {
					System.out.println("Welcome Back! "
							+ read_out_loud);
					System.out.println("Remember by default encryption is off!");
					System.out.println("To turn on AES encryption simply type: aes on");
					System.out.println("To turn on RSA encryption simply type: rsa on");
					System.out.println("To turn off encryption simply type: ec off");
					System.out.println("Exiting is as simple as typing: exit (while encryption is off)");
					System.out.println(
							"Note: to swap encryptions you must first turn off encryption and then type in the encryption level you want!");

				} else {
					System.out.println("Welcome Back! "
							+ in.readLine());
					System.out.println("Remember by default encryption is off!");
					System.out.println("To turn on AES encryption simply type: aes on");
					System.out.println("To turn on RSA encryption simply type: rsa on");
					System.out.println("To turn off encryption simply type: ec off");
					System.out.println("Exiting is as simple as typing: exit (while encryption is off)");
					System.out.println(
							"Note: to swap encryptions you must first turn off encryption and then type in the encryption level you want!");

				}
			}
			while (!"exit".equalsIgnoreCase(line)) {

				// reading from user
				line = sc.nextLine();

				// AES Mode
				if (current_mode == 1) {
					try {
						out.println(aes.encrypt(line));
						out.flush(); // clear the stream of any element

						// displaying server reply
						System.out.println(" Server replied (AES): "
								+ in.readLine());
					} catch (Exception ignored) {
					}
				}
				// RSA Mode
				if (current_mode == 2) {
					try {
						out.println(rsa.encrypt(line));
						out.flush(); // clear the stream of any element

						// displaying server reply
						System.out.println(" Server replied (RSA): "
								+ in.readLine());
					} catch (Exception ignored) {
					}

				}
				if (current_mode == 0) {
					// sending the user input to server
					// displaying server reply
					out.println(line);
					out.flush(); // clear the stream of any element

					System.out.println(" Server replied "
							+ in.readLine());
				}

				if ("aes on".equalsIgnoreCase(line)) {
					current_mode++;
					System.out.print("AES encryption has been turned on!\n");
				}
				if ("rsa on".equalsIgnoreCase(line)) {
					current_mode = 2;
					System.out.print("RSA encryption has been turned on!\n");
				}
				if ("ec off".equalsIgnoreCase(line)) {
					current_mode = 0;
					System.out.print("Encryption has been turned off!\n");
				}
			}
			// closing the scanner object
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (attempt_counter < 6) {
			System.out.print("Exited Successfuly");
		}
	}
}
