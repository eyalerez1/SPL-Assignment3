package protocol;

import java.io.*; 
import java.net.*;
import java.util.*;

import MultipleClientServer.BlufferGameRoom;
import MultipleClientServer.Database;
import MultipleClientServer.GameRoom;
import MultipleClientServer.GameServer;
import MultipleClientServer.Player;

/**
 * The Class TBGP.
 */
public class TBGP implements ServerProtocol<String>{

	/** The nick. */
	private String nick;
	
	/** The room. */
	private String room=null;
	
	/** The database. */
	protected Database database = Database.getInstance();
	
	/** The expected message. */
	private String expectedMessage=null;

	/**
	 * process message
	 * 
	 * @param msg the message to process
	 * @param callbak the {@link ProtocolCallback} used to send the result
	 */
	public void processMessage(String msg , ProtocolCallback <String > callback ){
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
			if(nick==null) {
				response="SYSMSG JOIN REJECTED You do not have a name. Please choose one.";
			} else {
				if(room!=null){
					if(room.equals(msg)) {
						response="SYSMSG JOIN REJECTED You are allready in this room.";
					}else{
						if(!(database.getGameRoom(room).removePlayer(nick))) {
							response="SYSMSG JOIN REJECTED You can't get out of this room at the moment.";
						}else {
							room=null;
						}
					}
				}
				if(room==null) {
					boolean acceptedToGameRoom=database.getGameRoom(msg).addPlayer(nick, callback);
					if(acceptedToGameRoom){
						room=msg;
						response="SYSMSG JOIN ACCEPTED";
					}else {
						response="SYSMSG JOIN REJECTED There is an active game in progress. Please try joining the room later.";
					}				
				}
			}
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
			if(expectedMessage==null) {
				if(room==null) {
					response="SYSMSG STARTGAME REJECTED You are not in a room, so you can not start a game.";
				} else {
					ServerProtocolFactory factory=this.database.getServerProtocolFactory(msg);
					if(factory!=null) {
						try {					
							callback.sendMessage("SYSMSG STARTGAME ACCEPTED");
						} catch(IOException e) {
							System.out.println("Error in I/O");
						}
						database.updateGameRoom(room,new BlufferGameRoom(room,database.getGameRoom(room).getPlayers()));
						database.getGameRoom(room).changeGame(factory);
					}else {
						response="SYSMSG "+msgType+" "+msg+" UNDEFINED This game does not exist.";
					}
				}	
			} else {
				response="SYSMSG STARTGAME REJECTED The game has allready started";
			}
			break;
		case "QUIT":
			if(room==null || database.getGameRoom(room).removePlayer(nick)) {
				database.removePlayer(nick);
				response="SYSMSG QUIT ACCEPTED";
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


