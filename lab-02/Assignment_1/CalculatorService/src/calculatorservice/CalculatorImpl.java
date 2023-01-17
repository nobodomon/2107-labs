package calculatorservice;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import interfaces.CalculatorIn;

public class CalculatorImpl extends UnicastRemoteObject implements CalculatorIn {
    private static final long serialVersionUID = 1L;

    public CalculatorImpl() throws Exception {
        super();
    }

    @Override
    public double add(double a, double b) throws RemoteException {
        return a + b;
    }

    @Override
    public double sub(double a, double b) throws RemoteException {
        return a - b;
    }

    @Override
    public double mul(double a, double b) throws RemoteException {
        return a * b;
    }

    @Override
    public double div(double a, double b) throws RemoteException {
        return a / b;
    }
}

