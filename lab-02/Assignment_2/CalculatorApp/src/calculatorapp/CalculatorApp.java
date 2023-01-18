package calculatorapp;

import interfaces.CalculatorIn;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.regex.Pattern;

public class CalculatorApp {

    static JButton one = new JButton("1");
    static JButton two = new JButton("2");
    static JButton three = new JButton("3");
    static JButton four = new JButton("4");
    static JButton five = new JButton("5");
    static JButton six = new JButton("6");
    static JButton seven = new JButton("7");
    static JButton eight = new JButton("8");
    static JButton nine = new JButton("9");
    static JButton zero = new JButton("0");
    static JButton add = new JButton("+");
    static JButton sub = new JButton("-");
    static JButton mul = new JButton("*");
    static JButton div = new JButton("/");
    static JButton eq = new JButton("=");
    static JButton c = new JButton("C");

    public static void main(String[] args){
        try{
            CalculatorIn calculatorIn = (CalculatorIn) Naming.lookup(
                    "rmi://localhost:1097/CalculatorService"
            );

            JFrame frame = new JFrame("Calculator");

            JTextField textField = new JTextField("0", 20);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(4, 4));
            frame.add(textField, BorderLayout.NORTH);

            String[] inputs = {"1", "2", "3", "+", "4", "5", "6", "-", "7", "8", "9", "x", "C", "0", "=", "/"};
            for(String input : inputs){
                JButton button = new JButton(input);
                button.addActionListener(e -> {
                    try {
                        textField.setText(attemptAppend(textField, input, calculatorIn));
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                });
                buttonPanel.add(button);
            }
            frame.add(buttonPanel, BorderLayout.CENTER);

            frame.setSize(300, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static JPanel populateButtons(JPanel panel){
        GridLayout grid = new GridLayout(4, 4);
        panel.add(one);
        panel.add(two);
        panel.add(three);
        panel.add(add);
        panel.add(four);
        panel.add(five);
        panel.add(six);
        panel.add(sub);
        panel.add(seven);
        panel.add(eight);
        panel.add(nine);
        panel.add(mul);
        panel.add(c);
        panel.add(zero);
        panel.add(eq);
        panel.add(div);

        panel.setLayout(grid);

        return panel;
    }

    public static boolean detectValidInput(String input){
        String[] operands = {"\\+", "-", "x", "/"};
        for(String operand : operands){
            if(input.split(operand).length == 2){
                return true;
            }
        }
        return false;
    }

    public static String attemptAppend(JTextField tf, String input, CalculatorIn calculatorIn) throws RemoteException {
        String text = tf.getText();
        if(text.equals("0")){
            return input;
        }else if(input.equals("C")){
            return "";
        }else if(input.equals("+")) {
            if(detectValidInput(text)){
                String[] operator = findOperandIndex(text);
                double result = calculatorIn.add(Double.parseDouble(operator[0]), Double.parseDouble(operator[1]));
                return Double.toString(result);
            }else{
                return text + input;
            }
        }else if(input.equals("-")) {
            if (detectValidInput(input)) {
                String[] operator = findOperandIndex(text);
                double result = calculatorIn.sub(Double.parseDouble(operator[0]), Double.parseDouble(operator[1]));
                return Double.toString(result);
            } else {
                return text + input;
            }
        }else if(input.equals("x")) {
            if (detectValidInput(text)) {
                String[] operator = findOperandIndex(text);
                double result = calculatorIn.mul(Double.parseDouble(operator[0]), Double.parseDouble(operator[1]));
                return Double.toString(result);
            } else {
                return text + input;
            }
        }else if(input.equals("/")) {
            if (detectValidInput(text)) {
                String[] operator = findOperandIndex(text);
                double result = calculatorIn.div(Double.parseDouble(operator[0]), Double.parseDouble(operator[1]));
                return Double.toString(result);
            } else {
                return text + input;
            }
        }else if(input.equals("=")) {
            if (detectValidInput(text)) {
                String[] operator = findOperandIndex(text);
                if(operator[1].equals("+")) {
                    double result = calculatorIn.add(Double.parseDouble(operator[0]), Double.parseDouble(operator[2]));
                    return Double.toString(result);
                }else if(operator[1].equals("-")) {
                    double result = calculatorIn.sub(Double.parseDouble(operator[0]), Double.parseDouble(operator[2]));
                    return Double.toString(result);
                }else if(operator[1].equals("x")) {
                    double result = calculatorIn.mul(Double.parseDouble(operator[0]), Double.parseDouble(operator[2]));
                    return Double.toString(result);
                }else if(operator[1].equals("/")) {
                    double result = calculatorIn.div(Double.parseDouble(operator[0]), Double.parseDouble(operator[2]));
                    return Double.toString(result);
                }else{
                    return text;
                }
            } else {
                return text;
            }
        }else{
            return text + input;
        }
    }

    public static String[] findOperandIndex(String text){
        String operator ="";
        if (text.matches(".*\\+.*")) {
            operator = "+";
        } else if (text.matches(".*\\-.*")) {
            operator = "-";
        } else if (text.matches(".*x.*")) {
            operator = "x";
        }else if (text.matches(".*\\/.*")) {
            operator = "/";
        }
        int operatorIndex = text.indexOf(operator);
        String firstNumber = text.substring(0, operatorIndex);
        String lastNumber = text.substring(operatorIndex+1);

        String[] arr = {firstNumber, operator, lastNumber};

        return arr;
    }
}
