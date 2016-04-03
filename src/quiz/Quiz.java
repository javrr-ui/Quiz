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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import quiz.utils.Util;

public class Quiz {

    private ArrayList<Question> questions;

    private double score = 0.0;
    private double vettedScore = 0.0;
    private double trialScore = 0.0;

    private int totalVetted = 0;
    private int totalTrial = 0;

    private int totalCorrectVetted = 0;
    private int totalIncorrectVetted = 0;

    private int totalCorrectTrial = 0;
    private int totalIncorrectTrial = 0;

    public Quiz() {
        questions = new ArrayList<Question>();
    }

    /**
     * Create the questions of the quiz. 10 Questions.
     */
    public void createQuestions() {
//Question 1. Vetted.
        addMultipleChoiceQuestion(questions, "vetted",
                "Multiple choice: What is the first month of the year?",
                createAnswerChoicesMap(
                        Util.varargs("January", "February", "March"),
                        Util.varargs(true, false, false)
                )
        );

        //Question 2. Trial.
        MultipleChoiceQuestion question2 = new MultipleChoiceQuestion("trial");
        question2.setText("Multiple choice: What is an apple product?");
        question2.setChoice("ipod", true);
        question2.setChoice("zune", false);
        question2.setChoice("lenovo", false);
        questions.add(question2);

        //Question 3. Vetted.
        MultipleChoiceQuestion question3 = new MultipleChoiceQuestion("vetted");
        question3.setText("Multiple choice: What is Barack Obama known for?");
        question3.setChoice("Really likes the show Narcos", false);
        question3.setChoice("He's the president, dude", true);
        question3.setChoice("Doesn’t actually like mac and cheese", false);
        questions.add(question3);

        //Question 4. Vetted.
        MultipleAnswerQuestion question4 = new MultipleAnswerQuestion("vetted");
        question4.setText("Multiple answer (more than one possible): What days are not work days?");
        question4.setChoice("Monday", false);
        question4.setChoice("Tuesday", false);
        question4.setChoice("Wednesday", false);
        question4.setChoice("Thursday", false);
        question4.setChoice("Friday", false);
        question4.setChoice("Saturday", true);
        question4.setChoice("Sunday", true);
        questions.add(question4);

        //Question 5. Trial.
        MultipleAnswerQuestion question5 = new MultipleAnswerQuestion("trial");
        question5.setText("Multiple answer (more than one possible): What words begin with the letter \"a\"?");
        question5.setChoice("apple", true);
        question5.setChoice("chair", false);
        question5.setChoice("astronomy", true);
        question5.setChoice("desk", false);
        questions.add(question5);

        //Question 6. Vetted.
        MultipleAnswerQuestion question6 = new MultipleAnswerQuestion("vetted");
        question6.setText("Multiple answer (more than one possible): What are common household chores?");
        question6.setChoice("sweeping", true);
        question6.setChoice("laundry", true);
        question6.setChoice("skydiving", false);
        question6.setChoice("basket weaving", false);
        questions.add(question6);

        //Question 7. Vetted.
        MultipleChoiceQuestion question7 = new MultipleChoiceQuestion("vetted");
        question7.setText("Multiple choice: What's the answer, bro?");
        question7.setChoice("The answer", true);
        question7.setChoice("Not the answer", false);
        question7.setChoice("Also not the answer", false);
        questions.add(question7);

        //Question 8. Vetted.
        FillBlankQuestion question8 = new FillBlankQuestion("vetted");
        question8.setText("Fill in the blank: The quick red ___ jumped over the lazy brown ___. Hint: fox dog");
        question8.setAnswer("fox");
        question8.setAnswer("dog");
        questions.add(question8);

        //Question 9. Vetted.
        FillBlankQuestion question9 = new FillBlankQuestion("vetted");
        question9.setText("Fill in the blank: This is a fill in the _____ question.");
        question9.setAnswer("blank");
        questions.add(question9);

        //Question 10. Vetted.
        FillBlankQuestion question10 = new FillBlankQuestion("vetted");
        question10.setText("Fill in the blank: This assignment is for a _________ course.");
        question10.setAnswer("programming");
        questions.add(question10);

    }

