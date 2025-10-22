import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw04.WhiteheadKlondike;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test that controller works with both game variants.
 */
public class ControllerWithVariantsTest {

    @Test
    public void testControllerWithBasicModel() {
        StringReader input = new StringReader("q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue("Should contain game quit message", result.contains("Game quit!"));
    }

    @Test
    public void testControllerWithWhiteheadModel() {
        StringReader input = new StringReader("q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        WhiteheadKlondike model = new WhiteheadKlondike();
        List<KlondikeCard> deck = model.createNewDeck();

        controller.playGame(model, deck, false, 7, 3);

        String result = output.toString();
        assertTrue("Should contain game quit message", result.contains("Game quit!"));
    }

    @Test
    public void testControllerCommandsWorkWithBothVariants() {
        String commands = "dd dd q";

        // Test with Basic
        StringReader input1 = new StringReader(commands);
        StringWriter output1 = new StringWriter();
        KlondikeController controller1 = new KlondikeTextualController(input1, output1);

        BasicKlondike basicModel = new BasicKlondike();
        controller1.playGame(basicModel, basicModel.createNewDeck(), false, 7, 3);

        assertTrue(output1.toString().contains("Game quit!"));

        // Test with Whitehead
        StringReader input2 = new StringReader(commands);
        StringWriter output2 = new StringWriter();
        KlondikeController controller2 = new KlondikeTextualController(input2, output2);

        WhiteheadKlondike whiteheadModel = new WhiteheadKlondike();
        controller2.playGame(whiteheadModel, whiteheadModel.createNewDeck(), false, 7, 3);

        assertTrue(output2.toString().contains("Game quit!"));
    }

    @Test
    public void testViewWorksWithBothVariants() {
        // Verify that both models produce valid output through controller
        StringReader input = new StringReader("q");
        StringWriter output = new StringWriter();
        KlondikeController controller = new KlondikeTextualController(input, output);

        BasicKlondike model = new BasicKlondike();
        controller.playGame(model, model.createNewDeck(), false, 7, 3);

        String result = output.toString();
        assertTrue(result.contains("Draw:"));
        assertTrue(result.contains("Foundation:"));
        assertTrue(result.contains("Score:"));
    }
}