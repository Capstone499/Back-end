import java.net.*;
import java.io.*;

public class dumClient implements Runnable {
    @Override
    public void run() {
        Socket s = new Socket("localhost", 81738);
    }
}