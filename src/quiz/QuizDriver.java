
package quiz;

/**
 * Administers a quiz consisting of multiple choice, multiple answer, and
 * fill-in-the-blank questions.
 *
 * @author NicolasADavid
 * @author Javatlacati
 */
public final class QuizDriver {

    /**
     * Default constructor.
     */
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
        doom.showFailed();
    }

}
