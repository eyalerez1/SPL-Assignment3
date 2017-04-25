package MultipleClientServer;

import java.util.*;
import com.google.gson.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * The Class JsonReader.
 */
public class JsonReader {

	/**
	 * Reads the questions from the file.
	 *
	 * @return an array of 3 questions
	 */
	public static Question[] read3Questions() {
		Question[] ans=null;
		try{
			JsonParser parser = new JsonParser();
			FileReader f=new FileReader("src/MultipleClientServer/Json.json");
			JsonObject parsed =(JsonObject)(parser.parse(f));	
			JsonArray parsedQuestions=parsed.getAsJsonArray("questions");
			ArrayList<Question> questions=new ArrayList<Question>();
			for(int i=0; i<parsedQuestions.size(); i++) {
				questions.add(new Question(((JsonObject)(parsedQuestions.get(i))).get("questionText").getAsString(),((JsonObject)(parsedQuestions.get(i))).get("realAnswer").getAsString().toLowerCase()));
			}
			Collections.shuffle(questions);
			ans=new Question[3];
			for(int i=0; i<3; i++) {
				ans[i]=questions.get(i);
			}	
			return ans;
		}catch(FileNotFoundException  e){
			System.out.println("File not found");
		}
	 	catch(IOException ioe) {
	 		System.out.println("FileReader was not closed");
	 	}
		return ans;
	}

}
