package AsyncProtocol;

import java.nio.ByteBuffer; 
import java.nio.charset.CharacterCodingException;

import Reactor.ProtocolTask;
import protocol.ProtocolCallback;
import protocol.ServerProtocol;


/**
 * The Class AsyncProtocolCallbackImpl.
 *
 * @param <T> the generic type
 */
public class AsyncProtocolCallbackImpl<T> implements ProtocolCallback<T> {
	
	/** The protocol task. */
	private ProtocolTask<T> protocolTask;
	
	/**
	 * Instantiates a new async protocol callback impl.
	 *
	 * @param protocolTask the protocol task
	 */
	public AsyncProtocolCallbackImpl(ProtocolTask<T> protocolTask) {
		this.protocolTask=protocolTask;
	}

	/**
	 * sends a message to the client using {@link protocolTask}
	 * 
	 * @param msg the message
	 */
	public void sendMessage ( T msg ) throws java . io . IOException {
       protocolTask.addResponse(msg);
	}	

	/**
	 * Changes the game, by changing the game protocol
	 *
	 * @param protocol the game protocol
	 */
	public void changeGame(ServerProtocol protocol) {
		protocolTask.setProtocol(protocol);
	}
	
	/**
	 * Reset protocol to the base protocol {@link TBGP}
	 */
	public void resetProtocol() {
		protocolTask.resetProtocol();
	}
}
