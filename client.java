import java.net.*;
import java.io.*;

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
	private int current_mode = 0;

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
				if (line.equals("AES1")) {
					current_mode++;
					// serverLines = serverinput.readUTF();
					// System.err.println(serverLines);
				}
				if (line.equals("turn off AES1")) {
					current_mode--;
				}
				if (current_mode == 1) {
					try {
						AES1 aes = new AES1();
						aes.initFromStrings();
						line = input.readLine();
						out.writeUTF(aes.encrypt(line));
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
