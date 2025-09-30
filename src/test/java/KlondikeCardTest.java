import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.Suit;
import klondike.model.hw02.Value;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for KlondikeCard.
 */
public class KlondikeCardTest {

    @Test
    public void testGetSuit() {
        KlondikeCard card = new KlondikeCard(Suit.HEARTS, Value.A);
        assertEquals(Suit.HEARTS, card.getSuit());

        KlondikeCard card2 = new KlondikeCard(Suit.SPADES, Value.K);
        assertEquals(Suit.SPADES, card2.getSuit());
    }

    @Test
    public void testGetValue() {
        KlondikeCard card = new KlondikeCard(Suit.HEARTS, Value.A);
        assertEquals(Value.A, card.getValue());

        KlondikeCard card2 = new KlondikeCard(Suit.CLUBS, Value.ten);
        assertEquals(Value.ten, card2.getValue());
    }

    @Test
    public void testIsRedHearts() {
        KlondikeCard card = new KlondikeCard(Suit.HEARTS, Value.five);
        assertTrue(card.isRed());
    }

    @Test
    public void testIsRedDiamonds() {
        KlondikeCard card = new KlondikeCard(Suit.DIAMONDS, Value.Q);
        assertTrue(card.isRed());
    }

    @Test
    public void testIsRedClubsFalse() {
        KlondikeCard card = new KlondikeCard(Suit.CLUBS, Value.seven);
        assertFalse(card.isRed());
    }

    @Test
    public void testIsRedSpadesFalse() {
        KlondikeCard card = new KlondikeCard(Suit.SPADES, Value.J);
        assertFalse(card.isRed());
    }

    @Test
    public void testToStringAce() {
        KlondikeCard card = new KlondikeCard(Suit.HEARTS, Value.A);
        assertEquals("Ace♡", card.toString());
    }

    @Test
    public void testToStringNumberCard() {
        KlondikeCard card = new KlondikeCard(Suit.SPADES, Value.five);
        assertEquals("5♠", card.toString());
    }

    @Test
    public void testToStringTenCard() {
        KlondikeCard card = new KlondikeCard(Suit.DIAMONDS, Value.ten);
        assertEquals("10♢", card.toString());
    }

    @Test
    public void testToStringJack() {
        KlondikeCard card = new KlondikeCard(Suit.CLUBS, Value.J);
        assertEquals("Jack♣", card.toString());
    }

    @Test
    public void testToStringQueen() {
        KlondikeCard card = new KlondikeCard(Suit.HEARTS, Value.Q);
        assertEquals("Queen♡", card.toString());
    }

    @Test
    public void testToStringKing() {
        KlondikeCard card = new KlondikeCard(Suit.SPADES, Value.K);
        assertEquals("King♠", card.toString());
    }

    @Test
    public void testEqualsSameCard() {
        KlondikeCard card1 = new KlondikeCard(Suit.HEARTS, Value.A);
        KlondikeCard card2 = new KlondikeCard(Suit.HEARTS, Value.A);
        assertEquals(card1, card2);
    }

    @Test
    public void testEqualsSameObject() {
        KlondikeCard card = new KlondikeCard(Suit.CLUBS, Value.K);
        assertEquals(card, card);
    }

    @Test
    public void testEqualsDifferentSuit() {
        KlondikeCard card1 = new KlondikeCard(Suit.HEARTS, Value.five);
        KlondikeCard card2 = new KlondikeCard(Suit.SPADES, Value.five);
        assertNotEquals(card1, card2);
    }

    @Test
    public void testEqualsDifferentValue() {
        KlondikeCard card1 = new KlondikeCard(Suit.DIAMONDS, Value.two);
        KlondikeCard card2 = new KlondikeCard(Suit.DIAMONDS, Value.three);
        assertNotEquals(card1, card2);
    }

    @Test
    public void testEqualsDifferentBoth() {
        KlondikeCard card1 = new KlondikeCard(Suit.HEARTS, Value.A);
        KlondikeCard card2 = new KlondikeCard(Suit.CLUBS, Value.K);
        assertNotEquals(card1, card2);
    }

    @Test
    public void testEqualsNull() {
        KlondikeCard card = new KlondikeCard(Suit.SPADES, Value.seven);
        assertNotEquals(card, null);
    }

    @Test
    public void testEqualsDifferentClass() {
        KlondikeCard card = new KlondikeCard(Suit.HEARTS, Value.Q);
        String notACard = "Not a card";
        assertNotEquals(card, notACard);
    }

    @Test
    public void testHashCodeSameCards() {
        KlondikeCard card1 = new KlondikeCard(Suit.DIAMONDS, Value.J);
        KlondikeCard card2 = new KlondikeCard(Suit.DIAMONDS, Value.J);
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    public void testHashCodeDifferentCards() {
        KlondikeCard card1 = new KlondikeCard(Suit.HEARTS, Value.A);
        KlondikeCard card2 = new KlondikeCard(Suit.SPADES, Value.K);
        assertNotEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    public void testAllSuits() {
        for (Suit suit : Suit.values()) {
            KlondikeCard card = new KlondikeCard(suit, Value.A);
            assertEquals(suit, card.getSuit());
        }
    }

    @Test
    public void testAllValues() {
        for (Value value : Value.values()) {
            KlondikeCard card = new KlondikeCard(Suit.HEARTS, value);
            assertEquals(value, card.getValue());
        }
    }

    @Test
    public void testCardColorConsistency() {
        KlondikeCard heartCard = new KlondikeCard(Suit.HEARTS, Value.A);
        KlondikeCard diamondCard = new KlondikeCard(Suit.DIAMONDS, Value.A);
        KlondikeCard clubCard = new KlondikeCard(Suit.CLUBS, Value.A);
        KlondikeCard spadeCard = new KlondikeCard(Suit.SPADES, Value.A);

        assertTrue(heartCard.isRed());
        assertTrue(diamondCard.isRed());
        assertFalse(clubCard.isRed());
        assertFalse(spadeCard.isRed());
    }
}