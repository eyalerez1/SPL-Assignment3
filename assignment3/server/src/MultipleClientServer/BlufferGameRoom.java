package MultipleClientServer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import protocol.ProtocolCallback;
import protocol.ProtocolCallbackImpl;
import protocol.ServerProtocolFactory;

/**
 * The Class BlufferGameRoom used by the {@link BlufferProtocol}
 */
public class BlufferGameRoom extends GameRoom{
	
	/** The questions of the game */
	private Question[] questions;
	
	/** counts number of answers for each question */
	private AtomicInteger counter;
	
	/** The players answers. */
	private LinkedList<PlayerAnswer> playersAnswers;
	
	/** The round number. */
	private AtomicInteger roundNumber;

	
	
	/**
	 * Instantiates a new bluffer game room.
	 *
	 * @param roomName the room name
	 * @param players the players
	 */
	public BlufferGameRoom(String roomName, LinkedList<Player> players) {
		super(roomName,players);
		this.questions=new Question[3];
		questions=JsonReader.read3Questions();
		this.playersAnswers=new LinkedList<PlayerAnswer>();
		counter=new AtomicInteger(0);
		roundNumber=new AtomicInteger(1);
	}



	/**
	 * Gets the round number.
	 *
	 * @return the round number
	 */
	public int getRoundNumber() {
		return roundNumber.get();
	}
	
	/**
	 * starts a new bluffer game
	 */
	@Override
	public void startGame() {
		isActive=true;
		if(questions==null) {
			for(Player p : players) {
				try {
					p.getCallback().sendMessage("GAMEMSG Game failed. could'nt load questions");
				} catch (IOException e) {}
			}
		}else {
			nextRound();
		}
	}
	


	/**
	 * Adds the player answer.
	 *
	 * @param nick the nick of the player
	 * @param answer the answer of the player
	 * @return true, if successful
	 */
	public synchronized boolean addPlayerAnswer(String nick, String answer) {
		answer=answer.toLowerCase();
		boolean accepted=false;
		if(answer.equals(questions[roundNumber.get()-1].getRealAnswer())) {
			try {				
				findPlayerByNick(nick).getCallback().sendMessage("SYSMSG TXTRESP REJECTED This is the real answer, GOOD JOB! Please invent a different answer and maybe gain more points!");
			} catch (IOException e) {}
		} else{
			if(containAnswer(answer)) {
				try {				
					findPlayerByNick(nick).getCallback().sendMessage("SYSMSG TXTRESP REJECTED This answer allready exists. Please invent a different answer.");
				} catch (IOException e) {}
			}else{
				randomAddToList(new PlayerAnswer(nick,answer));
				accepted=true;
				try {				
					findPlayerByNick(nick).getCallback().sendMessage("SYSMSG TXTRESP ACCEPTED");
				} catch (IOException e) {}
				counter.incrementAndGet();
				if(counter.get()==numOfPlayers.get()){
					counter.set(0);
					sendASKCHOICES();
				}	
			}
		}
		return accepted;
	}
	
	/**
	 * Contain answer.
	 *
	 * @param answer the answer to find
	 * @return true, the playersAnswers list contains answer
	 */
	private boolean containAnswer(String answer) {
		for(PlayerAnswer ans: playersAnswers) {
			if(ans.getAnswer().equals(answer)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * send an ASKCHOICE
	 */
	private void sendASKCHOICES() {
		int correctAnsPlace=(int)(Math.random()*(numOfPlayers.get()+1));
		playersAnswers.add(correctAnsPlace,new PlayerAnswer("realAnswer",questions[roundNumber.get()-1].getRealAnswer()));
		String askChoices="";
		int j=0;
		for(PlayerAnswer ans : playersAnswers) {
			askChoices=askChoices+j+"."+ans.getAnswer()+" ";
			j++;
		}
		for(Player p : players) {
			try {				
				p.getCallback().sendMessage("ASKCHOICES "+askChoices);
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Random add to list of players invented answer
	 *
	 * @param ans the answer
	 */
	private void randomAddToList(PlayerAnswer ans) {
		int place=(int)(Math.random()*2);
		if(place==0) {
			playersAnswers.addFirst(ans);
		} else {
			playersAnswers.addLast(ans);
		}
	}
	
	/**
	 * Adds the player choice answer.
	 *
	 * @param nick the nick of the player
	 * @param answerNumber the answer number
	 */
	public void addPlayerChoiceAnswer(String nick, String answerNumber) {
		counter.incrementAndGet();
		PlayerAnswer answer=playersAnswers.get((int)(answerNumber.toCharArray()[0])-48);
		Player p=findPlayerByNick(nick);
		if("realAnswer".equals(answer.getNick())) {
			p.setScore(15);
			p.setRoundAnswer("correct!");
		} else {
			findPlayerByNick(answer.getNick()).setScore(5);
			p.setRoundAnswer("wrong!");
		}
		
		if(counter.get()==numOfPlayers.get()) {
			counter.set(0);
			sendRonudSummary();
		}
	}
	
	/**
	 * Send ronud summary.
	 */
	private void sendRonudSummary() {
		for(Player p : players) {
			try {
				p.getCallback().sendMessage("GAMEMSG The correct answer is: "+questions[roundNumber.get()-1].getRealAnswer());
				p.getCallback().sendMessage(p.getRoundAnswer()+" +"+p.getRoundScore()+"pts");
			} catch (IOException e) {}
		}
		if(roundNumber.incrementAndGet()==4) {
			sendGameSummary();
		} else {
			nextRound();
		}
	}
	
	/**
	 * starts the next round
	 */
	private void nextRound() {
		playersAnswers= new LinkedList<PlayerAnswer>();
		for(Player p : players) {
			p.resetRoundScore();
			try {
				p.getCallback().sendMessage("ASKTXT "+questions[roundNumber.get()-1].getQuestionText());
			} catch (IOException e) {}
		
		}
	}
	
	/**
	 * Send game summary.
	 * and ends the game
	 */
	private void sendGameSummary() {
		String gameSummary="GAMEMSG Summary: "; 
		for(Player p : players) {
			gameSummary+=p.getNick()+": "+p.getTotalScore()+"pts, ";
		}
		gameSummary.substring(0, gameSummary.length()-2);
		for(Player p : players) {
			try {
				p.getCallback().sendMessage(gameSummary);
			} catch (IOException e) {}
			p.resetScore();
		}
		isActive=false;
		playersAnswers= new LinkedList<PlayerAnswer>();
		endGame();
	}
}
