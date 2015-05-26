package ds2015_client;

import java.util.TimerTask;

public class KeepAliveTask extends TimerTask {
	@Override
	public void run() {
		System.out.println("keepalivetask");
		if (Main.Connected) HTTPconnector.keepAlive();
	}
}