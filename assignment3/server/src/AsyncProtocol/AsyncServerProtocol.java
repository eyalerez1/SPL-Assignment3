package AsyncProtocol;
/* *
* A protocol that describes the behavior of the server .
*/

import protocol.ServerProtocol;
public interface AsyncServerProtocol <T > extends ServerProtocol <T> {
	
	/* *
	* Is the protocol in a closing state ?.
	* When a protocol is in a closing state , it ’s handler should write
	out all pending data ,
	* and close the connection .
	*
	* @return true if the protocol is in closing state .
	*/
	boolean shouldClose () ;

	/* *
	* Indicate to the protocol that the client disconnected .*/
	void connectionTerminated() ;

}