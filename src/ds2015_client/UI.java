package ds2015_client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.apache.http.client.HttpResponseException;

public class UI implements ActionListener {
	private String DEFAULT_SERVER = "localhost:5000";
	
	static JTextField hostUrl;
	static JPanel mainPane;
	static JTextField clientName;
	public static String hardClientName;
	static JTextField hashName;
	static JButton connectButton;
	static JButton hashButton;
	static JScrollPane statusPane;
	
	public static JTextArea statusWindow = new JTextArea(20, 30);
	
	public UI() {
		JFrame jf = new JFrame("Ds 2015 client");
		jf.setSize(400, 500);
		
		mainPane = new JPanel(new MigLayout("wrap 3"));
		
		mainPane.add(new JLabel("Server url"));
		hostUrl = new JTextField(30);
		hostUrl.setText(DEFAULT_SERVER);
		mainPane.add(hostUrl, "span 2");
		
		mainPane.add(new JLabel("Client name:"));
		clientName = new JTextField(32);
		mainPane.add(clientName);
		connectButton = new JButton("Connect");
		connectButton.setActionCommand("connect");
		connectButton.addActionListener(this);
		mainPane.add(connectButton);
		
		hashName = new JTextField(32);
		mainPane.add(hashName, "span 2");
		hashButton = new JButton("Send new MD5");
		hashButton.addActionListener(this);
		hashButton.setActionCommand("post");
		mainPane.add(hashButton);
		
		statusWindow.setEditable(false);
		statusPane = new JScrollPane(statusWindow);
		statusWindow.setLineWrap(true);
		mainPane.add(statusPane, "span 3");
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(mainPane);
		jf.setVisible(true);
	}

	public static String getUrl() {
		return hostUrl.getText();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("connect")) {
			String response;
			try {
				response = HTTPconnector.connect(clientName.getText());
				System.out.println(response);
				if (response.contains("OK")) {
					statusWindow.append("\nConnected.");
					refresh();
					connectButton.setActionCommand("disconnect");
					connectButton.setText("Disconnect");
					hostUrl.setEnabled(false);
					hardClientName = clientName.getText();
					clientName.setEnabled(false);
					
					Main.Connected = true;
				}
			} catch (HttpResponseException e1) {
				statusWindow.append("\nINTERNAL SERVER ERROR");
			}
			if (!Main.Working) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {HTTPconnector.start();} catch (HttpResponseException e1) {e1.printStackTrace();}
					}
				});
			}
		}
		if (e.getActionCommand().equals("disconnect")) {
			// post disconnect, change things if ok
			String response;
			try {
				response = HTTPconnector.disconnect(clientName.getText());
				if (response.contains("Deleted")) {
					Main.Connected = false;
					Main.Working = false;
					statusWindow.append("\nDisonnected.");
					refresh();
					connectButton.setActionCommand("connect");
					connectButton.setText("Connect");
					hostUrl.setEnabled(true);
					clientName.setEnabled(true);
					
				}
			} catch (HttpResponseException e1) {
				statusWindow.append("\nINTERNAL SERVER ERROR");
			}
		}
		if (e.getActionCommand().equals("post")) {
			// post new hash
		}
	}
	
	public static void refresh() {
		JScrollBar vertical = statusPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
}
