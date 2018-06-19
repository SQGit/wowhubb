package vineture.wowhubb.data;

/**
 * Created by Salman on 23-12-2017.
 */

public class Faq {
    String question, answer;


    public Faq(String question, String answer) {
        this.question = question;
        this.answer = answer;

    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}
