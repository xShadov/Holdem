package ServerClientTests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.tp.holdem.SampleResponse;

import junit.framework.TestCase;

public class MessagerTest extends TestCase {
	
	private ArrayList<Thread> threads = new ArrayList();
	ArrayList<EndPoint> endPoints = new ArrayList();
	private Timer timer;
	boolean fail;
	
	protected void setUp () throws Exception {
		timer = new Timer();
	}

	protected void tearDown () throws Exception {
		timer.cancel();
	}
	
	public void testPing () throws IOException {
		final Server server = new Server();
		
		Kryo kryo = server.getKryo();
        kryo.register(SampleResponse.class);
        
		startEndPoint(server);
		server.bind(55555);

		final Client client = new Client();
		
		Kryo kryo2 = client.getKryo();
        kryo2.register(SampleResponse.class);
		
		startEndPoint(client);
		client.addListener(new Listener() {
			public void connected (Connection connection) {
				SampleResponse response = new SampleResponse("T", "Test");
				server.sendToTCP(connection.getID(), response);
			}

			public void received (Connection connection, Object object){
				if (object instanceof SampleResponse){
					SampleResponse resp = (SampleResponse) object;
					if(!resp.getText().equals("Test")){
						fail = true;
					}
					if(!resp.getTAG().equals("T")){
						fail = true;
					}
				}
			}
		});
		client.connect(5000, "127.0.0.1", 55555);

		waitForThreads(5000);
	}
	
	public void startEndPoint (EndPoint endPoint) {
		endPoints.add(endPoint);
		Thread thread = new Thread(endPoint, endPoint.getClass().getSimpleName());
		threads.add(thread);
		thread.start();
	}
	
	public void waitForThreads (int stopAfterMillis) {
		if (stopAfterMillis > 10000) throw new IllegalArgumentException("stopAfterMillis must be < 10000");
		stopEndPoints(stopAfterMillis);
		waitForThreads();
	}

	public void waitForThreads () {
		fail = false;
		TimerTask failTask = new TimerTask() {
			public void run () {
				stopEndPoints();
				fail = true;
			}
		};
		timer.schedule(failTask, 11000);
		while (true) {
			for (Iterator iter = threads.iterator(); iter.hasNext();) {
				Thread thread = (Thread)iter.next();
				if (!thread.isAlive()) iter.remove();
			}
			if (threads.isEmpty()) break;
			try {
				Thread.sleep(100);
			} catch (InterruptedException ignored) {
			}
		}
		failTask.cancel();
		if (fail) fail("Test did not complete in a timely manner.");
		// Give sockets a chance to close before starting the next test.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ignored) {
		}
	}
	
	public void stopEndPoints () {
		stopEndPoints(0);
	}

	public void stopEndPoints (int stopAfterMillis) {
		timer.schedule(new TimerTask() {
			public void run () {
				for (EndPoint endPoint : endPoints)
					endPoint.stop();
				endPoints.clear();
			}
		}, stopAfterMillis);
	}
	
	
}
