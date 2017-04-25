package AsyncProtocol;

import MultipleClientServer.GameRoom; 
import protocol.ServerProtocolFactory;
import protocol.BlufferProtocol;
import protocol.ServerProtocol;

/**
 * A factory for creating AsyncBlufferProtocol objects.
 */
public class AsyncBlufferProtocolFactory implements ServerProtocolFactory {

	/**
	 * a method to create a new {@link AsyncServerProtocol}
	 * should not be used.
	 * 
	 *  @return the server protocol
	 */
	@Override
	public ServerProtocol create() {
		return new AsyncBlufferProtocol(null, null, false);
	}

	/**
	 * a method to create a new {@link AsyncServerProtocol}
	 * 
	 * @param nick the nick of the client
	 * @param room the room name
	 * @param start true if the game should start
	 * @return the server protocol
	 */
	public ServerProtocol create(String nick, String room, boolean start) {
		return new AsyncBlufferProtocol(nick, room, start);
	}

}
