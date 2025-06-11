import java.io.*;
import java.net.*;
import java.util.*;
public class Server {
    private static final List<ClientHandler> clients = new ArrayList<>();
    public static void main(String[] args) {
        String password = null;
        final String Banner = """
                    .-"      "-.
                   /            \\
       _          |              |          _
      ( \\         |,  .-.  .-.  ,|         / )
       > "=._     | )(__/  \\__)( |     _.=" <
      (_/"=._"=._ |/     /\\     \\| _.="_.="\\_)
             "=._ (_     ^^     _)"_.="
                 "=\\__|IIIIII|__/="
                _.="| \\IIIIII/ |"=._
      _     _.="_.="\\          /"=._"=._     _
     ( \\_.="_.="     `--------`     "=._"=._/ )
      > _.="                            "=._ <
     (_/                                    \\_)
            ☠ WELCOME TO THE CHATROOM ☠
""";

        try{
            password = new BufferedReader(new FileReader("pass.txt")).readLine().split(":")[1];
        
        }catch(Exception e){
            Color.err(e);
        }

        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            Color.it("Group Chat Server started on port 6666...", "yellow");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Color.it("New client Trying to connect: " + clientSocket, "Purple");
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.out.writeUTF(Banner + "\n"+"Enter the secret Password : ");
                String Password = handler.in.readUTF();
                if(Password.equals(password)){  
                    clients.add(handler);
                    new Thread(handler).start();
                    Color.it("New client connected: " + clientSocket, "green");
                
                }else{
                    handler.out.writeUTF("***Authentication Failed!***");
                    Color.it(clientSocket+" Authentication Failed!", "RED");
                    clientSocket.close();
                }
            }
        }catch (IOException e) {
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
        clients.remove(client); // remove client from the array list!
    }
}

class ClientHandler implements Runnable {
    final Socket socket;
    final DataInputStream in;
    final DataOutputStream out;
    String name;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.name = "Client-" + socket.getPort();
    }

    public void run() {
        try {
            out.writeUTF("Welcome " + name + "! Type 'exit' to leave.");
            Server.broadcast("New User connected "+this.name,this);
            
            while (true) {
                String msg = in.readUTF();
                if (msg.equalsIgnoreCase("exit")) {
                    break;
                }
                String formatted =name + ": " + msg;
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
                Server.broadcast("Disconnected "+this.name, this); // send the broad cast signalling every one that the "user-Port" is  disconnecteed
                Color.it(name + " Disconnected.", "red"); 
            } catch (IOException e) {
                Color.err(e);
            }
        }
    }
    public void send(String msg) {
        try {
            out.writeUTF(msg); // this message is going to the server , and the server is then sending the code to the other 
        } catch (IOException e) {
            Color.err(e);
        }
    }
}
