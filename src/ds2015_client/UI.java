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

import net.miginfocom.swing.MigLayout;

import org.apache.http.client.HttpResponseException;

public class UI implements ActionListener {
	private String DEFAULT_SERVER = "moetto.dy.fi:5000";
	
	static JTextField hostUrl;
	static JPanel mainPane;
	static JTextField clientName;
	public static String hardClientName;
	static JTextField hashName;
	static JButton connectButton;
	static JButton hashButton;
	static JScrollPane statusPane;
	public static JButton startButton;
	public static JButton pauseButton;
	public static JLabel statusText;
	
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
		/*
		hashName = new JTextField(32);
		mainPane.add(hashName, "span 2");
		hashButton = new JButton("Send new MD5");
		hashButton.addActionListener(this);
		hashButton.setActionCommand("post");
		mainPane.add(hashButton);
		*/
		statusWindow.setEditable(false);
		statusPane = new JScrollPane(statusWindow);
		statusWindow.setLineWrap(true);
		mainPane.add(statusPane, "span 3");
		
		startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.addActionListener(this);
		
		pauseButton = new JButton("Pause");
		pauseButton.setActionCommand("pause");
		pauseButton.addActionListener(this);
		pauseButton.setEnabled(false);
		
		statusText = new JLabel("");
		
		mainPane.add(startButton);
		mainPane.add(pauseButton);
		mainPane.add(statusText);
		
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
			UI.setStatus("Attempting to connect..");
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
					UI.setStatus("Connected");
					Main.Connected = true;
				}
			} catch (HttpResponseException e1) {
				statusWindow.append("\nINTERNAL SERVER ERROR");
			}
			if (!Main.Working) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {HTTPconnector.start();} catch (HttpResponseException e1) {e1.printStackTrace();}
					}
				}).start();
			}
		}
		if (e.getActionCommand().equals("disconnect")) {
			// post disconnect, change things if ok
			UI.setStatus("Attempting disconnect..");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String response = HTTPconnector.disconnect(clientName.getText());
						if (response.contains("Deleted")) {
							Main.Connected = false;
							Main.Working = false;
							statusWindow.append("\nDisconnected.");
							connectButton.setActionCommand("connect");
							connectButton.setText("Connect");
							hostUrl.setEnabled(true);
							clientName.setEnabled(true);
							UI.setStatus("Disconnected");
							Main.stop();
							refresh();
							
						}
					}
					 catch (HttpResponseException e1) {
						statusWindow.append("\nINTERNAL SERVER ERROR");
					}
				}
			}).start();
		
		}
		if (e.getActionCommand().equals("post")) {
			// post new hash
		}
		if (e.getActionCommand().equals("start")) {
			Main.Stopped = false;
			UI.startButton.setText("Stop");
			UI.startButton.setActionCommand("stop");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						HTTPconnector.start();
					} catch (HttpResponseException e) {e.printStackTrace();}
				}
			}).start();
			Main.Stopped = false;
			pauseButton.setEnabled(true);
			Main.Paused = false;
			UI.pauseButton.setText("Pause");
			UI.pauseButton.setActionCommand("pause");
			UI.setStatus("Started");
			UI.refresh();
		}
		if (e.getActionCommand().equals("stop")) {
			Main.Stopped = true;
			UI.startButton.setText("Start");
			UI.startButton.setActionCommand("start");
			pauseButton.setEnabled(false);
			UI.setStatus("Stopped");
			UI.refresh();
		}
		if (e.getActionCommand().equals("pause")) {
			Main.Paused = true;
			UI.pauseButton.setText("Continue");
			UI.pauseButton.setActionCommand("continue");
			UI.setStatus("Paused.");
			UI.refresh();
		}
		if (e.getActionCommand().equals("continue")) {
			Main.Paused = false;
			UI.pauseButton.setText("Pause");
			UI.pauseButton.setActionCommand("pause");
			UI.setStatus("Continuing..");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						HTTPconnector.next();
					} catch (HttpResponseException e) {e.printStackTrace();}
				}
			}).start();
			Main.Paused = false;
			UI.refresh();
		}
	}
	
	public static void refresh() {
		JScrollBar vertical = statusPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	public static void setStatus(String message) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				statusText.setText(message);
				statusText.repaint();
			}
		}).start();;
		
	}
}
