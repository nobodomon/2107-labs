package tcpclientapp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
public class TCPClientApp {
    public static void main(String[] args) {
        //Name of the host that we are going to connect to
        String hostName = "localhost";
        //Make sure that this port is the same as
        //the serrver listenign port
        int port = 12345;
        try {
            //Use socket to connect to the server
            Socket socket = new Socket(hostName, port);
            String request = "divide 5 0";
            //Access to the output stream
            OutputStream os = socket.getOutputStream();
            //Write line
            PrintWriter pw = new PrintWriter(os);
            pw.println(request);
            pw.flush();
            //Read response.
            InputStream is = socket.getInputStream();
            //Read characters
            InputStreamReader isr = new InputStreamReader(is);
            //Read lines
            BufferedReader br = new BufferedReader(isr);
            String header = br.readLine();
            System.out.println(header);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
