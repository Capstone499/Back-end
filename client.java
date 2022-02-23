// A Java program for a Client 
import java.net.*;
//import java.io.*;

public class Client implements Runnable {
    private final Socket socket; 
    public Client (Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run(){
        System.out.println("\nclient Started for " + this.socket);
        //handleRequest(this.socket);
        System.out.println("Client Terminated for " + this.socket + "\n");
    }
}
    









/* public void handleRequest (Socket socket) {
        try (BufferedReader in = new BufferedReader{
            new inputStreamReader (socket.getInputStream()));){
                String headerLine = in.readLine();
                    new StringTokenizer(headerLine);
                String httpMethod = tokenizer.nextToken();
        
            } catch (Exception e){
                e.printStackTrace();
    }
    }
    if (httpMethod.equals("GET")){
        System.out.println ("Get method processed");
        String httpQueryString = tokenizer.nextToken();
        StringBuilder responseBuffer = new StringBuilder();
        responseBuffer
            .append("<html><hi><Server Home Page"</h1><br>)
            .append("<b>Wecome to my web server!</b><BR>")
            .append("</html>");
        sendResponse(socket,200, responseBuffer.toString());
    } else {
        System.out.println("The HTTP method is not recognized");
        sendResponse (socket, 405, "Method not allowed"); 
    } 
    }
*/