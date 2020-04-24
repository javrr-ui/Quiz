///////////////////////////////////////////////////////////////////////////////                  
// Title:            Quiz
// Files:            
// Semester:         COP3337 Fall 2015
//
// Author:           3587814
// Lecturer's Name:  Prof. Maria Charters
//
// Description of Program’s Functionality: 
//////////////////////////// 80 columns wide/////////////////////////////////
package quiz;

import java.util.ArrayList;

/**
 * A question with multiple choices.
 */
public class MultipleChoiceQuestion extends Question {

    private ArrayList<String> choices;

    /**
     * Constructs a multiple choice question with no choices.
     */
    public MultipleChoiceQuestion(String vettedness) {
        super(vettedness);
        choices = new ArrayList<>();
    }

    /**
     * Adds an answer choice to this question.
     *
     * @param choice the choice to add
     * @param correct true if this is the correct choice, false otherwise
     */
    public void setChoice(String choice, boolean correct) {
        choices.add(choice);
        if (correct) {
            // Convert choices.size() to string
            String choiceString = "" + (choices.size());
            setAnswer(choiceString);
        }
    }

    /**
     * Sets the correct answer. A number in a string.
     *
     * @param answer a number in a string that corresponds to the answers place
     * in the choices arrayList
     */
    @Override
    public void setAnswer(String answer) {
        this.answer = answer;
//        int correctAnswer = choices.indexOf(answer);
    }

    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public double checkQuestionProvidingAnswer(final String answer) {
        userAnswer = answer.replaceAll("\\s+", "");
        return checkQuestion();
    }

    /**
     * Returns a string with the question text and choices
     *
     * @return string with question text and choice
     */
    @Override
    public String display() {

        String display = text + "\n";

        for (int i = 0; i < choices.size(); i++) {
            int choiceNumber = i + 1;
            display = display.concat(choiceNumber + ": " + choices.get(i) + "\n");
        }

        return display;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    double getMaxPoints() {
        return 1.0;
    }

}
