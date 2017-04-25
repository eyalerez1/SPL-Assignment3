package protocol;

import java.io.*; 
import java.net.*;
import java.util.*;

import MultipleClientServer.BlufferGameRoom;
import MultipleClientServer.Database;
import MultipleClientServer.GameRoom;
import MultipleClientServer.GameServer;

/**
 * The Class BlufferProtocol.
 */
public class BlufferProtocol extends TBGP implements ServerProtocol<String> {
	
	/** The nick of the player. */
	private String nick;
	
	/** The room. */
	private String room;
	
	/** The database. */
	private Database database = Database.getInstance();
	
	/** The expected message. */
	private String expectedMessage;
	
	/**
	 * Instantiates a new bluffer protocol.
	 *
	 * @param nick the nick of the player
	 * @param room the room
	 * @param start the start
	 */
	public BlufferProtocol(String nick, String room, boolean start) {
		expectedMessage="TXTRESP";
		this.nick=nick;
		this.room=room;
		if(start) {
			database.getGameRoom(room).startGame();
		}
				
	}
	
	/**
	 * process message
	 * 
	 * @param msg the message to process
	 * @param callbak the {@link ProtocolCallback} used to send the result
	 */
	@Override
	public void processMessage(String msg , ProtocolCallback<String> callback ){
		if(msg.length()==0) {
			return;
		}
		String response=null;
		String[] msgArray=msg.split(" ");
		String msgType=msgArray[0];
		if(msgArray.length>1) {
			msg=msg.substring(msgType.length()+1);			
		}
		switch(msgType) {
		case "NICK":
			if(nick!=null){
				response="SYSMSG NICK REJECTED You allready have a name.";
			}else {
				if(database.containsPlayer(msg)) {
					response="SYSMSG NICK REJECTED The name allready exists. Please send a new NICK message with a different name.";
				}else {
					database.addNick(msg);
					this.nick=msg;
					response="SYSMSG NICK ACCEPTED";
				}
			}
			break;
		case "JOIN":
			response="SYSMSG JOIN REJECTED You can't get out of this room at the moment.";
			break;
		case "MSG":
			String message="MSG "+nick+" "+msg;
			database.getGameRoom(room).sendMessage(message, nick);
			response="SYSMSG MSG ACCEPTED";
			break;
		case "LISTGAMES":
			HashMap<String,ServerProtocolFactory> gameList=database.getGameList();
			for(int i=0;i<gameList.size(); i++) {
				response+=gameList.get(i)+", ";
			}
			response.substring(0, response.length()-2);
			response="SYSMSG LISTGAMES ACCEPTED "+response;
			break;
		case "STARTGAME":
			if(expectedMessage!=null) {
				response="SYSMSG STARTGAME REJECTED The game has allready started";
			}
			break;
		case "TXTRESP":
			if(expectedMessage=="TXTRESP") {
				if(((BlufferGameRoom)(database.getGameRoom(room))).addPlayerAnswer(nick, msg)) {
					expectedMessage="SELECTRESP";
				}
			}else {
				response="SYSMSG TXTRESP REJECTED";
			}
			break;
		case "SELECTRESP":
			if(expectedMessage=="SELECTRESP") {
				if(msg.compareTo("0")>=0 & msg.compareTo(database.getGameRoom(room).getNumberOfPlayers().toString())<=0) {
					((BlufferGameRoom)(database.getGameRoom(room))).addPlayerChoiceAnswer(nick, msg);
					if(((BlufferGameRoom)(database.getGameRoom(room))).getRoundNumber()==4) {
						expectedMessage=null;
					}else {
						expectedMessage="TXTRESP";
					}
				}else {
					response="SYSMSG SELECTRESP REJECTED Choose a valid SELECTRESP.";
				}
			}else {
				response="SYSMSG SELECTRESP REJECTED";
			}
			break;
		case "QUIT":
			if(room==null || database.getGameRoom(room).removePlayer(nick)) {
				response="SYSMSG QUIT ACCEPTED";
				database.removePlayer(nick);
			}else {
				response="SYSMSG QUIT REJECTED You are in the middle of a game. Don't be a chicken, finish the game and then try to QUIT again.";
			}
			close();
			break;
			default:
				response="SYSMSG "+msgType+" UNDEFINED";
		}
		if(response!=null) {
			try {					
				callback.sendMessage(response);
			} catch(IOException e) {
				System.out.println("Error in I/O");
			}
		}
	}
	
	/**
	 * @return true, if the client sent a QUIT message and it was accepted.
	 */
	public boolean isEnd( String msg )
	{
		return msg.equals("QUIT") && (room==null || !(database.getGameRoom(room).isActive()));
	}
	
	/**
	 * more actions in order to gracefully terminate the connection
	 */
	void close() {}
}
