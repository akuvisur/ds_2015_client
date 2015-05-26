package ds2015_client;

import java.lang.reflect.InvocationTargetException;
import java.util.Timer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class Main {
	public static boolean Working = false;
	public static boolean Connected = false;
	
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
			    @Override
				public void run() {
			        // set SeaGlass laf if available
			        try {
			            UIManager.installLookAndFeel("SeaGlass", "ds2015_client.SeaGlassLookAndFeel");
			            UIManager.setLookAndFeel("ds2015_client.SeaGlassLookAndFeel");
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
		timer.schedule(new KeepAliveTask(), 30000, 30000);
	}

}
