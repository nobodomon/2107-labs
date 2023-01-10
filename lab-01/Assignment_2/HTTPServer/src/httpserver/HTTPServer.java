package httpserver;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
    //The port, at which, the server is listening for requests
    public static void main(String[] args) throws IOException {
        // Open a ServerSocket on port 80
        ServerSocket serverSocket = new ServerSocket(12345);

        while (true) {
            // Wait for a client connection request
            Socket socket = serverSocket.accept();

            // Open I/O streams from the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Read the request line of the client's HTTP request message
            String requestLine = in.readLine();

            // Check the command and react accordingly
            String[] parts = requestLine.split(" ");
            String command = parts[0];
            String path = parts[1];

            if (command.equals("GET")) {
                // Extract the path and filename from the request line
                String filename = path.substring(path.lastIndexOf('/') + 1);
                // Open the file requested (and check if it exists)
                System.out.println(filename);
                File file = new File(filename);
                System.out.println(file.getAbsolutePath());
                if (!file.exists()) {
                    // Send the response line indicating a "404 Not Found" error
                    out.println("HTTP/1.0 404 Not Found");
                } else {
                    // Send the response line indicating a "200 OK" status
                    out.println("HTTP/1.0 200 OK");

                    // Set the content type to "text/html"
                    out.println("Content-Type: text/html");

                    // Send a blank line to indicate the end of the headers
                    out.println();

                    // Read in each line of the file, and send it back to the client
                    BufferedReader fileIn = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = fileIn.readLine()) != null) {
                        out.println(line);
                    }
                    fileIn.close();
                }
            }

            // Close the I/O streams, file and socket connection
            in.close();
            out.close();
            socket.close();
        }
    }
}