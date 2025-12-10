
import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleServer {
    private static final int PORT = 5000;
    private static List<Worker> workers = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server running on port " + PORT);

        while (true) {
            Socket socket = server.accept();
            System.out.println("A new client joined.");

            Worker w = new Worker(socket);
            workers.add(w);
            w.start();
        }
    }

    static class Worker extends Thread {
        Socket socket;
        BufferedReader in;
        PrintWriter out;

        Worker(Socket s) throws IOException {
            socket = s;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                String text;
                while ((text = in.readLine()) != null) {
                    sendToEveryone(text, this);
                }
            } catch (IOException e) {
                System.out.println("Client left.");
            }
        }
    }

    static void sendToEveryone(String msg, Worker whoSent) {
        for (Worker w : workers) {
            if (w != whoSent) {
                w.out.println(msg);
            }
        }
    }
}
