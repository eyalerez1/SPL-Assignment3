package protocol;

import java.io.*;
import java.net.*;

import AsyncProtocol.AsyncServerProtocol;
import MultipleClientServer.GameRoom;

/**
 * A factory for creating BlufferProtocol objects.
 */
public class BlufferProtocolFactory implements ServerProtocolFactory {
	
	/**
	 * a method to create a new {@link ServerProtocol}
	 * should not be used.
	 * 
	 *  @return the server protocol
	 */
	public ServerProtocol create(){
		return new BlufferProtocol(null, null,false);
	}
	
	/**
	 * a method to create a new {@link ServerProtocol}
	 * 
	 * @param nick the nick of the client
	 * @param room the room name
	 * @param start true if the game should start
	 * @return the server protocol
	 */
	public BlufferProtocol create(String nick, String room, boolean start) {
		return new BlufferProtocol(nick,room, start);
	}
}
