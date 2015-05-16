import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Dennis on 5/14/2015.
 */
public class MSGRServer{

    public static CopyOnWriteArrayList<Client> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws Exception {

        //Creating serverSocket for port 3333
        ServerSocket serverSocket = new ServerSocket(3333);

        //Creating console listener to stop server with keyword "quit"
        new Runnable(){
            {
                Thread exitListener = new Thread(this);
                exitListener.start();
            }
            @Override
            public void run() {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    while (true) {
                        if ("quit".equals(reader.readLine())){
                            System.exit(0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        //Main endless loop, which adding new users into clients collection (clients)
        while (true) {
            try {

                System.out.println("Waiting for a client");
                clients.add(new Client(serverSocket.accept()));

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("client added");
        }
    }
}
