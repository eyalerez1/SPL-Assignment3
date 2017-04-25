package protocol;

import MultipleClientServer.GameRoom;

/**
 * A factory for creating TBGP objects.
 */
public class TBGPFactory implements ServerProtocolFactory {
	
	/**
	 * a method to create a new {@link ServerProtocol}
	 * should not be used.
	 * 
	 *  @return the server protocol
	 */
	@Override
	public ServerProtocol create() {
		return new TBGP();
	}
	
	/**
	 * a method to create a new {@link ServerProtocol}
	 * 
	 * @param nick the nick of the client
	 * @param room the room name
	 * @param start true if the game should start
	 * @return the server protocol
	 */
	public ServerProtocol create(String nick, String room,  boolean start) {
		return create();
	}

}
