import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        Scanner sc = null;

        try {
            for (int i = 0; i < 256; i++) {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress("192.168.1." + i, 6666), 100); // 200 ms timeout
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    sc = new Scanner(System.in);
                    Color.it("\nConnected to group chat!", "green");
                    break;
                } catch (IOException e) {
                    // Ignore and try next IP
                }
            }

            if (socket == null) {
                Color.it("\nCould not connect to any server.", "red");
                return;
            }

            DataInputStream finalIn = in;
            Thread reader = new Thread(() -> {
                try {
                    while (true) {
                        String msg = finalIn.readUTF();
                        Color.it("\n"+msg+"\n", "purple");
                    }
                } catch (IOException e) {
                    Color.err(e);
                }
            });

            reader.start();
            while (true) {
                Color.it("> ", "cyan");
                String msg = sc.nextLine();
                out.writeUTF(msg);
                if (msg.strip().equalsIgnoreCase("exit")) break;
            }

        } catch (IOException e) {
            Color.err(e);
        }
    }
}
