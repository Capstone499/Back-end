import java.net.*;
import java.io.*;

public class Server {
	// initialize socket and input stream
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream in = null;

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

			// reads message from client until "Over" is sent
			while (!line.equals("Over")) {
				try {
					line = in.readUTF();
					System.out.println(line);

				} catch (IOException i) {
					System.out.println(i);
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

class ClientHandler extends Thread{
	Socket socket;
	DataInputStream in;
	DataOutputStream out;
	String strrecieved="",strsent="",name;
	ClientHandler(Socket socket,String name,DataInputStream in,DataOutputStream out)
	{
		this.socket=socket;
		this.in=in;
		this.out=out;
		this.name=name;
	}
	public void run() 
	{
		try
		{
			while(true)
			{
				out.writeUTF("What is your name? Written already write Exit.");
				strrecieved=in.readUTF();
				if(strrecieved.equals("Exit"))
				{
					System.out.println("Client "+this.socket+" EXITED");
					this.socket.close();
					this.in.close();
					this.out.close();
					break;
				}
				System.out.println("Client Messaged "+strrecieved);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
}
