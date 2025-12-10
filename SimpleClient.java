import java.io.*;
import java.net.*;

public class SimpleClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connected to server.");

        BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Thread to print whatever server sends
        new Thread(() -> {
            try {
                String line;
                while ((line = fromServer.readLine()) != null) {
                    System.out.println(">> " + line);
                }
            } catch (Exception e) {
                System.out.println("Disconnected.");
            }
        }).start();

        // main thread: send what user types
        String userLine;
        while ((userLine = fromUser.readLine()) != null) {
            out.println(userLine);
        }
    }
}
