/*
 * /src/main/java/klondike/model/hw02/KlondikeCard.java
 */

package klondike.model.hw02;

import java.util.Objects;

/**
 * [be sure to add this later].
 */
public class KlondikeCard implements VisCard, Card{
    private final Suit suit;
    private final Value value;

    /**
     * [OH WE NEED ONE HERE TOO]
     * @param suit ADD THIS
     * @param value ADD THIS
     */
    public KlondikeCard(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    @Override
    public Suit getSuit() {
        return suit;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public boolean isRed() {
        return suit.red;
    }

    /**
     * Converts the card into a string.
     * @return the string value and suit
     */
    @Override
    public String toString() {
        return value.label + suit.symbol;
    }

    /**
     * [equality! addlater].
     * @param o yeah add
     * @return yeah add
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KlondikeCard)) {
            return false;
        }
        KlondikeCard that = (KlondikeCard) o;
        return suit == that.suit && value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, value);
    }
}
