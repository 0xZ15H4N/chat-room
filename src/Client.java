import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6666);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner sc = new Scanner(System.in)) {

            Color.it("Connected to group chat!", "green");

            // Thread for reading messages
            Thread reader = new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readUTF();
                        Color.it(msg, "purple");
                    }
                } catch (IOException e) {
                    Color.err(e);
                }
            });

            reader.start();

            // Writing messages
            while (true) {
                Color.it("> ", "cyan");
                String msg = sc.nextLine();
                out.writeUTF(msg);
                if (msg.equalsIgnoreCase("exit")) break;
            }

        } catch (IOException e) {
            Color.err(e);
        }
    }
}
