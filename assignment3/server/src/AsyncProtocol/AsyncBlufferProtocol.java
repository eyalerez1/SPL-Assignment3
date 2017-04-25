package AsyncProtocol;

import protocol.BlufferProtocol;
import protocol.ProtocolCallback;
import MultipleClientServer.GameRoom;
import Tokenizer.StringMessage;

public class AsyncBlufferProtocol extends BlufferProtocol implements AsyncServerProtocol<String> {
	
	private boolean _shouldClose;
	private boolean _connectionTerminated;
	
	public AsyncBlufferProtocol(String nick,String room, boolean start) {
		super(nick,room,start);
		_shouldClose = false;
		_connectionTerminated = false;
	}
	
	public void processMessage(String msg , ProtocolCallback <String > callback ) {
		if(!_connectionTerminated) {
			super.processMessage(msg, callback);
		}
	}
	/**
	 * Is the protocol in a closing state?.
	 * When a protocol is in a closing state, it's handler should write out all pending data, 
	 * and close the connection.
	 * @return true if the protocol is in closing state.
	 */
	public boolean shouldClose() {
		return this._shouldClose;
	}

	/**
	 * Indicate to the protocol that the client disconnected.
	 */
	public void connectionTerminated() {
		this._connectionTerminated = true;
	}
	
	 void close() {
		 _shouldClose=true;
	 }
}
