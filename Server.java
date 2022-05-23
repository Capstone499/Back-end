import java.io.*;
import java.net.*;

class Server {

	public static void main(String[] args) {
		ServerSocket server = null;
		// starting up encryption things for later

		try {

			// server is listening on port 25565
			server = new ServerSocket(25565);
			server.setReuseAddress(true);

			System.out.println("Server is now online");
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
			String user = null;
			String pass = null;
			int attempt = 0;
			int encryption_level = 0;
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

				String line = "";
				String dc_line = null;

				while (Authorization(user, pass) != true) {
					try {
						user = rsa.decrypt(in.readLine());
						pass = rsa.decrypt(in.readLine());
					} catch (Exception ignored) {
					}
					System.out.println("attempted username: " + user);
					System.out.println("attempted password: " + pass);
					if (Authorization(user, pass) != false) {
						out.println("authorized");
						System.out.println(user + " Successfully Logged In");
					} else {
						if (attempt == 3) {
							out.println("Last Attempt!");
						}
						if (attempt > 4) {
							break;
						} else {
							out.println("failed. try again!");
							attempt++;
						}
						System.out.println("Login Attempt # " + attempt);
					}
				}

				if (attempt > 4) {
					line = null;
					System.out.print(user + " failed to login. closing thread\n");
				} else {
					pass = null;
					System.out.print(user + " logged in.\n");
					out.println(user);
				}

				while (line != null) {
					line = in.readLine();
					// writing the received message from
					// client
					if (encryption_level == 0) {
						System.out.printf(" Sent from " + user + ": %s\n", line);
						out.println(line);
					}
					if (encryption_level == 1) {
						System.out.printf(" Sent from " + user + " (AES): %s\n", line);
						try {
							System.out.printf(" Sent from " + user + " (decrypted): %s\n", aes.decrypt(line));
							dc_line = aes.decrypt(line);
						} catch (Exception ignored) {
						}
						out.println(line);
					}
					if (encryption_level == 2) {
						System.out.printf(" Sent from " + user + " (RSA): %s\n", line);
						try {
							System.out.printf(" Sent from " + user + " (decrypted): %s\n", rsa.decrypt(line));
							dc_line = rsa.decrypt(line);
						} catch (Exception ignored) {
						}
						out.println(line);
					}
					if ("aes on".equalsIgnoreCase(line)) {
						encryption_level++;
					}
					if ("rsa on".equalsIgnoreCase(line)) {
						encryption_level = 2;
					}
					if ("ec off".equalsIgnoreCase(dc_line)) {
						encryption_level = 0;
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
			if (attempt < 5) {
				System.out.print(user + " has ended their session.\n");
				user = null;
			}
		}
	}
}
