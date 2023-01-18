package calculatorservice;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CalculatorService{
    public static void main(String[] args){
        try {
            CalculatorImpl calculatorImpl = new CalculatorImpl();
            // Bind the remote object's stub in the registry
            LocateRegistry.createRegistry(1098);
            Naming.rebind("rmi://localhost:1098/CalculatorService", calculatorImpl);
            System.err.println("Server is ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
