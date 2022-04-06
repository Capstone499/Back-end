import java.io.*;
import java.net.*;
import java.util.*;

public class BetaClient {

    	
	// driver code
	public static void main(String[] args)
	{
		// establish a connection by providing host and port
		// number
		try (Socket socket = new Socket("localhost", 1234)) {
			
			// writing to server
			PrintWriter out = new PrintWriter( //converts characters into bytes
				socket.getOutputStream(), true);

			// reading from server
			BufferedReader in
				= new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			// object of scanner class
			Scanner sc = new Scanner(System.in); //converts bytes into characters
			String line = null;

			while (!"exit".equalsIgnoreCase(line)) {
				
				// reading from user
				line = sc.nextLine();

				// sending the user input to server
				out.println(line);
				out.flush(); //clear the stream of any element 

				// displaying server reply
				System.out.println("Server replied "
								+ in.readLine());
			}
			
			// closing the scanner object
			sc.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}


