package klondike.model.hw02;

/**
 * Extended interface card that shows suit, value, and color.
 */
public interface VisCard {
    /**
     * Will get the suit of a card.
     * @return its suit
     */
    Suit getSuit();

    /**
     * Will get the value of a card.
     * @return teh card value
     */
    Value getValue();

    /**
     * Will determine if the card is red or not.
     * @return true or false if its red
     */
    boolean isRed();
}