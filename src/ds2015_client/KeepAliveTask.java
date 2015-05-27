package ds2015_client;

import java.util.TimerTask;

public class KeepAliveTask extends TimerTask {
	@Override
	public void run() {
		if (Main.Connected && !Main.Working) System.out.println("keepalivetask"); HTTPconnector.keepAlive();
	}
}