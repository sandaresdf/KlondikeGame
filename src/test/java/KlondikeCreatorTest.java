import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw04.KlondikeCreator;
import klondike.model.hw04.WhiteheadKlondike;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for KlondikeCreator factory.
 */
public class KlondikeCreatorTest {

    @Test
    public void testCreateBasic() {
        KlondikeModel<KlondikeCard> model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
        assertNotNull(model);
        assertTrue(model instanceof BasicKlondike);
    }

    @Test
    public void testCreateWhitehead() {
        KlondikeModel<KlondikeCard> model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
        assertNotNull(model);
        assertTrue(model instanceof WhiteheadKlondike);
    }

    @Test
    public void testMultipleCreations() {
        KlondikeModel<KlondikeCard> model1 = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
        KlondikeModel<KlondikeCard> model2 = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);

        assertNotSame("Should create different instances", model1, model2);
    }

    @Test
    public void testCreatedModelsWork() {
        KlondikeModel<KlondikeCard> basicModel = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
        KlondikeModel<KlondikeCard> whiteheadModel = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);

        assertNotNull(basicModel.createNewDeck());
        assertNotNull(whiteheadModel.createNewDeck());

        assertEquals(52, basicModel.createNewDeck().size());
        assertEquals(52, whiteheadModel.createNewDeck().size());
    }
}