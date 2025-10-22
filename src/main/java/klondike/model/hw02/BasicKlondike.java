//package klondike.model.hw02;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Implementation of Klondike Solitaire game model.
// */
//public class BasicKlondike implements KlondikeModel<KlondikeCard> {
//    private List<Pile> cascadePiles;
//    private List<Pile> foundationPiles;
//    private List<KlondikeCard> drawPile;
//    private int numDraw;
//    private boolean gameStarted;
//
//    /**
//     * Creates a new BasicKlondike game.
//     */
//    public BasicKlondike() {
//        this.gameStarted = false;
//    }
//
//    @Override
//    public List<KlondikeCard> createNewDeck() {
//        List<KlondikeCard> deck = new ArrayList<>();
//        for (Suit suit : Suit.values()) {
//            for (Value value : Value.values()) {
//                deck.add(new KlondikeCard(suit, value));
//            }
//        }
//        return deck;
//    }
//
//    @Override
//    public void startGame(List<KlondikeCard> deck, boolean shuffle, int numPiles, int numDraw) {
//
//        if (gameStarted) {
//            throw new IllegalStateException("Game already started");
//        }
//        if (deck == null || deck.isEmpty()) {
//            throw new IllegalArgumentException("Invalid deck");
//        }
//        if (numPiles <= 0 || numDraw < 0) {
//            throw new IllegalArgumentException("Invalid parameters");
//        }
//
//        validateDeck(deck);
//
//        int cardsNeeded = numPiles * (numPiles + 1) / 2;
//        if (deck.size() < cardsNeeded) {
//            throw new IllegalArgumentException("Not enough cards");
//        }
//
//        List<KlondikeCard> gameDeck = new ArrayList<>(deck);
//        if (shuffle) {
//            Collections.shuffle(gameDeck);
//        }
//
//        this.numDraw = numDraw;
//        this.cascadePiles = new ArrayList<>();
//        this.foundationPiles = new ArrayList<>();
//        this.drawPile = new ArrayList<>();
//
//        int numAces = countAces(deck);
//        for (int i = 0; i < numAces; i++) {
//            foundationPiles.add(new Pile());
//        }
//
//        int deckIndex = 0;
//        for (int i = 0; i < numPiles; i++) {
//            cascadePiles.add(new Pile());
//        }
//
//        for (int pile = 0; pile < numPiles; pile++) {
//            for (int count = 0; count <= pile; count++) {
//                cascadePiles.get(pile).addCard(gameDeck.get(deckIndex++));
//            }
//        }
//
//        for (Pile cascadePile : cascadePiles) {
//            cascadePile.setNumVisible(1);
//        }
//
//        while (deckIndex < gameDeck.size()) {
//            drawPile.add(gameDeck.get(deckIndex++));
//        }
//
//        this.gameStarted = true;
//    }
//
//    private void validateDeck(List<KlondikeCard> deck) {
//        List<Integer> runLengths = new ArrayList<>();
//
//        for (Suit suit : Suit.values()) {
//            List<KlondikeCard> suitCards = new ArrayList<>();
//            for (KlondikeCard card : deck) {
//                if (card.getSuit() == suit) {
//                    suitCards.add(card);
//                }
//            }
//
//            if (suitCards.isEmpty()) {
//                continue;
//            }
//
//            int[] valueCounts = new int[13];
//            for (KlondikeCard card : suitCards) {
//                valueCounts[card.getValue().rank - 1]++;
//            }
//
//            for (int count : valueCounts) {
//                if (count > 0) {
//                    runLengths.add(count);
//                }
//            }
//
//            if (valueCounts[0] == 0) {
//                throw new IllegalArgumentException("Run must start with Ace");
//            }
//
//            int runLength = 0;
//            for (int i = 0; i < valueCounts.length; i++) {
//                if (valueCounts[i] > 0) {
//                    runLength++;
//                } else if (runLength > 0) {
//                    break;
//                }
//            }
//        }
//
//        if (runLengths.isEmpty()) {
//            throw new IllegalArgumentException("Invalid deck");
//        }
//
//        int firstLength = runLengths.get(0);
//        for (int length : runLengths) {
//            if (length != firstLength) {
//                throw new IllegalArgumentException("Unequal run lengths");
//            }
//        }
//    }
//
//    private int countAces(List<KlondikeCard> deck) {
//        int count = 0;
//        for (KlondikeCard card : deck) {
//            if (card.getValue() == Value.A) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    @Override
//    public void movePile(int srcPile, int numCards, int destPile) {
//        checkGameStarted();
//        if (srcPile < 0 || srcPile >= cascadePiles.size()) {
//            throw new IllegalArgumentException("Invalid source pile");
//        }
//        if (destPile < 0 || destPile >= cascadePiles.size()) {
//            throw new IllegalArgumentException("Invalid dest pile");
//        }
//        if (numCards <= 0) {
//            throw new IllegalArgumentException("Invalid number of cards");
//        }
//
//        Pile source = cascadePiles.get(srcPile);
//        Pile dest = cascadePiles.get(destPile);
//
//        if (source.size() < numCards) {
//            throw new IllegalArgumentException("Not enough cards");
//        }
//
//        int startIndex = source.size() - numCards;
//        if (!source.isVisible(startIndex)) {
//            throw new IllegalArgumentException("Card not visible");
//        }
//
//        KlondikeCard topCard = source.getCard(startIndex);
//
//        if (dest.isEmpty()) {
//            if (topCard.getValue() != Value.K) {
//                throw new IllegalArgumentException("Must move King to empty");
//            }
//        } else {
//            KlondikeCard destBottom = dest.getLastCard();
//            if (!isValidCascadeMove(topCard, destBottom)) {
//                throw new IllegalArgumentException("Invalid move");
//            }
//        }
//
//        List<KlondikeCard> cardsToMove = new ArrayList<>();
//        for (int i = 0; i < numCards; i++) {
//            cardsToMove.add(source.removeLastCard());
//        }
//        Collections.reverse(cardsToMove);
//
//        for (KlondikeCard card : cardsToMove) {
//            dest.addCard(card);
//        }
//
//        dest.setNumVisible(dest.getNumVisible() + numCards);
//        source.setNumVisible(Math.max(0, source.getNumVisible() - numCards));
//
//        if (!source.isEmpty() && source.getNumVisible() == 0) {
//            source.setNumVisible(1);
//        }
//    }
//
//    private boolean isValidCascadeMove(KlondikeCard top, KlondikeCard bottom) {
//        if (top.isRed() == bottom.isRed()) {
//            return false;
//        }
//        return top.getValue().rank == bottom.getValue().rank - 1;
//    }
//
//    @Override
//    public void moveDraw(int destPile) {
//        checkGameStarted();
//        if (drawPile.isEmpty()) {
//            throw new IllegalStateException("No draw cards");
//        }
//        if (destPile < 0 || destPile >= cascadePiles.size()) {
//            throw new IllegalArgumentException("Invalid pile");
//        }
//
//        KlondikeCard card = drawPile.get(0);
//        Pile dest = cascadePiles.get(destPile);
//
//        if (dest.isEmpty()) {
//            if (card.getValue() != Value.K) {
//                throw new IllegalArgumentException("Must be King");
//            }
//        } else {
//            KlondikeCard destBottom = dest.getLastCard();
//            if (!isValidCascadeMove(card, destBottom)) {
//                throw new IllegalArgumentException("Invalid move");
//            }
//        }
//
//        drawPile.remove(0);
//        dest.addCard(card);
//        dest.setNumVisible(dest.getNumVisible() + 1);
//    }
//
//    @Override
//    public void moveToFoundation(int srcPile, int foundationPile) {
//        checkGameStarted();
//        if (srcPile < 0 || srcPile >= cascadePiles.size()) {
//            throw new IllegalArgumentException("Invalid source pile");
//        }
//        if (foundationPile < 0 || foundationPile >= foundationPiles.size()) {
//            throw new IllegalArgumentException("Invalid foundation");
//        }
//
//        Pile source = cascadePiles.get(srcPile);
//        if (source.isEmpty()) {
//            throw new IllegalArgumentException("Source is empty");
//        }
//
//        KlondikeCard card = source.getLastCard();
//        Pile foundation = foundationPiles.get(foundationPile);
//
//        if (!isValidFoundationMove(card, foundation)) {
//            throw new IllegalArgumentException("Invalid foundation move");
//        }
//
//        source.removeLastCard();
//        foundation.addCard(card);
//        foundation.setNumVisible(foundation.getNumVisible() + 1);
//
//        source.setNumVisible(Math.max(0, source.getNumVisible() - 1));
//        if (!source.isEmpty() && source.getNumVisible() == 0) {
//            source.setNumVisible(1);
//        }
//    }
//
//    private boolean isValidFoundationMove(KlondikeCard card, Pile foundation) {
//        if (foundation.isEmpty()) {
//            return card.getValue() == Value.A;
//        }
//
//        KlondikeCard top = foundation.getLastCard();
//        if (card.getSuit() != top.getSuit()) {
//            return false;
//        }
//        return card.getValue().rank == top.getValue().rank + 1;
//    }
//
//    @Override
//    public void moveDrawToFoundation(int foundationPile) {
//        checkGameStarted();
//        if (drawPile.isEmpty()) {
//            throw new IllegalStateException("No draw cards");
//        }
//        if (foundationPile < 0 || foundationPile >= foundationPiles.size()) {
//            throw new IllegalArgumentException("Invalid foundation");
//        }
//
//        KlondikeCard card = drawPile.get(0);
//        Pile foundation = foundationPiles.get(foundationPile);
//
//        if (!isValidFoundationMove(card, foundation)) {
//            throw new IllegalArgumentException("Invalid move");
//        }
//
//        drawPile.remove(0);
//        foundation.addCard(card);
//        foundation.setNumVisible(foundation.getNumVisible() + 1);
//    }
//
//    @Override
//    public void discardDraw() {
//        checkGameStarted();
//        if (drawPile.isEmpty()) {
//            throw new IllegalStateException("No draw cards");
//        }
//
//        KlondikeCard card = drawPile.remove(0);
//        drawPile.add(card);
//    }
//
//    @Override
//    public int getNumRows() {
//        checkGameStarted();
//        int max = 0;
//        for (Pile pile : cascadePiles) {
//            max = Math.max(max, pile.size());
//        }
//        return max;
//    }
//
//    @Override
//    public int getNumPiles() {
//        checkGameStarted();
//        return cascadePiles.size();
//    }
//
//    @Override
//    public int getNumDraw() {
//        checkGameStarted();
//        return numDraw;
//    }
//
//    @Override
//    public boolean isGameOver() {
//        checkGameStarted();
//        boolean val = hasValidMoves();
//        return !val;
//    }
//
//    private boolean hasValidMoves() {
//        for (int i = 0; i < cascadePiles.size(); i++) {
//            Pile pile = cascadePiles.get(i);
//            if (!pile.isEmpty()) {
//                KlondikeCard card = pile.getLastCard();
//
//                for (int j = 0; j < foundationPiles.size(); j++) {
//                    if (isValidFoundationMove(card, foundationPiles.get(j))) {
//                        return true;
//                    }
//                }
//
//                for (int j = 0; j < cascadePiles.size(); j++) {
//                    if (i != j) {
//                        Pile dest = cascadePiles.get(j);
//                        if (dest.isEmpty() && card.getValue() == Value.K) {
//                            return true;
//                        } else if (!dest.isEmpty()) {
//                            if (isValidCascadeMove(card, dest.getLastCard())) {
//                                return true;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        if (!drawPile.isEmpty()) {
//            return true;
//        }
//
//        return false;
//    }
//
//    @Override
//    public int getScore() {
//        checkGameStarted();
//        int score = 0;
//        for (Pile foundation : foundationPiles) {
//            score += foundation.size();
//        }
//        return score;
//    }
//
//    @Override
//    public int getPileHeight(int pileNum) {
//        checkGameStarted();
//        if (pileNum < 0 || pileNum >= cascadePiles.size()) {
//            throw new IllegalArgumentException("Invalid pile");
//        }
//        return cascadePiles.get(pileNum).size();
//    }
//
//    @Override
//    public KlondikeCard getCardAt(int pileNum, int card) {
//        checkGameStarted();
//        if (pileNum < 0 || pileNum >= cascadePiles.size()) {
//            throw new IllegalArgumentException("Invalid pile");
//        }
//        Pile pile = cascadePiles.get(pileNum);
//        if (card < 0 || card >= pile.size()) {
//            throw new IllegalArgumentException("Invalid card index");
//        }
//        if (!pile.isVisible(card)) {
//            throw new IllegalArgumentException("Card not visible");
//        }
//        return pile.getCard(card);
//    }
//
//    @Override
//    public KlondikeCard getCardAt(int foundationPile) {
//        checkGameStarted();
//        if (foundationPile < 0 || foundationPile >= foundationPiles.size()) {
//            throw new IllegalArgumentException("Invalid foundation");
//        }
//        return foundationPiles.get(foundationPile).getLastCard();
//    }
//
//    @Override
//    public boolean isCardVisible(int pileNum, int card) {
//        checkGameStarted();
//        if (pileNum < 0 || pileNum >= cascadePiles.size()) {
//            throw new IllegalArgumentException("Invalid pile");
//        }
//        Pile pile = cascadePiles.get(pileNum);
//        if (card < 0 || card >= pile.size()) {
//            throw new IllegalArgumentException("Invalid card");
//        }
//        return pile.isVisible(card);
//    }
//
//    @Override
//    public List<KlondikeCard> getDrawCards() {
//        checkGameStarted();
//        List<KlondikeCard> visible = new ArrayList<>();
//        for (int i = 0; i < Math.min(numDraw, drawPile.size()); i++) {
//            visible.add(drawPile.get(i));
//        }
//        return visible;
//    }
//
//    @Override
//    public int getNumFoundations() {
//        checkGameStarted();
//        return foundationPiles.size();
//    }
//
//    private void checkGameStarted() {
//        if (!gameStarted) {
//            throw new IllegalStateException("Game not started");
//        }
//    }
//}

