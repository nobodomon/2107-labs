package interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SITStudent extends Remote {
    public String greet(String name) throws RemoteException;
    public int add(int x, int y) throws RemoteException;
}
