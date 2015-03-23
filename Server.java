import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Dennis on 3/23/2015.
 */
public class Server {

    public static void main(String[] args) {
        WriterThread writer = new WriterThread("writer");
        Server.listener();

        try {
            writer.thrd.join();
        } catch (InterruptedException e) {
            System.out.println("Goes wrong with joining");
        }
    }
    public static void listener() {
        try {
            ServerSocket server = new ServerSocket(3334);
            Socket socket = server.accept();
            System.out.println("Listener server on air");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String incoming = reader.readLine();
            System.out.println("Recived: " + incoming);
            socket.close();
            server.close();
        } catch (IOException e) {
            System.out.println("IOExc, listener");
        }
    }
}
class WriterThread implements Runnable {
    Thread thrd;
    WriterThread(String name) {
        thrd = new Thread(this, name);
        thrd.start();
    }
    public void run() {
        try {
            ServerSocket srvr = new ServerSocket(3333);
            Socket socket = srvr.accept();
            System.out.println("Writer server on air");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Sending test string");
            out.print("ths is a testing string");
            out.close();
            socket.close();
            srvr.close();
        } catch (IOException e) {
            System.out.println("IOException occured");
        }
    }
}