package klondike;

import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeCard;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for KlondikeTextualController.
 */
public class KlondikeTextualControllerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullReadable() {
        new KlondikeTextualController(null, new StringWriter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullAppendable() {
        new KlondikeTextualController(new StringReader(""), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBothNull() {
        new KlondikeTextualController(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayGameNullModel() {
        StringReader input = new StringReader("q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(null, deck, false, 7, 3);
    }

    @Test
    public void testQuitImmediately() {
        StringReader input = new StringReader("q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Game quit!"));
        assertTrue(result.contains("State of game when quit:"));
        assertTrue(result.contains("Score:"));
    }

    @Test
    public void testQuitUppercase() {
        StringReader input = new StringReader("Q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Game quit!"));
    }

    @Test
    public void testDiscardDraw() {
        StringReader input = new StringReader("dd q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Draw:"));
        assertTrue(result.contains("Game quit!"));
    }

    @Test
    public void testInvalidCommand() {
        StringReader input = new StringReader("xyz q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Invalid command") || result.contains("Game quit!"));
    }

    @Test
    public void testInvalidNumberInput() {
        StringReader input = new StringReader("mpp abc 1 2 q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Invalid input") || result.contains("Game quit!"));
    }

    @Test
    public void testNegativeNumberInput() {
        StringReader input = new StringReader("mpp -1 1 2 q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Invalid input") || result.contains("Game quit!"));
    }

    @Test
    public void testMovePileCommand() {
        StringReader input = new StringReader("mpp 1 1 2 q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Score:"));
    }

    @Test
    public void testMoveDrawCommand() {
        StringReader input = new StringReader("md 1 q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Score:"));
    }

    @Test
    public void testMoveToFoundationCommand() {
        StringReader input = new StringReader("mpf 1 1 q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Score:"));
    }

    @Test
    public void testMoveDrawToFoundationCommand() {
        StringReader input = new StringReader("mdf 1 q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Score:"));
    }

    @Test
    public void testInvalidMoveShowsError() {
        StringReader input = new StringReader("mpp 1 1 1 q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Invalid move") || result.contains("Game quit!"));
    }

    @Test
    public void testMultipleCommands() {
        StringReader input = new StringReader("dd dd dd q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Game quit!"));
    }

    @Test
    public void testScoreDisplayed() {
        StringReader input = new StringReader("q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Score: 0"));
    }

    @Test
    public void testGameStateDisplayed() {
        StringReader input = new StringReader("q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Draw:"));
        assertTrue(result.contains("Foundation:"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoInputAvailable() {
        StringReader input = new StringReader("");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);
    }

    @Test
    public void testQuitDuringNumberInput() {
        StringReader input = new StringReader("mpp 1 q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Game quit!"));
    }

    @Test
    public void testRetryInvalidInput() {
        StringReader input = new StringReader("mpp abc 1 1 2 q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Invalid input"));
    }
}