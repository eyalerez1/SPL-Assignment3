package MultipleClientServer;

/**
 * The Class PlayerAnswer.
 */
public class PlayerAnswer {
	
	/** The nick of the player. */
	private String nick;
	
	/** The answer of the player. */
	private String answer;
	
	/**
	 * Instantiates a new player answer.
	 *
	 * @param nick the nick of the player
	 * @param answer the answer of the player
	 */
	public PlayerAnswer(String nick, String answer) {
		this.nick = nick;
		this.answer = answer;
	}

	/**
	 * Gets the nick.
	 *
	 * @return the nick of the player
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Gets the answer.
	 *
	 * @return the answer of the player
	 */
	public String getAnswer() {
		return answer;
	}
}