package klondike.model.hw02;

import klondike.model.hw04.AKlondike;

import java.util.List;

/**
 * Implementation of basic Klondike Solitaire game model.
 */
public class BasicKlondike extends AKlondike {

    /**
     * Creates a new BasicKlondike game.
     */
    public BasicKlondike() {
        super();
    }

    @Override
    protected void setupInitialVisibility(int numPiles) {
        // In basic Klondike, only bottom card of each pile is visible
        for (int i = 0; i < cascadePiles.size(); i++) {
            cascadePiles.get(i).setNumVisible(1);
        }
    }

    @Override
    protected boolean isValidCascadeMove(KlondikeCard cardToMove,
                                         KlondikeCard targetCard) {
        // Must be opposite color
        if (cardToMove.isRed() == targetCard.isRed()) {
            return false;
        }
        // Must be one value lower
        return cardToMove.getValue().rank == targetCard.getValue().rank - 1;
    }

    @Override
    protected boolean isValidBuildToMove(List<KlondikeCard> cards) {
        // In basic Klondike, cards just need to be visible (already checked)
        // They don't need to be same suit
        return true;
    }

    @Override
    protected boolean canMoveToEmptyPile(KlondikeCard card) {
        // Only King can move to empty pile in basic Klondike
        return card.getValue() == Value.K;
    }
}