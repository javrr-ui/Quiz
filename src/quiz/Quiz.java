package quiz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Represents a Quiz
 *
 * @author NicolasADavid
 * @author Javatlacati
 */
public class Quiz {

    /**
     *
     */
    private List<Question> questions;
    /**
     * Score obtained in the quiz.
     */
    private double score;
    /**
     * Mandatory questions.
     */
    private double vettedScore;
    /**
     * Optional questions.
     */
    private double trialScore;
    /**
     * Number of vetted questions asked.
     */
    private int totalVetted;
    /**
     * Number of trial questions asked.
     */
    private int totalTrial;
    /**
     * Number of vetted questions answered correcly.
     */
    private int totalCorrectVetted;
    /**
     * Number of vetted questions not answered correcly.
     */
    private int totalIncorrectVetted;
    /**
     *
     */
    private int totalCorrectTrial;
    /**
     *
     */
    private int totalIncorrectTrial;

    /**
     * Default Constructor
     */
    public Quiz() {
        questions = new ArrayList<>();
    }

    /**
     * Creates the questions of the quiz.
     */
  /*  public void createQuestions() {
        addMultipleChoiceQuestion(questions, Question.VETTED,
                "Multiple choice: What is the first month of the year?", 0,
                "January", "February", "March"
        );

        addMultipleChoiceQuestion(questions, Question.TRIAL,
                "Multiple choice: What is an apple product?", 0,
                "ipod", "zune", "lenovo"
        );

        addMultipleChoiceQuestion(questions, Question.VETTED,
                "Multiple choice: What is Barack Obama known for?", 1,
                "Really likes the show Narcos",
                "He's the president, dude",
                "Doesn't actually like mac and cheese"
        );

        addMultipleAnswerQuestion(questions, Question.VETTED,
                "Multiple answer (more than one possible): What days are not work days?",
                createAnswerChoicesMap(Utilities.varargs("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                        Utilities.varargs(false, false, false, false, false, true, true)
                )
        );

        addMultipleAnswerQuestion(questions, Question.TRIAL,
                "Multiple answer (more than one possible): What words begin with the letter \"a\"?",
                createAnswerChoicesMap(Utilities.varargs("apple", "chair", "astronomy", "desk"),
                        Utilities.varargs(true, false, true, false)
                )
        );
        addMultipleAnswerQuestion(questions, Question.VETTED,
                "Multiple answer (more than one possible): What are common household chores?",
                createAnswerChoicesMap(Utilities.varargs("sweeping", "laundry", "skydiving", "basket weaving"),
                        Utilities.varargs(true, true, false, false)
                )
        );

        addMultipleChoiceQuestion(questions, Question.VETTED,
                "Multiple choice: What's the answer, bro?", 0,
                "The answer", "Not the answer", "Also not the answer"
        );

        addFillBlankQuestion(questions, Question.VETTED,
                "Fill in the blank: The quick red ___ jumped over the lazy brown ___. Hint: fox dog",
                "fox", "dog"
        );

        addFillBlankQuestion(questions, Question.VETTED,
                "Fill in the blank: This is a fill in the _____ question.",
                "blank"
        );

        addFillBlankQuestion(questions, Question.VETTED,
                "Fill in the blank: This assignment is for a _________ course.",
                "programming"
        );
    }*/

