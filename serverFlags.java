import java.net.*;
import java.io.*;

public class serverFlags implements Runnable {
    public void run() {
        System.out.println("Webserver Started");
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                System.out.println("Waiting for client request");
                Socket remote = serverSocket.accept();
                System.out.println("Connection made");
                new Thread(new dumClient(remote)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new serverFlags();
    }
}
