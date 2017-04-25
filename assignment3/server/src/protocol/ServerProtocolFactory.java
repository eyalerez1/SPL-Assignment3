package protocol;

import java.io.*;
import java.net.*;

import MultipleClientServer.GameRoom;


/**
 * A factory for creating ServerProtocol objects.
 *
 * @param <T> the generic type
 */
public interface ServerProtocolFactory<T> {
   
   /**
    * Creates the {@link ServerProtocol}.
    *
    * @return the server protocol
    */
   ServerProtocol create();
   
   /**
    * Creates the {@link ServerProtocol}.
    *
    * @param nick the nick
    * @param room the room
    * @param start if the game should star
    * @return the server protocol
    */
   public ServerProtocol create(String nick,String room, boolean start);
}