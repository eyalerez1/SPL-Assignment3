package MultipleClientServer;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.concurrent.*;

import protocol.BlufferProtocolFactory;
import protocol.ServerProtocol;
import protocol.TBGPFactory;

/**
 * The Class GameServer.
 */
public class GameServer {
	

	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException
	{
		ServerSocket gameServerSocket = null;
		// Get port
		int port = Integer.decode(args[0]).intValue();
		
		Database database=Database.getInstance();
	
		database.addGame("BLUFFER",new BlufferProtocolFactory());
		
		// Listen on port
		try {
			gameServerSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Couldn't listen on port " + port);
			System.exit(1);
		}
		
		System.out.println("Listening...");
		
		// Waiting for a client connection
		Socket gameClientSocket = null;
		Executor executor=Executors.newFixedThreadPool(20);
		
		while(true) {
			try {
				gameClientSocket = gameServerSocket.accept();
				ServerProtocol<String> p=(new TBGPFactory()).create();
				executor.execute(new ConnectionHandler(gameClientSocket, p));
			} catch (IOException e) {
				System.out.println("Failed to accept...");
				System.exit(1);
			}	
		}
	}
}
