package ds2015_client;

import java.util.TimerTask;

public class UploadSolutionTask extends TimerTask {
	@Override
	public void run() {
		if (Main.Connected) {
			for (String key : Main.solutions.keySet()) {
				HTTPconnector.found(key, Main.solutions.get(key), UI.hardClientName);
			}
		}
	}
}