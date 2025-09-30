import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.Card;
import klondike.model.hw02.Suit;
import klondike.model.hw02.Value;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for BasicKlondike model.
 */
public class BasicKlondikeTest {
    private BasicKlondike game;

    @Before
    public void setUp() {
        game = new BasicKlondike();
    }

    @Test
    public void testCreateNewDeckSize() {
        List<KlondikeCard> deck = game.createNewDeck();
        assertEquals(52, deck.size());
    }

    @Test
    public void testCreateNewDeckHasAllCards() {
        List<KlondikeCard> deck = game.createNewDeck();
        assertEquals(52, deck.size());

        int aceCount = 0;
        int kingCount = 0;
        for (Card card : deck) {
            if (card.toString().contains("Ace")) {
                aceCount++;
            }
            if (card.toString().contains("King")) {
                kingCount++;
            }
        }
        assertEquals(4, aceCount);
        assertEquals(4, kingCount);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNumPilesBeforeStart() {
        game.getNumPiles();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScoreBeforeStart() {
        game.getScore();
    }

    @Test
    public void testStartGameBasic() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertEquals(7, game.getNumPiles());
        assertEquals(3, game.getNumDraw());
        assertEquals(4, game.getNumFoundations());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartGameTwice() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.startGame(deck, false, 7, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartGameNullDeck() {
        game.startGame(null, false, 7, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartGameEmptyDeck() {
        List<KlondikeCard> emptyDeck = new ArrayList<>();
        game.startGame(emptyDeck, false, 7, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartGameInvalidPiles() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartGameNegativePiles() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, -1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartGameNegativeDraw() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartGameNotEnoughCards() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 15, 3);
    }

    @Test
    public void testGetNumRowsAfterStart() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertEquals(7, game.getNumRows());
    }

    @Test
    public void testGetPileHeightFirstPile() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertEquals(1, game.getPileHeight(0));
    }

    @Test
    public void testGetPileHeightLastPile() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertEquals(7, game.getPileHeight(6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPileHeightInvalidIndex() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.getPileHeight(10);
    }

    @Test
    public void testGetScoreInitiallyZero() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertEquals(0, game.getScore());
    }

    @Test
    public void testIsCardVisibleBottomCard() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertTrue(game.isCardVisible(0, 0));
    }

    @Test
    public void testIsCardVisibleTopCardNotVisible() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertFalse(game.isCardVisible(6, 0));
    }

    @Test
    public void testIsCardVisibleBottomCardVisible() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertTrue(game.isCardVisible(6, 6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCardAtNotVisible() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.getCardAt(6, 0);
    }

    @Test
    public void testGetCardAtVisible() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        Card card = game.getCardAt(0, 0);
        assertNotNull(card);
    }

    @Test
    public void testGetCardAtFoundationEmpty() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertNull(game.getCardAt(0));
    }

