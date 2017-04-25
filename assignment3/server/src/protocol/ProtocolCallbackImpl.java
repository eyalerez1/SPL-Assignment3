package protocol;

import java.io.IOException;
import java.net.*;

import MultipleClientServer.*;

/**
 * The Class ProtocolCallbackImpl.
 */
public class ProtocolCallbackImpl implements ProtocolCallback<String> {
	
	/** The connection handler. */
	private ConnectionHandler connectionHandler;
	
	/**
	 * Instantiates a new protocol callback impl.
	 *
	 * @param connectionHandler the connection handler
	 */
	public ProtocolCallbackImpl(ConnectionHandler connectionHandler) {
		this.connectionHandler=connectionHandler;
	}

	/**
	 * sends a message to the client using {@link ConnectionHandler}
	 * 
	 * @param msg the message
	 */
	@Override
	public void sendMessage(String msg) throws IOException {
		msg+="\n";
		connectionHandler.send(msg);
	}
	
	/**
	 * Change game.
	 *
	 * @param protocol the protocol
	 */
	public void changeGame(ServerProtocol protocol) {
		connectionHandler.setProtocol(protocol);
	}
	
	/**
	 * Reset protocol.
	 */
	public void resetProtocol() {
		connectionHandler.resetProtocol();
	}

}
