import java.io.*;
import java.net.*;

class BetaServer {

	public static void main(String[] args) {
		ServerSocket server = null;
		// starting up encryption things for later

		try {

			// server is listening on port 1234
			server = new ServerSocket(1234);
			server.setReuseAddress(true);

			// running infinite loop for getting
			// client request
			while (true) {

				// socket object to receive incoming client
				// requests
				Socket client = server.accept();

				// Displaying that new client is connected
				// to server
				System.out.println("New client connected "
						+ client.getInetAddress()
								.getHostAddress());

				// create a new thread object
				ClientHandler clientSock = new ClientHandler(client);

				// This thread will handle the client
				// separately
				new Thread(clientSock).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ClientHandler class
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;

		// Constructor
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		public Boolean Authorization(String username, String password) {
			if ("user".equalsIgnoreCase(username) && "pass".equalsIgnoreCase(password)) {
				return true;
			}
			return false;
		}

		public void run() {
			PrintWriter out = null;
			BufferedReader in = null;
			RSA rsa = new RSA();
			AES aes = new AES();
			aes.initFromStrings();
			rsa.initFromStrings();
			try {

				// get the outputstream of client
				out = new PrintWriter( // converts characters into bytes
						clientSocket.getOutputStream(), true);

				// get the inputstream of client
				in = new BufferedReader(
						new InputStreamReader(
								clientSocket.getInputStream()));

				String line;
				String dc_line = null;
				String user = null;
				String pass = null;
				int attempt = 0;
				int phase = 0;

				while (Authorization(user, pass) != true) {
					try {
						user = rsa.decrypt(in.readLine());
						pass = rsa.decrypt(in.readLine());
					} catch (Exception ignored) {
					}
					System.out.println("attempted username: " + user);
					System.out.println("attempted password: " + pass);
					attempt++;
					if (Authorization(user, pass) != false) {
						out.println("authorized");
						System.out.println(user + " Successfully Logged In");
					} else {
						System.out.println("Login Attempt # " + attempt);
						out.println("fail");
					}
				}

				out.println(user);

				while ((line = in.readLine()) != null) {

					// writing the received message from
					// client
					if (phase == 0) {
						System.out.printf(" Sent from the client: %s\n", line);
						out.println(line);
					}
					if (phase == 1) {
						System.out.printf(" Sent from the client (AES): %s\n", line);
						try {
							System.out.printf(" Sent from the client (decrypted): %s\n", aes.decrypt(line));
							dc_line = aes.decrypt(line);
						} catch (Exception ignored) {
						}
						out.println(line);
					}
					if (phase == 2) {
						System.out.printf(" Sent from the client (RSA): %s\n", line);
						try {
							System.out.printf(" Sent from the client (decrypted): %s\n", rsa.decrypt(line));
							dc_line = rsa.decrypt(line);
						} catch (Exception ignored) {
						}
						out.println(line);
					}
					if ("aes on".equalsIgnoreCase(line)) {
						phase++;
					}
					if ("rsa on".equalsIgnoreCase(line)) {
						phase = 2;
					}
					if ("ec off".equalsIgnoreCase(dc_line)) {
						phase = 0;
						dc_line = null;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
						clientSocket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
