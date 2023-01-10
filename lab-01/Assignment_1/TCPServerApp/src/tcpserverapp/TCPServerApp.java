package tcpserverapp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerApp {
    public static void main(String[] args) {
        //The port, at which, the server is listening for requests
        int port = 12345;
        try {
            //ServerSocket object is used to listen for requests
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Server is ready to receive command!");
            while(true){
                //accepting connection requests
                Socket socket = ss.accept();
                //get the input stream to read data
                InputStream is = socket.getInputStream();
                //Read data as character
                InputStreamReader isr = new
                        InputStreamReader(is);
                //Read data as lines
                BufferedReader br = new BufferedReader(isr);
                //Read the string command from the user
                String command = br.readLine();
                String cmdArray[] = command.split("\\s+");
                String response = executeCommand(cmdArray);

                //Access to the output stream, to write data back
                OutputStream os = socket.getOutputStream();
                //Write lines
                PrintWriter pw = new PrintWriter(os);
                pw.println(response);
                pw.flush();
                pw.close();
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private static String executeCommand(String[] cmdArray){
        String response = "";
        String[] validCommands = {"add","subtract","multiply", "divide"};
        if(cmdArray.length != 3){
            return "Result: Invalid number of arguments";
        }
        boolean valid = false;
        for(int i = 0; i < validCommands.length; i++){
            if(validCommands[i].equals(cmdArray[0])){
                valid = true;
            }
        }
        int x;
        int y;
        if(checkInt(cmdArray[1])){
            x = Integer.parseInt(cmdArray[1]);
        }else{
            return "Error: \"" + cmdArray[1] + "\" is not a number";
        }
        if(checkInt(cmdArray[2])){
            y = Integer.parseInt(cmdArray[2]);
        }else{
            return "Error: \"" + cmdArray[2] + "\" is not a number";
        }

        if(valid == true){
            if(cmdArray[0].equals("add")){
                return add(x,y);
            }
            if(cmdArray[0].equals("subtract")){
                return subtract(x,y);
            }
            if(cmdArray[0].equals("multiply")){
                return multiply(x,y);
            }
            if(cmdArray[0].equals("divide")){
                return divide(x,y);
            }
        }
        return "Error: Invalid command \"" + cmdArray[0] + "\"";
    }

    private static String add (int x, int y){
        String result = Integer.toString(x+y);
        return "The add result is: " + result;
    }

    private static String subtract (int x, int y){
        String result = Integer.toString(x-y);
        return "The subtract result is: " + result;
    }

    private static String divide (int x, int y){
        String result;
        try{
            result = Float.toString(x/y);
            return "The divide result is: " + result;
        }catch(ArithmeticException e){
            return "Error: Divided by zero exception";
        }
    }

    private static String multiply (int x, int y){
        String result = Integer.toString(x*y);
        return "The multiply result is: " + result;
    }

    private static boolean checkInt(String arg){
        int x;
        try{
            x = Integer.parseInt(arg);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}