    /**
     * Reads a file and call the method that processes it to fill the quiz.
     */
    public void createQuestionsFromFile(String path,boolean isInJar) {
        final URL rutaArchivo;
        if(isInJar){
            rutaArchivo=Thread.currentThread().getContextClassLoader().getResource(path);
        }else{
            try {
                rutaArchivo=new File(path).toURI().toURL();
            } catch (MalformedURLException ex) {
                return;
            }
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(rutaArchivo.toURI())));
            String thisLine = null;
            while ((thisLine = reader.readLine()) != null) {
                parseaPregunta(thisLine);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Quiz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(Quiz.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(Quiz.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Display a message. Display the questions. Get input. Check if
     * correct/gradable and notify user of points earned.
     */
    public void displayAndCheckQuestions() {

        //Display a message
        final String message
                = "For multiple choice questions, enter the number of "
                + "your choice.\nFor multiple answer questions, enter the number(s) "
                + "of your choice(s) separated by space.\nFor Fill in the blank"
                + " questions enter your answer(s) separated by spaces.\n";

        System.out.println(message);
        final Scanner scanner = new Scanner(System.in);
        //For every question in questions
        final int numQuestions = questions.size();
        for (int i = 0; i < numQuestions; i++) {
            final Question currentQuestion = questions.get(i);

            //Display the question
            System.out.println(currentQuestion.display());

            System.out.println("Enter your answers in order separated by spaces \n");
            //Take input
            final String input = scanner.nextLine();

            //Show user points earned for answer
            //TODO perhaps instead of 
            //currentQuestion.checkQuestionProvidingAnswer(input) userAnswer
            //could be just setted
            System.out.println("You received " + currentQuestion.checkQuestionProvidingAnswer(input) + " points.");

            //Add points to total score
            final double puntosRespuesta = currentQuestion.checkQuestion();
            score += puntosRespuesta;

            //Show user total points earned
            System.out.println("Total points: " + score + "\n");

            //Was the answer vetted/trial and correct, partially correct, or incorrect?
            int compare;

            if (currentQuestion.gradeQuestion()) { //Question is vetted

                //Count vetted questions
                totalVetted++;

                //Add to vetted score
                vettedScore += puntosRespuesta;

                //Was question correct, partially corrct, or incorrct?
                compare = Double.compare(puntosRespuesta, 0.0);
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
                trialScore += puntosRespuesta;

                //Was question correct, partially correct, or incorrect?
                compare = Double.compare(puntosRespuesta, 0.0);
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
        final int totalCorrect = totalCorrectVetted + totalCorrectTrial;
        final int totalIncorrect = totalIncorrectVetted + totalIncorrectTrial;

        System.out.printf("There were a total of %d questions.", questions.size());
        System.out.printf("You received a total of %.2f points.", score);
        System.out.printf("Answered %d for full or partial credit", totalCorrect);
        System.out.printf("Answered %d for no credit", totalIncorrect);
        System.out.println();

        System.out.println("There were " + totalVetted + " vetted questions.");
        System.out.printf("You received a total of %.2f points on them.\n",vettedScore);
        System.out.printf("Answered %d for full or partial credit", totalCorrectVetted);
        System.out.printf("Answered  %d for no credit", totalIncorrectVetted);
        System.out.println();

        System.out.printf("There were %d trial questions.", totalTrial);
        System.out.printf("You received a total of %.2f points on them.", trialScore);
        System.out.printf("Answered %d for full or partial credit", totalCorrectTrial);
        System.out.printf("Answered %d for no credit", totalIncorrectTrial);

        System.out.println("Have a wonderful day.\n");
    }

    /**
     * Adds a new multiple choice question to the specified question list.
     */
    private void addMultipleChoiceQuestion(final List<Question> questions,
            final String vettedness, final String questionText, final int correctAnswerIdx,
            final String... answersTexts) {
        final MultipleChoiceQuestion choiceQuestion = new MultipleChoiceQuestion(vettedness);
        choiceQuestion.setText(questionText);
        for (int i = 0; i < answersTexts.length; i++) {
            final String answerText = answersTexts[i];
            if (i == correctAnswerIdx) {
                choiceQuestion.setChoice(answerText, true);
            } else {
                choiceQuestion.setChoice(answerText, false);
            }
        }
        questions.add(choiceQuestion);
    }

    /**
     *
     */
    private void addMultipleAnswerQuestion(final List<Question> questions,
            final String vettedness, final String questionText,
            final Map<String, Boolean> answerChoicesMap) {
        final MultipleAnswerQuestion choiceQuestion = new MultipleAnswerQuestion(vettedness);
        choiceQuestion.setText(questionText);
        answerChoicesMap.entrySet().stream().forEach((choiceEntry) -> {
            choiceQuestion.setChoice(choiceEntry.getKey(), choiceEntry.getValue());
        });
        questions.add(choiceQuestion);
    }

    /**
     *
     */
    private void addFillBlankQuestion(final List<Question> questions,
            final String vettedness, final String questionText, final String... blanks) {
        final FillBlankQuestion fillBlankQuestion = new FillBlankQuestion(vettedness);
        fillBlankQuestion.setText(questionText);
        for (final String blank : blanks) {
            fillBlankQuestion.setAnswer(blank);
        }
        questions.add(fillBlankQuestion);
    }

    /**
     *
     */
    private Map<String, Boolean> createAnswerChoicesMap(final String[] answerTexts, final Boolean... answerValidities) {
        final int numsOfAnswers = answerTexts.length;
        if (numsOfAnswers != answerValidities.length) {
            throw new RuntimeException("Answer texts should match answer values one by one");
        }
        final HashMap<String, Boolean> hashMap = new HashMap<>();
        for (int answIdx = 0; answIdx < answerTexts.length; answIdx++) {
            hashMap.put(answerTexts[answIdx], answerValidities[answIdx]);
        }
        return hashMap;
    }

    /**
     * Prints again failed questions showing also their correct answers
     */
    public void showFailed() {
        System.out.println("Failed Questions:\n");
        for (int i = 0; i < questions.size(); i++) {
            final Question currentQuestion = questions.get(i);
            final double points = currentQuestion.checkQuestion();
            if (points < currentQuestion.getMaxPoints()) {
                //Show user points earned for incorrect answer
                System.out.println(questions.get(i).display());
                System.out.println("You received " + points + " points.");
                System.out.println("Correct answer was:" + questions.get(i).getAnswer() + "\n");
            }
        }
    }

    /////////////////// setters/getters  //////////////////////
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(final List<Question> questions) {
        this.questions = questions;
    }

    public double getScore() {
        return score;
    }

    public void setScore(final double score) {
        this.score = score;
    }

    public double getVettedScore() {
        return vettedScore;
    }

    public void setVettedScore(double vettedScore) {
        this.vettedScore = vettedScore;
    }

    public double getTrialScore() {
        return trialScore;
    }

    public void setTrialScore(final double trialScore) {
        this.trialScore = trialScore;
    }

    public int getTotalVetted() {
        return totalVetted;
    }

    public void setTotalVetted(final int totalVetted) {
        this.totalVetted = totalVetted;
    }

    public int getTotalTrial() {
        return totalTrial;
    }

    public void setTotalTrial(final int totalTrial) {
        this.totalTrial = totalTrial;
    }

    public int getTotalCorrectVetted() {
        return totalCorrectVetted;
    }

    public void setTotalCorrectVetted(final int totalCorrectVetted) {
        this.totalCorrectVetted = totalCorrectVetted;
    }

    public int getTotalIncorrectVetted() {
        return totalIncorrectVetted;
    }

    public void setTotalIncorrectVetted(final int totalIncorrectVetted) {
        this.totalIncorrectVetted = totalIncorrectVetted;
    }

    public int getTotalCorrectTrial() {
        return totalCorrectTrial;
    }

    public void setTotalCorrectTrial(final int totalCorrectTrial) {
        this.totalCorrectTrial = totalCorrectTrial;
    }

    public int getTotalIncorrectTrial() {
        return totalIncorrectTrial;
    }

    public void setTotalIncorrectTrial(final int totalIncorrectTrial) {
        this.totalIncorrectTrial = totalIncorrectTrial;
    }

    /**
     * @param questionString the question fields split by @@
     */
    private void parseaPregunta(final String questionString) {
        final String[] questarray = questionString.split("@@");
        String tipoPregunta = questarray[0];
        switch (tipoPregunta) {
            case "MC"://multiple choice
                addMultipleChoiceQuestion(questions, questarray[1] == "v" ? Question.VETTED : Question.TRIAL,
                        formateaPregunta(questarray[2]), Integer.valueOf(questarray[3]), Arrays.copyOfRange(questarray, 4, questarray.length));
                break;
            case "FB"://fill in the blanks
                addFillBlankQuestion(questions, questarray[1] == "v" ? Question.VETTED : Question.TRIAL, formateaPregunta(questarray[2]), Arrays.copyOfRange(questarray, 3, questarray.length));
                break;
            case "MA":
                addMultipleAnswerQuestion(questions, questarray[1] == "v" ? Question.VETTED : Question.TRIAL, formateaPregunta(questarray[2]),
                        parseChoicesMap(Arrays.copyOfRange(questarray, 3, questarray.length)));
                break;
            default:
                System.err.println("Question type not supported");
                break;
        }

    }
    public Map<String,Boolean> parseChoicesMap(final String... questionMapArr){
        String[]questarray=Arrays.copyOfRange(questionMapArr, 0, questionMapArr.length/2);
        String[] strAnswerValidities=Arrays.copyOfRange(questionMapArr, questionMapArr.length/2,questionMapArr.length);
        Boolean[] answerValidities=new Boolean[strAnswerValidities.length];
        Arrays.stream(strAnswerValidities).map((x)->x.equalsIgnoreCase("true")?Boolean.TRUE:Boolean.FALSE).collect(Collectors.toList()).toArray(answerValidities);
        return createAnswerChoicesMap(questarray, answerValidities);
    }
    
    private String formateaPregunta(String strPregunta){
        return strPregunta.replaceAll("\\n", "\n");
    }

}
