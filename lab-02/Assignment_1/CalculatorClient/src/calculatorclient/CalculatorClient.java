package calculatorclient;

import interfaces.CalculatorIn;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CalculatorClient {
    public static void main(String[] args){
        try{
            CalculatorIn calculatorIn = (CalculatorIn) Naming.lookup(
                    "rmi://localhost:1098/CalculatorService"
            );

            double result = calculatorIn.add(10,20);
            double result2 = calculatorIn.sub(10,20);
            double result3 = calculatorIn.mul(10,20);
            double result4 = calculatorIn.div(10,20);
            System.out.println("10+20 = " + result);
            System.out.println("10-20 = " + result2);
            System.out.println("10*20 = " + result3);
            System.out.println("10/20 = " + result4);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
