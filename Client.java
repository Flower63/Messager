import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Dennis on 5/15/2015.
 */
public class Client implements Runnable {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String receivedMessage;
    private Thread thread;
    private String name;
    private String ip;

    //Constructor, receives socket object
    public Client(Socket socket) {
        this.socket = socket;
        thread = new Thread(this);
        thread.start();
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            ip = socket.getInetAddress().getHostAddress();
            System.out.println("Client " + ip);
            //Creating input and output streams from socket
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            System.out.println("Connection lost");
        }
        //Main endless loop for client, waits for new messages
        while (true) {
            try {
                receivedMessage = inputStream.readUTF();
                if (receivedMessage.startsWith("*nam*")) {
                    //Registering new user's name, sends it with prefix *add* to whole collection
                    receivedMessage = receivedMessage.replaceFirst("\\*nam\\*", "");
                    name = receivedMessage;
                    for (Client client : MSGRServer.clients) {
                        client.send("*add*" + name);
                        if (!client.getName().equals(name)) {
                            this.send("*add*" + client.getName());
                        }
                    }
                } else {
                    //Forwards another messages to another clients (self included)
                    for (Client client : MSGRServer.clients) {
                        client.send(receivedMessage);
                    }
                }
            } catch (IOException e) {
                System.out.println("lost " + ip);
                MSGRServer.clients.remove(this);
                for (Client client : MSGRServer.clients) {
                    //Removes itself from other client's lists
                    client.send("*del*" + name);
                }
                break;
            }
        }
    }

    //Send message
    public void send(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            System.out.println("Cannot send message to " + ip);
        }
    }
}
