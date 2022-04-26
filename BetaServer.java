import java.io.*;
import java.net.*;
// import javax.crypto.Cipher;
// import javax.crypto.SecretKey;
// import javax.crypto.spec.GCMParameterSpec;
// import javax.crypto.spec.SecretKeySpec;
// import java.security.KeyFactory;
// import java.security.PrivateKey;
// import java.security.spec.PKCS8EncodedKeySpec;
// import java.util.Base64;

class BetaServer {
	// private String yabe = "cNY1I3M05D7jyjNCv2NdFQ==";
	// private String eyeBee = "oLFQ3dPSDv02xD1S";
	// private static final String PRIVATE_KEY_STRING = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIjcH4+yxit6mKWz4hTliAp+yCu+FivoJVJtunEXI6teht26xuRgj9KRB4cvwoMrCfJJBfH6df3Nfk8ZVuCsTKauuFIqM6FLzCbxeAfn+LbGtScbTQlwJAkLNgToB5myW7HETnIAYB88zYwmOTYTAjXFjY0PFwulcj9aeSOEVffHAgMBAAECgYAf2kOKOUyAEA94+W3T+Tv5XVqPO7WDUItnLNyot374wo5XCsKBoqu2kUSURRxbVOgCuNYmZGmTwYD1PeuHbPKFvYr+MmJEImezEMXmKqUdwGVemlvqBgN9L17HZeSICPp06sip4F2zPqs7DHB2zMDSK1EQJFS0bpin6kLsQlfzeQJBAMPBPyDlXNwsX/c6xLRGj8VPtjVe41JsNq+2uqwjn7Ha8XU6B5oqeKnBTuFAt0JIGwdJeHaPXMD6WQtl9kLpJyMCQQCy+sVn7H6Hj3BdxDyykzasN7Bg0+jkCoLW0texBjuq5f3VX0r62iLmp9g831UwO194Pa2fNmMxhTNQOAqkskkNAkAAsiowSsB2w+2famUSowGV2P+z1t+GBn53R3YIcKP7tOSQ3yDxyl7dc6N9J4a/RJRcBUXZXg8dXIZ+hOFIQZ3zAkEAkonTesUcy6zbWUpEUAlMKDDoTj7yXVNl0LGMO7pYvBHWhA6je0OCc8tUtnI8c2MJRY9qSgLjsDXYz4My46m9OQJAOI+Bp8J2FvOrd+tXqZlIJuDrO+P2I3wKQCzAyZk7GcgKOsKTVS39p00w9t1PmKlsEb7fu0PSU/g+AUVKPIeVgQ==";
	// private SecretKey key;
	// private PrivateKey privateKey;
	// private byte[] IV;

	// public void initFromStringsAES() {
	// 	key = new SecretKeySpec(decode(yabe), "AES");
	// 	this.IV = decode(eyeBee);
	// }

	// public void initPrivateKeyRSA() {
	// 	try {
	// 		PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRING));

	// 		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

	// 		privateKey = keyFactory.generatePrivate(keySpecPrivate);
	// 	} catch (Exception ignored) {
	// 	}

	// }

	// public String decryptAES(String encryptedMessage) throws Exception {
	// 	byte[] messageInBytes = decode(encryptedMessage);
	// 	Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
	// 	GCMParameterSpec spec = new GCMParameterSpec(128, IV);
	// 	decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
	// 	byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
	// 	return new String(decryptedBytes);
	// }

	// public String decryptRSA(String encryptedMessage) throws Exception {
	// 	byte[] encryptedBytes = decode(encryptedMessage);
	// 	Cipher cipher = Cipher.getInstance("RSA");
	// 	cipher.init(Cipher.DECRYPT_MODE, privateKey);
	// 	byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
	// 	return new String(decryptedMessage, "UTF8");
	// }

	// private byte[] decode(String data) {
	// 	return Base64.getDecoder().decode(data);
	// }

	public static void main(String[] args) {
		RSA rsa = new RSA();
		AES aes = new AES();
		aes.initFromStrings();
		rsa.initFromStrings();
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
				System.out.println("New client connected"
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

		public void run() {
			PrintWriter out = null;
			BufferedReader in = null;
			try {

				// get the outputstream of client
				out = new PrintWriter( // converts characters into bytes
						clientSocket.getOutputStream(), true);

				// get the inputstream of client
				in = new BufferedReader(
						new InputStreamReader(
								clientSocket.getInputStream()));

				String line;
				while ((line = in.readLine()) != null) {

					// writing the received message from
					// client
					System.out.printf(
							" Sent from the client: %s\n",
							line);
					out.println(line);
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
