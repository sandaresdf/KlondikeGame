package klondike.model.hw04;

import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.Suit;
import klondike.model.hw02.Value;

import java.util.List;

/**
 * Implementation of Whitehead Klondike variant.
 * Rules differ from basic:
 * - All cascade cards dealt face-up
 * - Builds must be same color (not alternating)
 * - Moving multiple cards requires same suit
 * - Any card can move to empty cascade pile
 */
public class WhiteheadKlondike extends AKlondike {

    /**
     * Creates a new Whitehead Klondike game.
     */
    public WhiteheadKlondike() {
        super();
    }

    @Override
    protected void setupInitialVisibility(int numPiles) {
        // In Whitehead, all cards are visible from the start
        for (int i = 0; i < cascadePiles.size(); i++) {
            cascadePiles.get(i).setNumVisible(cascadePiles.get(i).size());
        }
    }

    @Override
    protected boolean isValidCascadeMove(KlondikeCard cardToMove,
                                         KlondikeCard targetCard) {
        // Must be same color (not alternating)
        if (cardToMove.isRed() != targetCard.isRed()) {
            return false;
        }
        // Must be one value lower
        return cardToMove.getValue().rank == targetCard.getValue().rank - 1;
    }

    @Override
    protected boolean isValidBuildToMove(List<KlondikeCard> cards) {
        // When moving multiple cards, they must all be same suit
        if (cards.size() <= 1) {
            return true;
        }

        Suit firstSuit = cards.get(0).getSuit();
        for (KlondikeCard card : cards) {
            if (card.getSuit() != firstSuit) {
                return false;
            }
        }

        // Also verify they form a descending sequence
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getValue().rank != cards.get(i + 1).getValue().rank + 1) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected boolean canMoveToEmptyPile(KlondikeCard card) {
        // Any card can move to empty pile in Whitehead
        return true;
    }
}