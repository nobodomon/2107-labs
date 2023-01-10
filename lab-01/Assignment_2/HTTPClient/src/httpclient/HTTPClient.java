package httpclient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
public class HTTPClient {
    public static void main(String[] args)  throws IOException {
        // Open a socket connection to the server
        Socket socket = new Socket("127.0.0.1", 12345);

        // Open I/O streams for the socket connection
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Send the HTTP GET request to the server
        out.println("GET index.html HTTP/1.0");
        out.println();

        // Read the response from the server
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }

        // Close the I/O streams and socket connection
        out.close();
        in.close();
        socket.close();
    }
}
