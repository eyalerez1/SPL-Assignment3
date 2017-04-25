package MultipleClientServer;

import java.util.*; 

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import AsyncProtocol.AsyncProtocolCallbackImpl;
import protocol.ProtocolCallback;
import protocol.ProtocolCallbackImpl;
import protocol.ServerProtocolFactory;
import protocol.TBGPFactory;

import java.io.*;

/**
 * The Class GameRoom.
 */
public class GameRoom {
	
	/** The players. */
	protected LinkedList<Player> players;
	
	/** The room name. */
	protected String roomName;
	
	/** The number of players. */
	protected AtomicInteger numOfPlayers;
	
	/** indicates whether a game is active in the room. */
	protected boolean isActive;
	
	
	/**
	 * Instantiates a new game room.
	 *
	 * @param roomName the room name
	 */
	public GameRoom(String roomName) {
		this.players=new LinkedList<Player>();
		this.roomName = roomName;
		this.numOfPlayers = new AtomicInteger(0);
		this.isActive=false;
	}
	
	/**
	 * Instantiates a new game room.
	 *
	 * @param roomName the room name
	 * @param players the players
	 */
	public GameRoom(String roomName, LinkedList<Player> players) {
		this(roomName);
		this.players=players;
		this.isActive=true;
		this.numOfPlayers=new AtomicInteger(players.size());
	}

	/**
	 * Gets the room name.
	 *
	 * @return the room name
	 */
	public String getRoomName() {
		return roomName;
	}
	
	/**
	 * Gets the number of players.
	 *
	 * @return the number of players
	 */
	public AtomicInteger getNumberOfPlayers() {
		return numOfPlayers;
	}


	/**
	 * Checks if active.
	 *
	 * @return true, if active
	 */
	public boolean isActive() {
		return isActive;
	}
	
	/**
	 * Adds the player.
	 *
	 * @param nick the nick of the player
	 * @param callback the callback of the client
	 * @return true, if successful
	 */
	public synchronized boolean addPlayer(String nick, ProtocolCallback callback) {
		if(isActive) {
			return false;
		} else {
			if(findPlayerByNick(nick)==null) {
				players.add(new Player(nick,callback));
				numOfPlayers.incrementAndGet();
				return true;	
			}else {
				return false;
			}
		}
	}
	
	/**
	 * Removes the player.
	 *
	 * @param nick the nick of the player
	 * @return true, if successful
	 */
	public synchronized boolean removePlayer(String nick) {
		if(isActive) {
			return false;
		} else {
			Player player=findPlayerByNick(nick);
			if(player!=null) {
				players.remove(player);
				numOfPlayers.decrementAndGet();
				return true;
			} else {
				return false;
			}	
		}
	}

	/**
	 * Start game.
	 */
	public void startGame() {
		isActive=true;
	}
	

	/**
	 * Ends the game and resets the protocol to the base protocol
	 */
	public void endGame() {
		for(Player p : players) {
			if(p.getCallback() instanceof ProtocolCallbackImpl) {	
				((ProtocolCallbackImpl)(p.getCallback())).resetProtocol();
			}else {
				((AsyncProtocolCallbackImpl)(p.getCallback())).resetProtocol();
			}
		}
	}
	
	/**
	 * Find player by nick.
	 *
	 * @param nick the nick of the player
	 * @return the player
	 */
	protected Player findPlayerByNick(String nick) {
		for(Player p : players) {
			if(p.getNick().equals(nick)) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Send message.
	 *
	 * @param msg the message to send
	 * @param nick the nick of the sender
	 */
	public void sendMessage(String msg, String nick) {
		for(Player p : players) {
			if(!(p.getNick().equals(nick))) {
				try {
					p.getCallback().sendMessage(msg);
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * Change game.
	 *
	 * @param factory the factory to change to
	 */
	public void changeGame(ServerProtocolFactory factory) {
		
		for(int i=1 ; i<players.size() ; i++) {
			if(players.get(i).getCallback() instanceof ProtocolCallbackImpl) {	
				((ProtocolCallbackImpl)(players.get(i).getCallback())).changeGame(factory.create(players.get(i).getNick(), roomName, false));
			}else {
				((AsyncProtocolCallbackImpl)(players.get(i).getCallback())).changeGame(factory.create(players.get(i).getNick(), roomName, false));
			}
		}
		if(players.get(0).getCallback() instanceof ProtocolCallbackImpl) {	
			((ProtocolCallbackImpl)(players.get(0).getCallback())).changeGame(factory.create(players.get(0).getNick(), roomName, true));
		}else {
			((AsyncProtocolCallbackImpl)(players.get(0).getCallback())).changeGame(factory.create(players.get(0).getNick(), roomName, true));
		}	}
	
	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public LinkedList<Player> getPlayers() {
		return players;
	}
}
