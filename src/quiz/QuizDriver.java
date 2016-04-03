///////////////////////////////////////////////////////////////////////////////                  
// Title:            Quiz
// Files:          JavaApplication9Quiz.java;Quiz.java;Question.java;
//                  MultipleChoiceQuestion.java;MultipleAnswerQuestion.java;
//                  FillBlankQuestion.java
// Semester:         COP3337 Fall 2015
//
// Author:           3587814
// Lecturer's Name:  Prof. Maria Charters
//
// Description of Program’s Functionality: 
//////////////////////////// 80 columns wide/////////////////////////////////
package quiz;

/**
 * Program that administers a quiz consisting of multiple choice, multiple
 * answer, and fill-in-the-blank questions written for Programming 2 (Java)
 */
public final class QuizDriver {

    private QuizDriver() {
    }

    /**
     * Application entry point
     *
     * @param args program arguments ( actually they are not parsed )
     */
    public static void main(final String... args) {
        final Quiz doom = new Quiz();
        doom.createQuestions();
        //Display questions to user, receive and check response
        doom.displayAndCheckQuestions();
        doom.summarizeResults();

    }

}
