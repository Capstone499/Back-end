import java.net.*;
// import java.io.*;

public class dumClient implements Runnable {
    private final Socket socket;

    public dumClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("\nclient Started for " + this.socket);
        // handleRequest(this.socket);
        System.out.println("Client Terminated for " + this.socket + "\n");
    }
}