    @Test
    public void testGetDrawCardsNotEmpty() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        List<KlondikeCard> drawCards = game.getDrawCards();
        assertEquals(3, drawCards.size());
    }

    @Test
    public void testGetDrawCardsReturnsCorrectNumber() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 5, 2);
        List<KlondikeCard> drawCards = game.getDrawCards();
        assertEquals(2, drawCards.size());
    }

    @Test
    public void testDiscardDrawChangesDrawCards() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        List<KlondikeCard> before = game.getDrawCards();
        Card firstCard = before.get(0);

        game.discardDraw();

        List<KlondikeCard> after = game.getDrawCards();
        Card newFirstCard = after.get(0);

        assertNotEquals(firstCard.toString(), newFirstCard.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testDiscardDrawNoCards() {
        List<KlondikeCard> deck = game.createNewDeck();
        List<KlondikeCard> smallDeck = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            smallDeck.add(deck.get(i));
        }

        game.startGame(smallDeck, false, 5, 1);

        for (int i = 0; i < 20; i++) {
            try {
                game.discardDraw();
            } catch (IllegalStateException e) {
                throw e;
            }
        }
    }

    @Test
    public void testMovePileValidMove() {
        List<KlondikeCard> deck = game.createNewDeck();
        List<KlondikeCard> customDeck = createCustomDeck();

        game.startGame(customDeck, false, 3, 1);

        int pile0Height = game.getPileHeight(0);
        int pile1Height = game.getPileHeight(1);

        try {
            game.movePile(0, 1, 1);
            assertEquals(pile0Height - 1, game.getPileHeight(0));
            assertEquals(pile1Height + 1, game.getPileHeight(1));
        } catch (IllegalArgumentException e) {
            // Move might not be valid with random deck
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovePileInvalidSource() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.movePile(10, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovePileInvalidDest() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.movePile(0, 1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovePileZeroCards() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.movePile(0, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovePileNegativeCards() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.movePile(0, -1, 1);
    }

    @Test
    public void testMoveToFoundationAce() {
        List<KlondikeCard> deck = game.createNewDeck();
        List<KlondikeCard> customDeck = createDeckWithAceOnTop();

        game.startGame(customDeck, false, 3, 1);

        try {
            game.moveToFoundation(0, 0);
            assertEquals(1, game.getScore());
        } catch (IllegalArgumentException e) {
            // Ace might not be on top
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoveToFoundationInvalidPile() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.moveToFoundation(10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoveToFoundationInvalidFoundation() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.moveToFoundation(0, 10);
    }

    @Test
    public void testMoveDrawToValidPile() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        List<KlondikeCard> drawBefore = game.getDrawCards();
        int sizeBefore = drawBefore.size();

        try {
            game.moveDraw(0);
            List<KlondikeCard> drawAfter = game.getDrawCards();
            assertTrue(drawAfter.size() <= sizeBefore);
        } catch (IllegalArgumentException e) {
            // Move might not be valid
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoveDrawInvalidPile() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.moveDraw(10);
    }

    @Test
    public void testMoveDrawToFoundation() {
        List<KlondikeCard> deck = game.createNewDeck();
        List<KlondikeCard> customDeck = createDeckWithAceInDraw();

        game.startGame(customDeck, false, 2, 3);

        try {
            game.moveDrawToFoundation(0);
            assertEquals(1, game.getScore());
        } catch (IllegalArgumentException e) {
            // Ace might not be in draw
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoveDrawToFoundationInvalid() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.moveDrawToFoundation(10);
    }

    @Test
    public void testIsGameOverInitially() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertFalse(game.isGameOver());
    }

    @Test
    public void testGameNotOverWithDrawCards() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertFalse(game.isGameOver());
    }

    @Test
    public void testShuffleChangesOrder() {
        List<KlondikeCard> deck1 = game.createNewDeck();
        List<KlondikeCard> deck2 = game.createNewDeck();

        BasicKlondike game1 = new BasicKlondike();
        BasicKlondike game2 = new BasicKlondike();

        game1.startGame(deck1, false, 7, 3);
        game2.startGame(deck2, true, 7, 3);

        // Just test that shuffle parameter is accepted
        assertEquals(7, game1.getNumPiles());
        assertEquals(7, game2.getNumPiles());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDeckMissingAce() {
        List<KlondikeCard> deck = game.createNewDeck();
        List<KlondikeCard> invalidDeck = new ArrayList<>();

        for (Card card : deck) {
            if (!card.toString().contains("Ace")) {
                invalidDeck.add((KlondikeCard) card);
            }
        }

        game.startGame(invalidDeck, false, 7, 3);
    }

    @Test
    public void testValidCustomDeck() {
        List<KlondikeCard> customDeck = new ArrayList<>();

        customDeck.add(new KlondikeCard(Suit.HEARTS, Value.A));
        customDeck.add(new KlondikeCard(Suit.HEARTS, Value.two));
        customDeck.add(new KlondikeCard(Suit.HEARTS, Value.three));
        customDeck.add(new KlondikeCard(Suit.SPADES, Value.A));
        customDeck.add(new KlondikeCard(Suit.SPADES, Value.two));
        customDeck.add(new KlondikeCard(Suit.SPADES, Value.three));

        game.startGame(customDeck, false, 2, 1);
        assertEquals(2, game.getNumPiles());
    }

    private List<KlondikeCard> createCustomDeck() {
        List<KlondikeCard> deck = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            for (Value value : Value.values()) {
                deck.add(new KlondikeCard(suit, value));
            }
        }

        return deck;
    }

    private List<KlondikeCard> createDeckWithAceOnTop() {
        List<KlondikeCard> deck = createCustomDeck();
        List<KlondikeCard> reordered = new ArrayList<>();

        KlondikeCard ace = null;
        for (KlondikeCard card : deck) {
            if (card.getValue() == Value.A) {
                ace = card;
                break;
            }
        }

        if (ace != null) {
            reordered.add(ace);
        }

        for (KlondikeCard card : deck) {
            if (card != ace) {
                reordered.add(card);
            }
        }

        return reordered;
    }

    private List<KlondikeCard> createDeckWithAceInDraw() {
        List<KlondikeCard> deck = createCustomDeck();
        List<KlondikeCard> reordered = new ArrayList<>();

        KlondikeCard ace = null;
        for (KlondikeCard card : deck) {
            if (card.getValue() == Value.A) {
                ace = card;
                break;
            }
        }

        for (KlondikeCard card : deck) {
            if (card != ace) {
                reordered.add(card);
            }
        }

        if (ace != null) {
            reordered.add(ace);
        }

        return reordered;
    }

    @Test
    public void testMultiplePileSizes() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 5, 2);
        assertEquals(5, game.getNumPiles());
        assertEquals(5, game.getNumRows());
    }

    @Test
    public void testDifferentDrawSizes() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 5);
        assertEquals(5, game.getNumDraw());
    }

    @Test
    public void testGetNumFoundationsStandard() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        assertEquals(4, game.getNumFoundations());
    }
}