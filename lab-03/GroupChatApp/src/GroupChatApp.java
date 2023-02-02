import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class GroupChatApp extends JFrame {
    MulticastSocket multicastSocket= null;
    InetAddress multicastGroup = null;
    private JLabel lblProcessID, lblProcessIDValue, lblGroupIP, lblMessage;
    private JTextField txtGroupIP, textField;
    private JButton btnJoin, btnLeave, btnSend;
    private JTextArea textArea;

    public GroupChatApp() {
        super("Client GUI");
        setLayout(new FlowLayout());

        lblProcessID = new JLabel("Process ID:");
        add(lblProcessID);

        lblProcessIDValue = new JLabel("Process ID Value");
        lblProcessIDValue.setText(ManagementFactory.getRuntimeMXBean().getName());
        add(lblProcessIDValue);


        lblGroupIP = new JLabel("Group IP:");
        add(lblGroupIP);
        add(Box.createVerticalStrut(100));

        txtGroupIP = new JTextField("228.5.6.7", 15);
        add(txtGroupIP);

        btnJoin = new JButton("Join");
        add(btnJoin);

        btnJoin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    multicastGroup = InetAddress.getByName(txtGroupIP.getText());
                    multicastSocket = new MulticastSocket(6789);
                    //Join
                    multicastSocket.joinGroup(multicastGroup);
                    // 'Send a joined message

                    String message = lblProcessIDValue.getText() + " joined";
                    byte[] buf = message.getBytes();
                    DatagramPacket dgpConnected = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
                    multicastSocket.send(dgpConnected);
                    // 'Create a new thread to keep listening for packets from the group
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            byte buf1[] = new byte[1000];
                            DatagramPacket dgpReceived = new DatagramPacket(buf1, buf1.length);
                            while (true) {
                                try {
                                    multicastSocket.receive(dgpReceived);
                                    byte[] receivedData = dgpReceived.getData();
                                    int length = dgpReceived.getLength();
                                    //Assumed we received string
                                    String msg = new String(receivedData, 0, length);
                                    textArea.append(msg + "\n");
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    // 'Disable this button
                    btnJoin.setEnabled(false);
                    // 'Enable the button leave and to send message
                    btnLeave.setEnabled(true);
                    btnSend.setEnabled(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnLeave = new JButton("Leave");
        btnLeave.setEnabled(false);
        add(btnLeave);
        btnLeave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent argO) {
                try {
                    String msg = lblProcessIDValue.getText() + " is leaving";
                    byte[] buf = msg.getBytes();
                    DatagramPacket dgpSend = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
                    multicastSocket.send(dgpSend);
                    multicastSocket.leaveGroup(multicastGroup);
                    btnJoin.setEnabled(true);
                    btnSend.setEnabled(false);
                    btnLeave.setEnabled(false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        add(new JScrollPane(textArea));

        lblMessage = new JLabel("Message:");
        add(lblMessage);

        textField = new JTextField("", 30);
        add(textField);

        btnSend = new JButton("Send");
        btnSend.setEnabled(false);
        add(btnSend);
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String msg = textField.getText();
                    msg = lblProcessIDValue.getText() + ": " + msg;
                    byte[] buf = msg.getBytes();
                    DatagramPacket dgpSend = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
                    multicastSocket.send(dgpSend);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });



        setSize(570, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }





    public static void main(String[] args) {
        new GroupChatApp();
    }
}
