package rmiclientapp;

import java.net.MalformedURLException;
import java.rmi.Naming;
import interfaces.SITStudent;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClientApp {
    public static void main(String[] args){
        try{
            SITStudent sitStudent = (SITStudent) Naming.lookup(
                    "rmi://localhost:1099/StudentService"
            );

            String greetMsg = sitStudent.greet("Mr A");

            System.out.println(greetMsg);

            int result = sitStudent.add(10,20);
            System.out.println("10+20 = " + result);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
