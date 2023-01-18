package rmiserverapp;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import interfaces.SITStudent;


public class RMIServerApp {
    public static void main(String[] args) {
        try {
            StudentServant studentServant = new StudentServant();
            // Bind the remote object's stub in the registry
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/StudentService", studentServant);
            System.err.println("Server is ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
