package MultipleClientServer;

import java.util.*;
import java.util.concurrent.*;

import protocol.ServerProtocolFactory;

/**
 * The Class Database of the Server.
 */
public class Database {
	
	/** The nicks. */
	private LinkedList<String> nicks;
	
	/** The game rooms. */
	private ConcurrentHashMap<String,GameRoom> gameRooms;
	
	/** The game list. */
	private HashMap<String,ServerProtocolFactory> gameList;
	
	/**
	 * The Class DatabaseHolder.
	 */
	private static class DatabaseHolder {
		
		/** The instance. */
		private static Database instance=new Database();
	}
	
	/**
	 * Instantiates a new database.
	 */
	protected Database() {
		this.nicks= new LinkedList<String>();
		this.gameRooms= new ConcurrentHashMap<String,GameRoom>();
		this.gameList=new HashMap<String,ServerProtocolFactory>();
	}
	
	/**
	 * Gets the single instance of Database.
	 *
	 * @return single instance of Database
	 */
	public static Database getInstance() {
		return DatabaseHolder.instance;
	}

	/**
	 * Contains player.
	 *
	 * @param nick the nick
	 * @return true, if successful
	 */
	public synchronized boolean containsPlayer(String nick) {
		return nicks.contains(nick);
	}

	/**
	 * Adds the nick.
	 *
	 * @param nick the nick of the player
	 */
	public synchronized void addNick(String nick) {
		this.nicks.add(nick);
	}

	/**
	 * Gets the game room.
	 * creates a new one if it does not exist
	 *
	 * @param roomName the room name
	 * @return the game room
	 */
	public synchronized GameRoom getGameRoom(String roomName) {
		GameRoom room= gameRooms.get(roomName);
		if(room==null) {
			room= new GameRoom(roomName);
			gameRooms.put(roomName, room);
		}
		return room;
	}
	
	/**
	 * Update game room.
	 *
	 * @param roomName the room name
	 * @param gameRoom the game room
	 */
	public synchronized void updateGameRoom(String roomName, GameRoom gameRoom) {
		gameRooms.put(roomName, gameRoom);
	}
	
	/**
	 * Removes the player.
	 *
	 * @param name the name of the player
	 */
	public void removePlayer(String name) {
		nicks.remove(name);
	}
	
	/**
	 * Gets the server protocol factory.
	 *
	 * @param gameName the game name
	 * @return the server protocol factory
	 */
	public ServerProtocolFactory getServerProtocolFactory(String gameName) {
		return gameList.get(gameName);
	}
	
	/**
	 * Adds a game name and its {@link ServerProtocolFactory}.
	 *
	 * @param gameName the game name
	 * @param factory the factory
	 */
	public void addGame(String gameName, ServerProtocolFactory factory) {
		gameList.put(gameName, factory);
	}
	
	/**
	 * Gets the game list.
	 *
	 * @return the game list
	 */
	public HashMap<String,ServerProtocolFactory> getGameList() {
		return gameList;
	}
}
