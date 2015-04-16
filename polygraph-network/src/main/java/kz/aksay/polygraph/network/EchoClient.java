package kz.aksay.polygraph.network;

import java.io.*;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kz.aksay.polygraph.network.terminal.UserServiceTerminal;
import kz.aksay.polygraph.service.UserService;

public class EchoClient implements Runnable 
{
	private static final Logger LOG = LoggerFactory.getLogger(EchoClient.class);
	private Queue<String> messageQueue;
	private Queue<String> hostNames;
	private int port;
	private static volatile boolean isConnected = false;
	
	private UserServiceTerminal userServiceTerminal;
	
	public EchoClient(String hostName, int port, Queue<String> messageQueue) {
		this.port = port;
		this.hostNames = new ArrayBlockingQueue<>(1);
		this.hostNames.add(hostName);
		this.messageQueue = messageQueue;
	}
	
	public EchoClient(Queue<String> hostNames, int port, Queue<String> messageQueue) {
		this.port = port;
		this.hostNames = hostNames;
		this.messageQueue = messageQueue;
	}
	
    public void startClient() throws IOException
    {
    	String hostName;
//    	System.out.println("hostNames: "+hostNames);
    	while((hostName = hostNames.poll()) != null && !isConnected) {
    		System.out.println("hostName: "+hostName);
    		try {
    			tryConnect(hostName);
    		} catch (Throwable t) {
    			t.printStackTrace();
    		}
    	}
    }
    
    private void tryConnect(String hostName) {
    	System.out.println("Try connect to "+hostName);
    	try ( 
	        	Socket echoSocket = new Socket(hostName, port);
	        	ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());
	        	ObjectInputStream in = 
	        			new ObjectInputStream(echoSocket.getInputStream());
	        ) {
    			out.writeObject("I'm connected");
    			System.out.println(hostName+" Status isConnected "+isConnected);
    			if(!isConnected) {
	    			isConnected = true;
	    			System.out.println(hostName+" Status isConnected "+isConnected);
		        	String message;
		        	try( BufferedReader stdIn = 
		        			new BufferedReader(
		        					new InputStreamReader(System.in)); ) {
			        	while((message = stdIn.readLine()) != null ) {
			        		if(Thread.interrupted()) break;
			        		
			        		System.out.println("-> "+message);
			        		
			        		userServiceTerminal = new UserServiceTerminal(stdIn);
			        		NetworkRequest networkRequest = userServiceTerminal.findByLoginAndPassword();
			        		
			        		out.writeObject(networkRequest);
			        		Object result = in.readObject();
			        		if(result != null) {
			        			System.out.println(hostName+" server response: "+result.toString()+" response class : "+result.getClass());
			        		} else {
			        			System.out.println(hostName+" server response: ");
			        		}
			        	}
		        	} catch(IOException ioe) {
		        		LOG.error(ioe.toString());
		        	}
    			}
	        } catch (UnknownHostException e) {
	        	LOG.error("Don't know about host "+hostName);
	        } catch (IOException ie) {
	        	LOG.error("Couldn't get I/O for the connection to "+hostName);
	        } catch (ClassNotFoundException e) {
	        	LOG.error(e.toString(), e);
			}
    }

	@Override
	public void run() {
		try {
			startClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
