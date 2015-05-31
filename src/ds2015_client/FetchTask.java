package ds2015_client;

import java.util.TimerTask;

import org.apache.http.client.HttpResponseException;

public class FetchTask extends TimerTask {
	@Override
	public void run() {
		if (Main.Connected && !Main.Working && !Main.Paused && !Main.Stopped && !Main.ThreadExists) {
			try {
				HTTPconnector.next();
			} catch (HttpResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}