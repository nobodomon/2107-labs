import java.awt.EventQueue;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class GroupChatApp2 extends JFrame {
	
	MulticastSocket multicastSocket = null;
	InetAddress multicastGroup = null;

	private JPanel contentPane;
	private JTextField txtUserName;
	private JTextField txtCreateGroupName;
	private JTextField txtJoinGroup;
	private JTextField txtMsg;
	private JButton btnJoin;
	private JButton btnUpdate;
	private JButton btnCreate;
	private JButton btnSend;
	private String userName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GroupChatApp2 frame = new GroupChatApp2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GroupChatApp2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setBounds(10, 11, 72, 14);
		contentPane.add(lblUserName);
		
		JLabel lblCreateGroup = new JLabel("Create Group");
		lblCreateGroup.setBounds(10, 36, 85, 14);
		contentPane.add(lblCreateGroup);
		
		JLabel lblJoinGroup = new JLabel("Join Group");
		lblJoinGroup.setBounds(10, 61, 72, 14);
		contentPane.add(lblJoinGroup);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(106, 12, 140, 20);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		txtCreateGroupName = new JTextField();
		String groupName = txtCreateGroupName.getText();
		txtCreateGroupName.setBounds(106, 37, 140, 20);
		contentPane.add(txtCreateGroupName);
		txtCreateGroupName.setColumns(10);
		
		txtJoinGroup = new JTextField();
		String joinGroupName = txtJoinGroup.getText();
		txtJoinGroup.setBounds(106, 62, 140, 20);
		contentPane.add(txtJoinGroup);
		txtJoinGroup.setColumns(10);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 86, 414, 142);
		contentPane.add(textArea);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userName = txtUserName.getText();
				textArea.append(userName + " updated \n" );
				System.out.println(userName + " updated" );
			}
		});
		btnUpdate.setBounds(268, 11, 89, 23);
		contentPane.add(btnUpdate);
		
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					multicastGroup = InetAddress.getByName("228.1.1.1");
					multicastSocket = new MulticastSocket(6789);
					System.out.println("Created " + groupName);
					//Join
					multicastSocket.joinGroup(multicastGroup);
					//Send a joined message
					String message = userName + " joined.";
					byte[] buf = message.getBytes();
					DatagramPacket dgpConnected = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
					multicastSocket.send(dgpConnected);
					
					//Create a new thread to keep listening for packets from the group
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
					// Disable this button
					btnJoin.setEnabled(false);
					// Enable the button to send a message
					btnSend.setEnabled(true);
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnCreate.setBounds(268, 36, 89, 23);
		contentPane.add(btnCreate);
		
		btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// If group name is same as the one created
					System.out.println(joinGroupName + groupName);
					if (joinGroupName.equals(groupName)) {
						System.out.println("Joining " + groupName);
						multicastGroup = InetAddress.getByName("228.1.1.1");
						multicastSocket = new MulticastSocket(6789);
						//Join
						multicastSocket.joinGroup(multicastGroup);
						//Send a joined message
						String message = userName + " joined.";
						byte[] buf = message.getBytes();
						DatagramPacket dgpConnected = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
						multicastSocket.send(dgpConnected);
						
						//Create a new thread to keep listening for packets from the group
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
						// Disable this button
						btnJoin.setEnabled(false);
						// Enable the button to send a message
						btnSend.setEnabled(true);
					}
					else {
						//Display try again
					}
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnJoin.setBounds(268, 61, 89, 23);
		contentPane.add(btnJoin);
		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(10, 236, 52, 14);
		contentPane.add(lblMessage);
		
		txtMsg = new JTextField();
		txtMsg.setBounds(70, 233, 276, 20);
		contentPane.add(txtMsg);
		txtMsg.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String msg = txtMsg.getText();
					msg = userName + ": " + msg;
					byte[] buf = msg.getBytes();
					DatagramPacket dgpSend = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
					multicastSocket.send(dgpSend);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnSend.setBounds(356, 232, 68, 23);
		contentPane.add(btnSend);
	}
}
