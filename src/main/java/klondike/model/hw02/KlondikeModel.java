package klondike.model.hw02;

import java.util.List;

/**
 * Behaviors necessary to play a game of Klondike Solitaire.
 * The design of the interfaces abstracts the Card type in case
 * a different implementation needs a specific type of card.
 *
 * <p>The interface assumes the constructor of the implementation
 * cannot start the game. Instead, the user of this type must
 * start the game itself. As a result, most methods report
 * an IllegalStateException if the game has not been started.</p>
 *
 * @param <C> the type of card for the implementation
 */
public interface KlondikeModel<C extends Card> {
    /**
     * Return a valid and complete deck of cards for a game of Klondike.
     * No restrictions on order; implementing class determines validity.
     *
     * @return the deck of cards as a list
     */
    List<C> createNewDeck();

    /**
     * Deal a new game of Klondike.
     * - Uses given deck (optionally shuffled).
     * - Deals cards into cascade piles (triangular layout).
     * - Sets up foundation piles based on Aces in the deck.
     *
     * @param deck     the deck to be dealt
     * @param shuffle  if false, use deck as-is; if true, shuffle first
     * @param numPiles number of cascade piles
     * @param numDraw  number of draw cards visible at once
     * @throws IllegalStateException    if the game has already started
     * @throws IllegalArgumentException if inputs are invalid
     */
    void startGame(List<C> deck, boolean shuffle, int numPiles, int numDraw)
            throws IllegalArgumentException, IllegalStateException;

    /**
     * Move a group of cards from one pile to another (cascade move).
     *
     * @param srcPile  index of source pile
     * @param numCards number of cards to move
     * @param destPile index of destination pile
     */
    void movePile(int srcPile, int numCards, int destPile)
            throws IllegalArgumentException, IllegalStateException;

    /**
     * Move the top draw card to a pile.
     * If no draw cards remain, reveal next ones.
     *
     * @param destPile index of destination pile
     */
    void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException;

    /**
     * Move top card of a cascade pile to a foundation pile.
     *
     * @param srcPile index of source cascade pile
     * @param foundationPile index of destination foundation pile
     */
    void moveToFoundation(int srcPile, int foundationPile)
            throws IllegalArgumentException, IllegalStateException;

    /**
     * Move the top draw card directly to a foundation pile.
     *
     * @param foundationPile index of foundation pile
     */
    void moveDrawToFoundation(int foundationPile)
            throws IllegalArgumentException, IllegalStateException;

    /**
     * Discard the current top draw card.
     */
    void discardDraw() throws IllegalStateException;

    /**
     * @return number of rows in the current game layout
     */
    int getNumRows() throws IllegalStateException;

    /**
     * @return number of cascade piles in the game
     */
    int getNumPiles() throws IllegalStateException;

    /**
     * @return maximum number of draw cards visible at once
     */
    int getNumDraw() throws IllegalStateException;

    /**
     * @return true if game is over (no moves or draw cards left)
     */
    boolean isGameOver() throws IllegalStateException;

    /**
     * @return current score (sum of top cards in foundations)
     */
    int getScore() throws IllegalStateException;

    /**
     * @param pileNum index of cascade pile
     * @return number of cards in that pile
     */
    int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException;

    /**
     * @param pileNum cascade pile index
     * @param card    row index within that pile
     * @return card at given coordinates (if visible)
     */
    C getCardAt(int pileNum, int card) throws IllegalArgumentException, IllegalStateException;

    /**
     * @param foundationPile index of foundation pile
     * @return top card of that foundation pile (or null if empty)
     */
    C getCardAt(int foundationPile) throws IllegalArgumentException, IllegalStateException;

    /**
     * @param pileNum cascade pile index
     * @param card    row index in pile
     * @return true if card is face-up
     */
    boolean isCardVisible(int pileNum, int card)
            throws IllegalArgumentException, IllegalStateException;

    /**
     * @return current visible draw cards (up to getNumDraw).
     *         Returned list is a copy (modifications don’t affect model).
     */
    List<C> getDrawCards() throws IllegalStateException;

    /**
     * @return number of foundation piles in the game
     */
    int getNumFoundations() throws IllegalStateException;

}
