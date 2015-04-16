package kz.aksay.polygraph.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kz.aksay.polygraph.network.db.DBManager;

public class ServerSocketThread implements Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(ServerSocketThread.class);
	
	private Socket clientSocket;
	private DBManager dbManager;
	private int portNumber;
	
	private ObjectOutputStream out;
	private ObjectInputStream ois;
	
	public ServerSocketThread(Socket clientSocket, DBManager dbManager, int portNumber) throws IOException {
		System.out.println("ServerSocketThread clientSocket is Closed =  "+clientSocket.isClosed());
		this.clientSocket = clientSocket;
		this.dbManager = dbManager;
		this.portNumber = portNumber;

	}

	@Override
	public void run() {
		System.out.println("ServerSocketThread.run clientSocket is Closed = "+clientSocket.isClosed());
		try
		 (		
				 ObjectInputStream ois = new ObjectInputStream(this.clientSocket.getInputStream());
				 ObjectOutputStream out = new ObjectOutputStream( this.clientSocket.getOutputStream());
		)
		{
			System.out.println("Client connected "+clientSocket.getInetAddress());
			
			Object inputObject;
			
			out.writeObject("Nice to meet you!");
			
			while( !Thread.interrupted() && (inputObject = ois.readObject()) != null ) {
				System.out.println("inpputObject "+inputObject);
				Serializable outputObject = null;
				if( inputObject instanceof NetworkRequest ) {
					NetworkRequest networkRequest = (NetworkRequest) inputObject;
					outputObject = dbManager.handleRequest( networkRequest );					
				}
				out.writeObject( outputObject );
			}
			
		} catch (IOException e) {
			LOG.error("Exception caught when trying to listen on port "
					+ portNumber + " or listening for a connection");
			LOG.error(e.toString(), e);
		} catch (ClassNotFoundException e) {
			LOG.error(e.toString(), e);
		} finally {
			try {
				System.out.println("Client disconnected "+clientSocket.getInetAddress());
				clientSocket.close();
			} catch (IOException e) {
				LOG.error(e.toString(), e);
			}
		}
	}
}
