import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            Color.it("Group Chat Server started on port 6666...", "yellow");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Color.it("New client connected: " + clientSocket, "green");

                ClientHandler handler = new ClientHandler(clientSocket);
                clients.add(handler);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            Color.err(e);
        }
    }

    static void broadcast(String msg, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.send(msg);
            }
        }
    }

    static void remove(ClientHandler client) {
        clients.remove(client);
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String name;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.name = "Client-" + socket.getPort();
    }

    public void run() {
        try {
            out.writeUTF("Welcome " + name + "! Type 'exit' to leave.");

            while (true) {
                String msg = in.readUTF();
                if (msg.equalsIgnoreCase("exit")) {
                    break;
                }
                String formatted = name + ": " + msg;
                Color.it(formatted, "blue");
                Server.broadcast(formatted, this);
            }
        } catch (IOException e) {
            Color.err(e);
        } finally {
            try {
                Server.remove(this);
                in.close();
                out.close();
                socket.close();
                Server.broadcast("Disconnected "+this.name, this);
                Color.it(name + " Disconnected.", "red");
            } catch (IOException e) {
                Color.err(e);
            }
        }
    }

    public void send(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            Color.err(e);
        }
    }
}
