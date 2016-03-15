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
import java.util.Scanner;

/**
 * A question with multiple choices.
 */
public class MultipleChoiceQuestion extends Question {

    private ArrayList<String> choices;
    private int correctAnswer;

    /**
     * Constructs a multiple choice question with no choices.
     */
    public MultipleChoiceQuestion(String vettedness) {
        super(vettedness);
        choices = new ArrayList<String>();
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
            String choiceString = "" + choices.size();
            setAnswer(choiceString);
        }
    }

    /**
     * Sets the correct answer. A number in a string.
     * @param answer a number in a string that corresponds to the answers place in the choices arrayList
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
//    /**
//     * Takes an answer from input
//     */
//    public boolean takeAnswer() {
//        String input;
//        System.out.println("Enter the number of the answer");
//        Scanner in = new Scanner(System.in); //Create new scanner for user input
//        input = in.nextLine();
//        return checkAnswer(input);
//    }

    public double checkQuestion(String answer) {
        answer = answer.replaceAll("\\s+","");
        if(this.answer.equals(answer)){
            return 1.0;
        } else {
            return 0.0;
        }
    }

    /**
     * Returns a string with the question text and choices
     * @return string with question text and choice
     */
    public String display() {
        
        String display = text+"\n";
        
        for (int i = 0; i < choices.size(); i++) {
            int choiceNumber = i + 1;
            display = display.concat(choiceNumber + ": " + choices.get(i)+"\n");
        }
        
        return display;
    }
    
}
