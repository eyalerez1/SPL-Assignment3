package MultipleClientServer;

import java.io.*;
import java.net.*;

import protocol.*;

import protocol.ProtocolCallbackImpl;
import protocol.ServerProtocol;

/**
 * The Class ConnectionHandler.
 */
public class ConnectionHandler implements Runnable {
	
	/** The input bufferReadder. */
	private BufferedReader in;
	
	/** The output bufferReader. */
	private PrintWriter out;
	
	/** The client socket. */
	Socket clientSocket;
	
	/** The protocol. */
	ServerProtocol protocol;
	
	/** The base protocol. */
	ServerProtocol baseProtocol;
	
	/**
	 * Instantiates a new connection handler.
	 *
	 * @param acceptedSocket the accepted socket
	 * @param p the {@link ServerProtocol}
	 */
	public ConnectionHandler(Socket acceptedSocket, ServerProtocol p)
	{
		in = null;
		out = null;
		clientSocket = acceptedSocket;
		baseProtocol=p;
		protocol = baseProtocol;
		System.out.println("Accepted connection from client!");
		System.out.println("The client is from: " + acceptedSocket.getInetAddress() + ":" + acceptedSocket.getPort());
	}
	
	/**
	 * the run method
	 */
	public void run()
	{

		String msg;
		
		try {
			initialize();
		}
		catch (IOException e) {
			System.out.println("Error in initializing I/O");
		}

		try {
			process();
		} 
		catch (IOException e) {
			System.out.println("Error in I/O");
		} 
		
		System.out.println("Connection closed - bye bye...");
		close();

	}
	
	/**
	 * Process the message according to the protocol
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void process() throws IOException
	{
		String msg;
		
		while ((msg = in.readLine()) != null)
		{	
			protocol.processMessage(msg,new ProtocolCallbackImpl(this));		
			if (protocol.isEnd(msg)) {
				
				break;
			}
		}
	}
	
	/**
	 * Initialize buffers and starts listening
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void initialize() throws IOException {
		// Initialize I/O
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
		out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),"UTF-8"), true);
		System.out.println("I/O initialized");
	}
	
	/**
	 * Close the connection.
	 */
	public void close() {
		try {
			if (in != null)
			{
				in.close();
			}
			if (out != null)
			{
				out.close();
			}
			clientSocket.close();
		}
		catch (IOException e)
		{
			System.out.println("Exception in closing I/O");
		}
	}
	
	/**
	 * Send a  messagee.
	 *
	 * @param msg the message to send
	 */
	public void send(String msg) {
		try{			
			clientSocket.getOutputStream().write(msg.getBytes());
		}catch(IOException e){
			System.out.println("Error in I/O");
		}
	}
	
	/**
	 * Sets the protocol to the new protocol.
	 *
	 * @param protocol the new protocol
	 */
	public void setProtocol(ServerProtocol protocol) {
		this.protocol=protocol;
	}
	
	/**
	 * Reset protocol to the base protocol.
	 */
	public void resetProtocol() {
		protocol=baseProtocol;
	}
}