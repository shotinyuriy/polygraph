package kz.aksay.polygraph.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import kz.aksay.polygraph.network.db.DBManager;
import kz.aksay.polygraph.util.ContextUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EchoServer implements Runnable {
	
	private int port;
	private ApplicationContext context;
	private DBManager dbManager;
	
	public EchoServer(int port) {
		this.port = port;
	}
	
	public void startServer() throws IOException {
				
		int portNumber = port;
		
		System.out.println("Server started at port "+portNumber);
		
		try (
				ServerSocket serverSocket = new ServerSocket( portNumber );
				
				Socket clientSocket = serverSocket.accept();
				
				ObjectOutputStream out = 
						new ObjectOutputStream( clientSocket.getOutputStream());
				
				/*BufferedReader in = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));*/
				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
		) {
			
			Object inputObject;
			
			while( !Thread.interrupted() && (inputObject = ois.readObject()) != null ) {
				
				Serializable outputObject = null;
				if( inputObject instanceof NetworkRequest ) {
					NetworkRequest networkRequest = (NetworkRequest) inputObject;
					outputObject = dbManager.handleRequest( networkRequest );					
				}
				out.writeObject( outputObject );
			}
			
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
					+ portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			initializeContext();
			startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeContext() {
//		ApplicationContext parentContext = ContextUtils.getApplicationContext();
		ClassPathXmlApplicationContext netContext 
			= new ClassPathXmlApplicationContext("META-INF/server-context.xml");
//		netContext.setParent(parentContext);
		netContext.refresh();
		
		context = netContext;
		dbManager = context.getBean(DBManager.class);
	}
}