    /**
     * Display a message. Display the questions. Get input. Check if
     * correct/gradable and notify user of points earned.
     */
    public void displayAndCheckQuestions() {

        //Display a message
        String message
                = "For multiple choice questions, enter the number of "
                + "your choice.\nFor multiple answer questions, enter the number(s) "
                + "of your choice(s) separated by space.\nFor Fill in the blank"
                + " questions enter your answer(s) separated by spaces.\n";

        System.out.println(message);

        //For every question in questions
        for (int i = 0; i < questions.size(); i++) {

            //Display the question
            System.out.println(questions.get(i).display());

            //Take input
            String input;
            System.out.println("Enter your answers in order separated by spaces");
            Scanner in = new Scanner(System.in);
            input = in.nextLine();

            //Print line
            System.out.println();

            //Show user points earned for answer
            System.out.println("You received " + questions.get(i).checkQuestion(input) + " points.");

            //Add points to total score
            score += questions.get(i).checkQuestion(input);

            //Show user total points earned
            System.out.println("Total points: " + score + "\n");

            //Was the answer vetted/trial and correct, partially correct, or incorrect?
            int compare;

            if (questions.get(i).gradeQuestion()) { //Question is vetted

                //Count vetted questions
                totalVetted++;

                //Add to vetted score
                vettedScore += questions.get(i).checkQuestion(input);

                //Was question correct, partially corrct, or incorrct?
                compare = Double.compare(questions.get(i).checkQuestion(input), 0.0);
                if (compare > 0) {
                    //Count correct/partial vetted
                    totalCorrectVetted++;
                } else {
                    //Count incorrect vetted
                    totalIncorrectVetted++;
                }

            } else { //Question is trial

                //Count trial questions
                totalTrial++;

                //Add to trial score
                trialScore += questions.get(i).checkQuestion(input);

                //Was question correct, partially correct, or incorrect?
                compare = Double.compare(questions.get(i).checkQuestion(input), 0.0);
                if (compare > 0) {
                    //Count correct/partial trial
                    totalCorrectTrial++;
                } else {
                    //Count incorrect trial
                    totalIncorrectTrial++;
                }

            }

        }

    }

    /**
     * Report total questions, vetted questions, and trial questions. Report
     * score earned for each. Report questions answered correctly or partially
     * correctly for each. Report questions answered incorrectly for each.
     */
    public void summarizeResults() {
        int totalCorrect = totalCorrectVetted + totalCorrectTrial;
        int totalIncorrect = totalIncorrectVetted + totalIncorrectTrial;

        System.out.println("There were a total of " + questions.size() + " questions.");
        System.out.println("You received a total of " + score + " points.");
        System.out.println("Answered " + totalCorrect + " for full or partial credit");
        System.out.println("Answered " + totalIncorrect + " for no credit");
        System.out.println();

        System.out.println("There were " + totalVetted + " vetted questions.");
        System.out.println("You received a total of " + vettedScore + " points on them.");
        System.out.println("Answered " + totalCorrectVetted + " for full or partial credit");
        System.out.println("Answered " + totalIncorrectVetted + " for no credit");
        System.out.println();

        System.out.println("There were " + totalTrial + " trial questions.");
        System.out.println("You received a total of " + trialScore + " points on them.");
        System.out.println("Answered " + totalCorrectTrial + " for full or partial credit");
        System.out.println("Answered " + totalIncorrectTrial + " for no credit");

        System.out.println("Have a wonderful day.");
    }

    private void addMultipleChoiceQuestion(ArrayList<Question> questions, String vettedness, String questionText, Map<String, Boolean> answerChoicesMap) {
        MultipleChoiceQuestion question1 = new MultipleChoiceQuestion(vettedness);
        question1.setText(questionText);
        for (Map.Entry<String, Boolean> choiceEntry : answerChoicesMap.entrySet()) {
            String choiceText = choiceEntry.getKey();
            Boolean answerValue = choiceEntry.getValue();
            question1.setChoice(choiceText, answerValue);
        }
        questions.add(question1);
    }

    private Map<String, Boolean> createAnswerChoicesMap(String[] answerTexts, Boolean[] answerValidities) {
        HashMap<String, Boolean> hashMap = new HashMap<>();
        int numsOfAnswers = answerTexts.length;
        if (numsOfAnswers != answerValidities.length) {
            throw new RuntimeException("Anwer texts whould match answr values one by one");
        }
        for (int answIdx = 0; answIdx < answerTexts.length; answIdx++) {
            hashMap.put(answerTexts[answIdx], answerValidities[answIdx]);
        }
        return hashMap;
    }

}
