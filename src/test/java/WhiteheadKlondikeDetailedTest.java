package klondike;

import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.Suit;
import klondike.model.hw02.Value;
import klondike.model.hw04.WhiteheadKlondike;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Detailed test class for Whitehead-specific rules.
 */
public class WhiteheadKlondikeDetailedTest {
    private WhiteheadKlondike game;

    @Before
    public void setUp() {
        game = new WhiteheadKlondike();
    }

    @Test
    public void testCreateDeck() {
        List<KlondikeCard> deck = game.createNewDeck();
        assertEquals(52, deck.size());
    }

    @Test
    public void testCreateDeckHasAllSuits() {
        List<KlondikeCard> deck = game.createNewDeck();

        int hearts = 0, diamonds = 0, clubs = 0, spades = 0;
        for (KlondikeCard card : deck) {
            switch (card.getSuit()) {
                case HEARTS: hearts++; break;
                case DIAMONDS: diamonds++; break;
                case CLUBS: clubs++; break;
                case SPADES: spades++; break;
            }
        }

        assertEquals(13, hearts);
        assertEquals(13, diamonds);
        assertEquals(13, clubs);
        assertEquals(13, spades);
    }

    @Test
    public void testAllCardsVisibleAfterStart() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        // Check all cards in all piles are visible
        for (int pile = 0; pile < game.getNumPiles(); pile++) {
            int height = game.getPileHeight(pile);
            for (int cardIdx = 0; cardIdx < height; cardIdx++) {
                assertTrue("Card at pile " + pile + ", index " + cardIdx +
                                " should be visible in Whitehead",
                        game.isCardVisible(pile, cardIdx));
            }
        }
    }

    @Test
    public void testAllCardsVisibleInSmallGame() {
        List<KlondikeCard> deck = createSmallDeck();
        game.startGame(deck, false, 2, 0);

        // Pile 0: 1 card, Pile 1: 2 cards
        assertTrue("Card 0 in pile 0 should be visible",
                game.isCardVisible(0, 0));
        assertTrue("Card 0 in pile 1 should be visible",
                game.isCardVisible(1, 0));
        assertTrue("Card 1 in pile 1 should be visible",
                game.isCardVisible(1, 1));
    }

    @Test
    public void testSameColorMoveRedOnRed() {
        // Create deck: Red 3, Red 2, Red A to ensure same-color build
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.three));
        deck.add(new KlondikeCard(Suit.DIAMONDS, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.two));

        game.startGame(deck, false, 2, 0);

        // Pile 0: [3♡]
        // Pile 1: [A♢, 2♡]

        // Try to move 2♡ onto 3♡ (both red, 2 is one less than 3)
        try {
            game.movePile(1, 1, 0);
            assertEquals("Should successfully move red on red", 2, game.getPileHeight(0));
        } catch (IllegalArgumentException e) {
            fail("Same color move should be allowed in Whitehead: " + e.getMessage());
        }
    }

    @Test
    public void testSameColorMoveBlackOnBlack() {
        // Create deck: Black 5, Black A, Black 4
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.SPADES, Value.five));
        deck.add(new KlondikeCard(Suit.CLUBS, Value.A));
        deck.add(new KlondikeCard(Suit.SPADES, Value.four));

        game.startGame(deck, false, 2, 0);

        // Pile 0: [5♠]
        // Pile 1: [A♣, 4♠]

        // Try to move 4♠ onto 5♠ (both black, 4 is one less than 5)
        try {
            game.movePile(1, 1, 0);
            assertEquals("Should successfully move black on black", 2, game.getPileHeight(0));
        } catch (IllegalArgumentException e) {
            fail("Same color move should be allowed in Whitehead: " + e.getMessage());
        }
    }

    @Test
    public void testOppositeColorMoveNotAllowedRedOnBlack() {
        // Create deck where we try red on black
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.SPADES, Value.five));
        deck.add(new KlondikeCard(Suit.CLUBS, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.four));

        game.startGame(deck, false, 2, 0);

        // Pile 0: [5♠] (black)
        // Pile 1: [A♣, 4♡] (red)

        // Try to move 4♡ onto 5♠ (opposite colors)
        try {
            game.movePile(1, 1, 0);
            fail("Should not allow opposite color move in Whitehead");
        } catch (IllegalArgumentException e) {
            assertTrue("Should fail due to color mismatch",
                    e.getMessage().contains("Invalid move"));
        }
    }

    @Test
    public void testOppositeColorMoveNotAllowedBlackOnRed() {
        // Create deck where we try black on red
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.six));
        deck.add(new KlondikeCard(Suit.DIAMONDS, Value.A));
        deck.add(new KlondikeCard(Suit.CLUBS, Value.five));

        game.startGame(deck, false, 2, 0);

        // Pile 0: [6♡] (red)
        // Pile 1: [A♢, 5♣] (black)

        // Try to move 5♣ onto 6♡ (opposite colors)
        try {
            game.movePile(1, 1, 0);
            fail("Should not allow opposite color move in Whitehead");
        } catch (IllegalArgumentException e) {
            assertTrue("Should fail due to color mismatch",
                    e.getMessage().contains("Invalid move"));
        }
    }

    @Test
    public void testNonKingToEmptyPileAllowedWithAce() {
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.A));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.two));

        game.startGame(deck, false, 2, 0);

        // Pile 0: [A♡]
        // Pile 1: [A♠, 2♡]

        // Move Ace from pile 0 to foundation to empty the pile
        game.moveToFoundation(0, 0);
        assertEquals(0, game.getPileHeight(0));

        // Now move 2♡ (non-King) to empty pile 0
        try {
            game.movePile(1, 1, 0);
            assertEquals("Non-King should move to empty pile in Whitehead",
                    1, game.getPileHeight(0));
        } catch (IllegalArgumentException e) {
            fail("Any card should be able to move to empty pile in Whitehead: " +
                    e.getMessage());
        }
    }

    @Test
    public void testNonKingToEmptyPileAllowedWithFive() {
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.A));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.five));

        game.startGame(deck, false, 2, 0);

        // Move Ace from pile 0 to foundation to empty the pile
        game.moveToFoundation(0, 0);

        // Now move 5♡ to empty pile
        game.movePile(1, 1, 0);
        assertEquals("Five should move to empty pile in Whitehead",
                1, game.getPileHeight(0));
    }

    @Test
    public void testMultipleCardsSameSuitAllowed() {
        // Create deck with same-suit descending sequence
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.six));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.five));
        deck.add(new KlondikeCard(Suit.CLUBS, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.four));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.three));

        game.startGame(deck, false, 3, 0);

        // Pile 0: [6♡]
        // Pile 1: [A♠, 5♡, 4♡]
        // Pile 2: [A♣, 3♡]

        // Move 5♡ and 4♡ together (same suit) onto 6♡
        try {
            game.movePile(1, 2, 0);
            assertEquals("Should move multiple same-suit cards", 3, game.getPileHeight(0));
            assertEquals(1, game.getPileHeight(1));
        } catch (IllegalArgumentException e) {
            fail("Same suit cards should move together in Whitehead: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleCardsDifferentSuitNotAllowed() {
        // Create deck with different suits in the build
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.six));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.five));
        deck.add(new KlondikeCard(Suit.CLUBS, Value.A));
        deck.add(new KlondikeCard(Suit.DIAMONDS, Value.four));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.three));

        game.startGame(deck, false, 3, 0);

        // Pile 0: [6♡]
        // Pile 1: [A♠, 5♡, 4♢]  - different suits!
        // Pile 2: [A♣, 3♡]

        // Try to move 5♡ and 4♢ together (different suits)
        try {
            game.movePile(1, 2, 0);
            fail("Should not allow moving cards of different suits together");
        } catch (IllegalArgumentException e) {
            assertTrue("Should fail due to different suits",
                    e.getMessage().contains("Invalid"));
        }
    }

    @Test
    public void testSingleCardMoveAlwaysAllowedRegardlessOfSuit() {
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.five));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.DIAMONDS, Value.four));

        game.startGame(deck, false, 2, 0);

        // Pile 0: [5♡]
        // Pile 1: [A♠, 4♢]

        // Move single card 4♢ onto 5♡ (same color, different suit)
        game.movePile(1, 1, 0);
        assertEquals("Single card should move successfully", 2, game.getPileHeight(0));
    }

    @Test
    public void testFoundationMovesUnchangedAceMustGoFirst() {
        List<KlondikeCard> deck = createSmallDeck();
        game.startGame(deck, false, 2, 0);

        // Pile 0: [A♡]
        // Try to move Ace to foundation
        game.moveToFoundation(0, 0);
        assertEquals(1, game.getScore());
        assertNotNull(game.getCardAt(0));
    }

    @Test
    public void testFoundationMovesSequential() {
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.A));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.two));

        game.startGame(deck, false, 2, 0);

        // Move A♡ to foundation
        game.moveToFoundation(0, 0);
        assertEquals(1, game.getScore());

        // Move 2♡ to foundation
        game.moveToFoundation(1, 0);
        assertEquals(2, game.getScore());
    }

    @Test
    public void testFoundationRejectWrongSuit() {
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.A));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.SPADES, Value.two));

        game.startGame(deck, false, 2, 0);

        // Move A♡ to foundation 0
        game.moveToFoundation(0, 0);

        // Try to move 2♠ to foundation 0 (wrong suit)
        try {
            game.moveToFoundation(1, 0);
            fail("Should not allow wrong suit on foundation");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid foundation move"));
        }
    }

    @Test
    public void testFoundationRejectSkippingValues() {
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.A));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.three));

        game.startGame(deck, false, 2, 0);

        // Move A♡ to foundation
        game.moveToFoundation(0, 0);

        // Try to move 3♡ (skipping 2♡)
        try {
            game.moveToFoundation(1, 0);
            fail("Should not allow skipping values on foundation");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid foundation move"));
        }
    }

    @Test
    public void testDrawPileWorks() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        List<KlondikeCard> drawCards = game.getDrawCards();
        assertEquals(3, drawCards.size());

        KlondikeCard firstCard = drawCards.get(0);
        game.discardDraw();

        List<KlondikeCard> afterDiscard = game.getDrawCards();
        assertNotEquals("Draw cards should change after discard",
                firstCard.toString(), afterDiscard.get(0).toString());
    }

    @Test
    public void testDrawPileWithDifferentNumDraw() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 5);

        List<KlondikeCard> drawCards = game.getDrawCards();
        assertEquals(5, drawCards.size());
    }

    @Test
    public void testDiscardDrawMultipleTimes() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 5, 2);

        for (int i = 0; i < 10; i++) {
            game.discardDraw();
            assertEquals("Should always have draw cards", 2, game.getDrawCards().size());
        }
    }

    @Test
    public void testMoveDrawToFoundation() {
        List<KlondikeCard> deck = new ArrayList<>();
        // Put cards in draw pile
        deck.add(new KlondikeCard(Suit.HEARTS, Value.two));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.A));

        game.startGame(deck, false, 1, 2);

        // Pile 0: [2♡]
        // Draw: [A♠, A♡]

        // Move A♠ from draw to foundation
        game.moveDrawToFoundation(0);
        assertEquals(1, game.getScore());
    }

    @Test
    public void testMoveDrawToCascade() {
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.five));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.DIAMONDS, Value.four));

        game.startGame(deck, false, 1, 2);

        // Pile 0: [5♡]
        // Draw: [A♠, 4♢]

        // Move 4♢ from draw onto 5♡
        game.moveDraw(0);
        assertEquals(2, game.getPileHeight(0));
    }

    @Test
    public void testScoring() {
        List<KlondikeCard> deck = createSmallDeck();
        game.startGame(deck, false, 2, 0);

        assertEquals(0, game.getScore());

        game.moveToFoundation(0, 0);
        assertEquals(1, game.getScore());

        game.moveToFoundation(1, 0);
        assertEquals(2, game.getScore());
    }

    @Test
    public void testScoringMultipleFoundations() {
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.A));
        deck.add(new KlondikeCard(Suit.SPADES, Value.A));
        deck.add(new KlondikeCard(Suit.DIAMONDS, Value.A));

        game.startGame(deck, false, 2, 0);

        // Move aces to different foundations
        game.moveToFoundation(0, 0);  // A♡ to foundation 0
        assertEquals(1, game.getScore());

        game.moveToFoundation(1, 1);  // A♠ to foundation 1
        assertEquals(2, game.getScore());
    }

    @Test
    public void testGameNotOverAtStart() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        assertFalse("Game should not be over at start", game.isGameOver());
    }

    @Test
    public void testGameNotOverWithDrawCards() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        assertFalse("Game should not be over with draw cards available",
                game.isGameOver());
    }

    @Test
    public void testGetNumPiles() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        assertEquals(7, game.getNumPiles());
    }

    @Test
    public void testGetNumRows() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        assertEquals(7, game.getNumRows());
    }

    @Test
    public void testGetNumDraw() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        assertEquals(3, game.getNumDraw());
    }

    @Test
    public void testGetNumFoundations() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        assertEquals(4, game.getNumFoundations());
    }

    @Test
    public void testGetPileHeight() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);

        assertEquals(1, game.getPileHeight(0));
        assertEquals(2, game.getPileHeight(1));
        assertEquals(7, game.getPileHeight(6));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScoreBeforeStart() {
        game.getScore();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNumPilesBeforeStart() {
        game.getNumPiles();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPileHeightInvalidIndex() {
        List<KlondikeCard> deck = game.createNewDeck();
        game.startGame(deck, false, 7, 3);
        game.getPileHeight(10);
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

    @Test(expected = IllegalStateException.class)
    public void testDiscardDrawNoCards() {
        List<KlondikeCard> deck = createSmallDeck();
        game.startGame(deck, false, 2, 0);
        game.discardDraw();
    }

    @Test
    public void testValidDeckWithMultipleSuits() {
        List<KlondikeCard> deck = new ArrayList<>();

        // Add equal runs for two suits
        for (int i = 0; i < 3; i++) {
            deck.add(new KlondikeCard(Suit.HEARTS, Value.values()[i]));
            deck.add(new KlondikeCard(Suit.SPADES, Value.values()[i]));
        }

        game.startGame(deck, false, 2, 0);
        assertEquals(2, game.getNumFoundations());
    }

    // Helper methods
    private List<KlondikeCard> createSmallDeck() {
        List<KlondikeCard> deck = new ArrayList<>();
        deck.add(new KlondikeCard(Suit.HEARTS, Value.A));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.two));
        deck.add(new KlondikeCard(Suit.HEARTS, Value.three));
        return deck;
    }
}