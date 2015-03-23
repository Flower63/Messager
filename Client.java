import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Dennis on 3/23/2015.
 */
public class Client {
    public static void main(String[] args) {
        ClientThread listener = new ClientThread("listener");
        Client.writer();

        try {
            listener.thrd.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interruption");
        }
    }
    static void writer() {
        try {
            Socket socket = new Socket("localhost", 3334);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.write("sended string");
            out.close();
            socket.close();
        } catch (UnknownHostException h) {
            System.out.println("host not found");
        } catch (IOException e) {
            System.out.println("IO goes wrong");
        }
    }
}
class ClientThread implements Runnable {
    Thread thrd;
    ClientThread(String name) {
        thrd = new Thread(this, name);
        thrd.start();
    }
    public void run() {
        try {
            Socket socket = new Socket("localhost", 3333);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Reciving message: ");
            System.out.println(reader.readLine());
            socket.close();
        } catch (IOException e) {
            System.out.println("IO goes wrong in thread");
        }
    }
}
