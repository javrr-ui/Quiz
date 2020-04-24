package quiz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.ResourceBundle.getBundle;

/**
 * Represents a Quiz
 *
 * @author NicolasADavid
 * @author Javatlacati
 */
public class Quiz {

    public static final String FIELD_DIVIDER = "@@";
    private static final Pattern SEPARATE_BY_DIVIDER_PATTERN = Pattern.compile(FIELD_DIVIDER);
    private static final Pattern LINE_BREAK = Pattern.compile("\\\\n");

    private int maxQuestions = -1;
    /**
     *
     */
    private final List<Question> questions;
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
        questions = new ArrayList<>(50);
    }

    /**
     * Reads a file and call the method that processes it to fill the quiz.
     */
    public void createQuestionsFromFile(String path, boolean isInJar) {
        try (BufferedReader reader = createReader(path, isInJar)) {
            if (reader == null) {
                return;
            }
            parseQuestions(reader);
        } catch (IOException ex) {
            Logger.getLogger(Quiz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private BufferedReader createReader(String path, boolean isInJar) {
        BufferedReader reader;
        if (isInJar) {
            reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)));
        } else {
            try {
                reader = new BufferedReader(new FileReader(new File(path)));
            } catch (FileNotFoundException e) {
                Logger.getLogger(Quiz.class.getName()).log(Level.SEVERE, "The specified file does not exist:" + path, e);
                return null;
            }
        }
        return reader;
    }

    private void parseQuestions(BufferedReader reader) throws IOException {
        String thisLine;
        while ((thisLine = reader.readLine()) != null) {
            parseQuestion(thisLine);
        }
    }

    /**
     * Display a message. Display the questions. Get input. Check if
     * correct/gradable and notify user of points earned.
     */
    public void displayAndCheckQuestions() {

        //Display a message
        final String message
                = MessageFormat.format(getBundle("quiz/resources/quiz").getString("FOR_MULTIPLE_CHOICE"), new Object[]{});
        Collections.shuffle(questions);//pregunta aleatoriamente
        System.out.println(message);
        final Scanner scanner = new Scanner(System.in);
        int questionsToAsk;
        if (maxQuestions != -1 && maxQuestions < questions.size()) {
            questionsToAsk = maxQuestions;
        } else {
            questionsToAsk = questions.size();
        }
        //For every question in questions
        for (int i = 0; i < questionsToAsk; i++) {
            Question currentQuestion = questions.get(i);
            //Display the question
            System.out.println(currentQuestion.display());

            System.out.println(MessageFormat.format(getBundle("quiz/resources/quiz").getString("ENTER_YOUR_ANSWERS"), new Object[]{}));
            //Take input
            final String input = scanner.nextLine();

            //Show user points earned for answer
            //TODO perhaps instead of 
            //currentQuestion.checkQuestionProvidingAnswer(input) userAnswer
            //could be just setted
            System.out.println(MessageFormat.format(getBundle("quiz/resources/quiz").getString("RECEIVED_POINTS"), currentQuestion.checkQuestionProvidingAnswer(input)));

            //Add points to total score
            final double puntosRespuesta = currentQuestion.checkQuestion();
            score += puntosRespuesta;

            //Show user total points earned
            System.out.printf(getBundle("quiz/resources/quiz").getString("TOTAL_POINTS"), score);

            //Was the answer vetted/trial and correct, partially correct, or incorrect?
            if (currentQuestion.gradeQuestion()) {
                countForVetted(puntosRespuesta);
            } else {
                countForTrial(puntosRespuesta);
            }

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

    }

    private void countForVetted(double puntosRespuesta) {
        //Count vetted questions
        totalVetted++;

        //Add to vetted score
        vettedScore += puntosRespuesta;

        //Was question correct, partially corrct, or incorrct?
        //Question is vetted
        int compare = Double.compare(puntosRespuesta, 0.0);
        if (compare > 0) {
            //Count correct/partial vetted
            totalCorrectVetted++;
        } else {
            //Count incorrect vetted
            totalIncorrectVetted++;
        }
    }

    private void countForTrial(double puntosRespuesta) {

        //Count trial questions
        totalTrial++;

        //Add to trial score
        trialScore += puntosRespuesta;

        //Was question correct, partially correct, or incorrect?
        //Question is trial
        int compare = Double.compare(puntosRespuesta, 0.0);
        if (compare > 0) {
            //Count correct/partial trial
            totalCorrectTrial++;
        } else {
            //Count incorrect trial
            totalIncorrectTrial++;
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

        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("TOTAL_QUESTIONS"), new Object[]{}), questions.size());
        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("TOTAL_POINTS"), new Object[]{}), score);
        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("FOR_CREDIT"), new Object[]{}), totalCorrect);
        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("FOR_NO_CREDIT"), new Object[]{}), totalIncorrect);
        System.out.println();

        System.out.println(MessageFormat.format(getBundle("quiz/resources/quiz").getString("VETTED_QUESTIONS"), totalVetted));
        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("TOTAL_OF_POINTS"), new Object[]{}), vettedScore);
        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("FOR_CREDIT"), new Object[]{}), totalCorrectVetted);
        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("FOR_NO_CREDIT"), new Object[]{}), totalIncorrectVetted);
        System.out.println();

        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("TRIAL_QUESTIONS"), new Object[]{}), totalTrial);
        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("TOTAL_OF_POINTS"), new Object[]{}), trialScore);
        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("FOR_CREDIT"), new Object[]{}), totalCorrectTrial);
        System.out.printf(MessageFormat.format(getBundle("quiz/resources/quiz").getString("FOR_NO_CREDIT"), new Object[]{}), totalIncorrectTrial);

        System.out.println(MessageFormat.format(getBundle("quiz/resources/quiz").getString("HAVE_A_W"), new Object[]{}));
    }

    /**
     * Adds a new multiple choice question to the specified question list.
     */
    private void addMultipleChoiceQuestion(final List<Question> questions,
                                           final String vettedness, final String explanation, final String questionText, final int correctAnswerIdx,
                                           final String... answersTexts) {
        final MultipleChoiceQuestion choiceQuestion = new MultipleChoiceQuestion(vettedness);
        choiceQuestion.setExplanation(explanation);
        choiceQuestion.setText(questionText);
        for (int i = 0; i < answersTexts.length; i++) {
            final String answerText = answersTexts[i];
            choiceQuestion.setChoice(answerText, i == correctAnswerIdx);
        }
        questions.add(choiceQuestion);
    }

    /**
     *
     */
    private void addMultipleAnswerQuestion(final List<Question> questions,
                                           final String vettedness, final String explanation, final String questionText,
                                           final Map<String, Boolean> answerChoicesMap) {
        final MultipleAnswerQuestion choiceQuestion = new MultipleAnswerQuestion(vettedness);
        choiceQuestion.setExplanation(explanation);
        choiceQuestion.setText(questionText);
        answerChoicesMap.forEach(choiceQuestion::setChoice);
        questions.add(choiceQuestion);
    }

    /**
     *
     */
    private void addFillBlankQuestion(final List<Question> questions,
                                      final String vettedness, final String explanation, final String questionText, final String... blanks) {
        final FillBlankQuestion fillBlankQuestion = new FillBlankQuestion(vettedness);
        fillBlankQuestion.setExplanation(explanation);
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
            throw new RuntimeException(MessageFormat.format(getBundle("quiz/resources/quiz").getString("ANSWER_TEXTS_ERR"), new Object[]{}));
        }
        final HashMap<String, Boolean> hashMap = new HashMap<>(2);
        for (int answIdx = 0; answIdx < answerTexts.length; answIdx++) {
            hashMap.put(answerTexts[answIdx], answerValidities[answIdx]);
        }
        return hashMap;
    }

    /**
     * Prints again failed questions showing also their correct answers
     */
    public void showFailed() {
        System.out.println(MessageFormat.format(getBundle("quiz/resources/quiz").getString("FAILED_QUESTIONS"), new Object[]{}));

        int questionsToAsk;
        if (maxQuestions != -1 && maxQuestions < questions.size()) {
            questionsToAsk = maxQuestions;
        } else {
            questionsToAsk = questions.size();
        }

        for (int i = 0; i < questionsToAsk; i++) {
            final Question currentQuestion = questions.get(i);
            final double points = currentQuestion.checkQuestion();
            if (points < currentQuestion.getMaxPoints()) {
                //Show user points earned for incorrect answer
                System.out.println(questions.get(i).display());
                System.out.println(MessageFormat.format(getBundle("quiz/resources/quiz").getString("RECEIVED_POINTS"), points));
                System.out.println(MessageFormat.format(getBundle("quiz/resources/quiz").getString("CORRECT_ANSWER"), questions.get(i).getAnswer()));
                System.out.println(MessageFormat.format(getBundle("quiz/resources/quiz").getString("EXPLANATION"), questions.get(i).explanation));
            }
        }
    }

    /**
     * @param questionString the question fields split by @@
     */
    private void parseQuestion(final String questionString) {
        final String[] questarray = SEPARATE_BY_DIVIDER_PATTERN.split(questionString);
        String tipoPregunta = questarray[0];
        switch (tipoPregunta) {
            case "MC"://multiple choice
                addMultipleChoiceQuestion(questions, "v".equals(questarray[1]) ? Question.VETTED : Question.TRIAL, questarray[2],
                        formateaPregunta(questarray[3]), Integer.parseInt(questarray[4]), Arrays.copyOfRange(questarray, 5, questarray.length));
                break;
            case "FB"://fill in the blanks
                addFillBlankQuestion(questions, "v".equals(questarray[1]) ? Question.VETTED : Question.TRIAL, questarray[2], formateaPregunta(questarray[3]), Arrays.copyOfRange(questarray, 4, questarray.length));
                break;
            case "MA":
                addMultipleAnswerQuestion(questions, "v".equals(questarray[1]) ? Question.VETTED : Question.TRIAL, questarray[2], formateaPregunta(questarray[3]),
                        parseChoicesMap(Arrays.copyOfRange(questarray, 4, questarray.length)));
                break;
            default:
                System.err.println(MessageFormat.format(getBundle("quiz/resources/quiz").getString("QUESTION_TYPE_ERR"), new Object[]{}));
                break;
        }

    }

    /**
     * Parses the choices and relates them with their values.
     *
     * @param questionMapArr list with options and then values of each question
     * @return options maped with their values
     */
    public Map<String, Boolean> parseChoicesMap(final String... questionMapArr) {
        final String[] questarray = Arrays.copyOfRange(questionMapArr, 0, questionMapArr.length / 2);
        final String[] strAnswerValidities = Arrays.copyOfRange(questionMapArr, questionMapArr.length / 2, questionMapArr.length);
        final Boolean[] answerValidities = new Boolean[strAnswerValidities.length];
        Arrays.stream(strAnswerValidities).map((booleanAnswer) -> "true".equalsIgnoreCase(booleanAnswer) ? Boolean.TRUE : Boolean.FALSE).collect(Collectors.toList()).toArray(answerValidities);
        return createAnswerChoicesMap(questarray, answerValidities);
    }

    private String formateaPregunta(final String strPregunta) {
        return LINE_BREAK.matcher(strPregunta).replaceAll("\n");
    }

    public void askForSubsetSize() {
        System.out.printf("Currently there are %d questions how many of them you want in the test?\n", questions.size());
        Scanner scanner = new Scanner(System.in);
        try {
            maxQuestions = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.err.println("problem reading number of questions maximum asserted");
            maxQuestions = -1;
        }

    }
}
