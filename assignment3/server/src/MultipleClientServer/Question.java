package MultipleClientServer;

/**
 * The Class Question. wraps the system's question and its real answer.
 */
public class Question {
	
	/** The question. */
	private String questionText;
	
	/** The real answer. */
	private String realAnswer;
	
	/**
	 * Instantiates a new question.
	 *
	 * @param questionText the question
	 * @param realAnswer the real answer
	 */
	public Question(String questionText, String realAnswer) {
		this.questionText = questionText;
		this.realAnswer = realAnswer;
	}

	/**
	 * Gets the question.
	 *
	 * @return the question
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * Gets the real answer.
	 *
	 * @return the real answer
	 */
	public String getRealAnswer() {
		return realAnswer;
	}
	
	
	
	

}
