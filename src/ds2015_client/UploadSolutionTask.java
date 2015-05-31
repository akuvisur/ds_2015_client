package ds2015_client;

import java.util.TimerTask;

public class UploadSolutionTask extends TimerTask {
	@Override
	public void run() {
		if (Main.Connected) {
			for (String key : Main.solutions.keySet()) {
				HTTPconnector.found(HTTPconnector.rot13(key), HTTPconnector.rot13(Main.solutions.get(key)), HTTPconnector.rot13(UI.hardClientName));
			}
		}
	}
}