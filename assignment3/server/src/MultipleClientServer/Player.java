package MultipleClientServer;

import protocol.ProtocolCallback;
import protocol.ProtocolCallbackImpl;

/**
 * The Class Player.
 */
public class Player {
	
	/** The nick. */
	private String nick;
	
	/** The callback. */
	private ProtocolCallback callback;
	
	/** The round answer. */
	private String roundAnswer;
	
	/** The round score. */
	private int roundScore;
	
	/** The total score. */
	private int totalScore;
	
	
	/**
	 * Instantiates a new player.
	 *
	 * @param nick the nick
	 * @param callback the callback
	 */
	public Player(String nick, ProtocolCallback callback) {
		this.nick = nick;
		this.callback = callback;
		this.roundScore=0;
		this.totalScore=0;
		roundAnswer="";
	}


	/**
	 * Gets the nick of the player.
	 *
	 * @return the nick of the player
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Gets the callback of the player.
	 *
	 * @return the callback of the player
	 */
	public ProtocolCallback getCallback() {
		return callback;
	}


	/**
	 * Gets the round score.
	 *
	 * @return the round score
	 */
	public int getRoundScore() {
		return roundScore;
	}

	/**
	 * Reset round score.
	 */
	public void resetRoundScore() {
		this.roundScore=0;
	}
	
	/**
	 * Reset total and round score.
	 */
	public void resetScore() {
		this.roundScore=0;
		this.totalScore=0;
	}

	/**
	 * Sets the score.
	 *
	 * @param roundScore the new score
	 */
	public synchronized void setScore(int roundScore) {
		this.roundScore += roundScore;
		this.totalScore += roundScore;
	}


	/**
	 * Gets the total score.
	 *
	 * @return the total score
	 */
	public int getTotalScore() {
		return totalScore;
	}


	/**
	 * Gets the round answer.
	 *
	 * @return the round answer
	 */
	public String getRoundAnswer() {
		return roundAnswer;
	}


	/**
	 * Sets the round answer.
	 *
	 * @param roundAnswer the new round answer
	 */
	public void setRoundAnswer(String roundAnswer) {
		this.roundAnswer = roundAnswer;
	}

	
	
	
}
