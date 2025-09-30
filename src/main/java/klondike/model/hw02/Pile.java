package klondike.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pile of cards in the Klondike game.
 * Helps manage card visibility and pile operations.
 */
class Pile {
    private final List<KlondikeCard> cards;
    private int numVisible;

    /**
     * Creates an empty pile.
     */
    public Pile() {
        this.cards = new ArrayList<>();
        this.numVisible = 0;
    }

    /**
     * Adds a card to the pile.
     * @param card the card to add
     */
    public void addCard(KlondikeCard card) {
        cards.add(card);
    }

    /**
     * Gets the number of cards in the pile.
     * @return the size
     */
    public int size() {
        return cards.size();
    }

    /**
     * Gets card at specific index.
     * @param index the index
     * @return the card
     */
    public KlondikeCard getCard(int index) {
        return cards.get(index);
    }

    /**
     * Removes the last card from pile.
     * @return the removed card
     */
    public KlondikeCard removeLastCard() {
        return cards.remove(cards.size() - 1);
    }

    /**
     * Gets the last card without removing.
     * @return the last card or null if empty
     */
    public KlondikeCard getLastCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(cards.size() - 1);
    }

    /**
     * Checks if card at index is visible.
     * @param cardIndex the index
     * @return true if visible
     */
    public boolean isVisible(int cardIndex) {
        return cardIndex >= cards.size() - numVisible;
    }

    /**
     * Sets number of visible cards.
     * @param num the number
     */
    public void setNumVisible(int num) {
        this.numVisible = num;
    }

    /**
     * Gets number of visible cards.
     * @return the count
     */
    public int getNumVisible() {
        return numVisible;
    }

    /**
     * Checks if pile is empty.
     * @return true if empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}