package ds2015_client;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Timer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Main {
	public static boolean Working = false;
	public static boolean Connected = false;
	public static boolean Stopped = true;
	public static boolean Paused = false;
	public static boolean ThreadExists = false;
	public static HashMap<String, String> solutions = new HashMap<String, String>();
	public static CloseableHttpClient httpclient = HttpClients.createDefault();
	
	public static int FETCH_DELAY = 5000;
	
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
			    @Override
				public void run() {
			        // set SeaGlass laf if available
			        try {
			            UIManager.installLookAndFeel("SeaGlass", "com.seaglasslookandfeel.SeaGlassLookAndFeel");
			            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			        } catch (Exception e) {
			            System.err.println("Seaglass LAF not available using Ocean.");
			            try {
			                UIManager.setLookAndFeel(new MetalLookAndFeel());
			            } catch (Exception e2) {
			                System.err.println("Unable to use Ocean LAF using default.");
			            }
			        }
			    }
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new UI();
		Timer timer = new Timer();
		// these times should prohibit overlapping requests that might mess up the client
		timer.schedule(new KeepAliveTask(), 22222, 25123);
		timer.schedule(new UploadSolutionTask(), 30000, 7234);
		timer.schedule(new FetchTask(), 5623, FETCH_DELAY);
	}
	
	public static void stop() {
		Stopped = true;
		UI.startButton.setText("Start");
		UI.startButton.setActionCommand("start");
		UI.pauseButton.setText("Pause");
		UI.pauseButton.setActionCommand("pause");
		UI.pauseButton.setEnabled(false);
	}

}
