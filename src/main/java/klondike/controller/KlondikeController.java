package klondike.controller;

import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

import java.util.List;

/**
 * Controller interface for Klondike Solitaire game.
 */
public interface KlondikeController {
    /**
     * Plays a new game of Klondike using the provided model.
     *
     * @param model the game model
     * @param deck the deck of cards to use
     * @param shuffle whether to shuffle the deck
     * @param numPiles number of cascade piles
     * @param numDraw number of draw cards visible
     * @param <C> the type of card
     * @throws IllegalArgumentException if the model is null
     * @throws IllegalStateException if the controller cannot read input or write output
     */
    <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck,
                                   boolean shuffle, int numPiles, int numDraw);
}