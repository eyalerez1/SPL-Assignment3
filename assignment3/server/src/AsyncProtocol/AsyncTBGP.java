package AsyncProtocol;

import MultipleClientServer.Database;
import MultipleClientServer.GameRoom;
import protocol.ProtocolCallback;
import protocol.TBGP;

public class AsyncTBGP extends TBGP implements AsyncServerProtocol<String> {

	private boolean _shouldClose;
	private boolean _connectionTerminated;
	
	public AsyncTBGP(){
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
