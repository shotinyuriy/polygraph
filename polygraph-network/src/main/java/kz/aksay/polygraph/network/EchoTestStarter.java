package kz.aksay.polygraph.network;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class EchoTestStarter {

	public void serverStart() {
		int port = 8310;
		
		EchoServer echoServer = new EchoServer(8310);
		
		Thread echoServerThread = new Thread(echoServer);
		
		echoServerThread.start();
	}
	
	public void clientStart() {
		int port = 8310;
		int capacity = 10;
		
		Queue<String> messageQueue = new ArrayBlockingQueue<>(capacity);
		
		Queue<String> hostNames = new ArrayBlockingQueue<>(255);
		
		int startIp = 101;
		int offset = 15;
		for(int i = startIp; i <= startIp + offset; i++) {
			hostNames.add("192.168.10."+i);
		}
		
		hostNames.add("127.0.0.1");
		
		String hostName;
		while((hostName = hostNames.poll()) != null) {
				EchoClient echoClient = new EchoClient(hostName, port, messageQueue);
				
				Thread echoClientThread = new Thread(echoClient);
				
				echoClientThread.start();
		}
	}
	
	public static void main(String[] args) {
		EchoTestStarter starter = new EchoTestStarter();
		for(int i =0; i < args.length; i++) {
			System.out.printf("args[%d] = %s\n", i, args[i]);
		}
		if(args.length > 0) {
			if(args[0].equals("client")) {
				System.out.println("Try to start client");
				starter.clientStart();
			}
			else if(args[0].equals("server")) {
				System.out.println("Try to start server");
				starter.serverStart();
			}
		}
	}
}
