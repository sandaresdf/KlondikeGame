package klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of Klondike Solitaire game model.
 */
public class BasicKlondike implements KlondikeModel<KlondikeCard> {
    private List<Pile> cascadePiles;
    private List<Pile> foundationPiles;
    private List<KlondikeCard> drawPile;
    private int numDraw;
    private boolean gameStarted;

    /**
     * Creates a new BasicKlondike game.
     */
    public BasicKlondike() {
        this.gameStarted = false;
    }

    @Override
    public List<KlondikeCard> createNewDeck() {
        List<KlondikeCard> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Value value : Value.values()) {
                deck.add(new KlondikeCard(suit, value));
            }
        }
        return deck;
    }

    @Override
    public void startGame(List<KlondikeCard> deck, boolean shuffle, int numPiles, int numDraw) {

        if (gameStarted) {
            throw new IllegalStateException("Game already started");
        }
        if (deck == null || deck.isEmpty()) {
            throw new IllegalArgumentException("Invalid deck");
        }
        if (numPiles <= 0 || numDraw < 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        validateDeck(deck);

        int cardsNeeded = numPiles * (numPiles + 1) / 2;
        if (deck.size() < cardsNeeded) {
            throw new IllegalArgumentException("Not enough cards");
        }

        List<KlondikeCard> gameDeck = new ArrayList<>(deck);
        if (shuffle) {
            Collections.shuffle(gameDeck);
        }

        this.numDraw = numDraw;
        this.cascadePiles = new ArrayList<>();
        this.foundationPiles = new ArrayList<>();
        this.drawPile = new ArrayList<>();

        int numAces = countAces(deck);
        for (int i = 0; i < numAces; i++) {
            foundationPiles.add(new Pile());
        }

        int deckIndex = 0;
        for (int i = 0; i < numPiles; i++) {
            cascadePiles.add(new Pile());
        }

        for (int row = 0; row < numPiles; row++) {
            for (int pile = row; pile < numPiles; pile++) {
                cascadePiles.get(pile).addCard(gameDeck.get(deckIndex++));
            }
        }

        for (Pile cascadePile : cascadePiles) {
            cascadePile.setNumVisible(1);
        }

        while (deckIndex < gameDeck.size()) {
            drawPile.add(gameDeck.get(deckIndex++));
        }

        this.gameStarted = true;
    }

    private void validateDeck(List<KlondikeCard> deck) {
        List<Integer> runLengths = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            List<KlondikeCard> suitCards = new ArrayList<>();
            for (KlondikeCard card : deck) {
                if (card.getSuit() == suit) {
                    suitCards.add(card);
                }
            }

            if (suitCards.isEmpty()) {
                continue;
            }

            int[] valueCounts = new int[13];
            for (KlondikeCard card : suitCards) {
                valueCounts[card.getValue().rank - 1]++;
            }

            for (int count : valueCounts) {
                if (count > 0) {
                    runLengths.add(count);
                }
            }

            if (valueCounts[0] == 0) {
                throw new IllegalArgumentException("Run must start with Ace");
            }

            int runLength = 0;
            for (int i = 0; i < valueCounts.length; i++) {
                if (valueCounts[i] > 0) {
                    runLength++;
                } else if (runLength > 0) {
                    break;
                }
            }
        }

        if (runLengths.isEmpty()) {
            throw new IllegalArgumentException("Invalid deck");
        }

        int firstLength = runLengths.get(0);
        for (int length : runLengths) {
            if (length != firstLength) {
                throw new IllegalArgumentException("Unequal run lengths");
            }
        }
    }

    private int countAces(List<KlondikeCard> deck) {
        int count = 0;
        for (KlondikeCard card : deck) {
            if (card.getValue() == Value.A) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void movePile(int srcPile, int numCards, int destPile) {
        checkGameStarted();
        if (srcPile < 0 || srcPile >= cascadePiles.size()) {
            throw new IllegalArgumentException("Invalid source pile");
        }
        if (destPile < 0 || destPile >= cascadePiles.size()) {
            throw new IllegalArgumentException("Invalid dest pile");
        }
        if (numCards <= 0) {
            throw new IllegalArgumentException("Invalid number of cards");
        }

        Pile source = cascadePiles.get(srcPile);
        Pile dest = cascadePiles.get(destPile);

        if (source.size() < numCards) {
            throw new IllegalArgumentException("Not enough cards");
        }

        int startIndex = source.size() - numCards;
        if (!source.isVisible(startIndex)) {
            throw new IllegalArgumentException("Card not visible");
        }

        KlondikeCard topCard = source.getCard(startIndex);

        if (dest.isEmpty()) {
            if (topCard.getValue() != Value.K) {
                throw new IllegalArgumentException("Must move King to empty");
            }
        } else {
            KlondikeCard destBottom = dest.getLastCard();
            if (!isValidCascadeMove(topCard, destBottom)) {
                throw new IllegalArgumentException("Invalid move");
            }
        }

        List<KlondikeCard> cardsToMove = new ArrayList<>();
        for (int i = 0; i < numCards; i++) {
            cardsToMove.add(source.removeLastCard());
        }
        Collections.reverse(cardsToMove);

        for (KlondikeCard card : cardsToMove) {
            dest.addCard(card);
        }

        dest.setNumVisible(dest.getNumVisible() + numCards);
        source.setNumVisible(Math.max(0, source.getNumVisible() - numCards));

        if (!source.isEmpty() && source.getNumVisible() == 0) {
            source.setNumVisible(1);
        }
    }

    private boolean isValidCascadeMove(KlondikeCard top, KlondikeCard bottom) {
        if (top.isRed() == bottom.isRed()) {
            return false;
        }
        return top.getValue().rank == bottom.getValue().rank - 1;
    }

    @Override
    public void moveDraw(int destPile) {
        checkGameStarted();
        if (drawPile.isEmpty()) {
            throw new IllegalStateException("No draw cards");
        }
        if (destPile < 0 || destPile >= cascadePiles.size()) {
            throw new IllegalArgumentException("Invalid pile");
        }

        KlondikeCard card = drawPile.get(0);
        Pile dest = cascadePiles.get(destPile);

        if (dest.isEmpty()) {
            if (card.getValue() != Value.K) {
                throw new IllegalArgumentException("Must be King");
            }
        } else {
            KlondikeCard destBottom = dest.getLastCard();
            if (!isValidCascadeMove(card, destBottom)) {
                throw new IllegalArgumentException("Invalid move");
            }
        }

        drawPile.remove(0);
        dest.addCard(card);
        dest.setNumVisible(dest.getNumVisible() + 1);
    }

    @Override
    public void moveToFoundation(int srcPile, int foundationPile) {
        checkGameStarted();
        if (srcPile < 0 || srcPile >= cascadePiles.size()) {
            throw new IllegalArgumentException("Invalid source pile");
        }
        if (foundationPile < 0 || foundationPile >= foundationPiles.size()) {
            throw new IllegalArgumentException("Invalid foundation");
        }

        Pile source = cascadePiles.get(srcPile);
        if (source.isEmpty()) {
            throw new IllegalArgumentException("Source is empty");
        }

        KlondikeCard card = source.getLastCard();
        Pile foundation = foundationPiles.get(foundationPile);

        if (!isValidFoundationMove(card, foundation)) {
            throw new IllegalArgumentException("Invalid foundation move");
        }

        source.removeLastCard();
        foundation.addCard(card);
        foundation.setNumVisible(foundation.getNumVisible() + 1);

        source.setNumVisible(Math.max(0, source.getNumVisible() - 1));
        if (!source.isEmpty() && source.getNumVisible() == 0) {
            source.setNumVisible(1);
        }
    }

    private boolean isValidFoundationMove(KlondikeCard card, Pile foundation) {
        if (foundation.isEmpty()) {
            return card.getValue() == Value.A;
        }

        KlondikeCard top = foundation.getLastCard();
        if (card.getSuit() != top.getSuit()) {
            return false;
        }
        return card.getValue().rank == top.getValue().rank + 1;
    }

    @Override
    public void moveDrawToFoundation(int foundationPile) {
        checkGameStarted();
        if (drawPile.isEmpty()) {
            throw new IllegalStateException("No draw cards");
        }
        if (foundationPile < 0 || foundationPile >= foundationPiles.size()) {
            throw new IllegalArgumentException("Invalid foundation");
        }

        KlondikeCard card = drawPile.get(0);
        Pile foundation = foundationPiles.get(foundationPile);

        if (!isValidFoundationMove(card, foundation)) {
            throw new IllegalArgumentException("Invalid move");
        }

        drawPile.remove(0);
        foundation.addCard(card);
        foundation.setNumVisible(foundation.getNumVisible() + 1);
    }

    @Override
    public void discardDraw() {
        checkGameStarted();
        if (drawPile.isEmpty()) {
            throw new IllegalStateException("No draw cards");
        }

        KlondikeCard card = drawPile.remove(0);
        drawPile.add(card);
    }

    @Override
    public int getNumRows() {
        checkGameStarted();
        int max = 0;
        for (Pile pile : cascadePiles) {
            max = Math.max(max, pile.size());
        }
        return max;
    }

    @Override
    public int getNumPiles() {
        checkGameStarted();
        return cascadePiles.size();
    }

    @Override
    public int getNumDraw() {
        checkGameStarted();
        return numDraw;
    }

    @Override
    public boolean isGameOver() {
        checkGameStarted();
        return !hasValidMoves();
    }

    private boolean hasValidMoves() {
        for (int i = 0; i < cascadePiles.size(); i++) {
            Pile pile = cascadePiles.get(i);
            if (!pile.isEmpty()) {
                KlondikeCard card = pile.getLastCard();

                for (int j = 0; j < foundationPiles.size(); j++) {
                    if (isValidFoundationMove(card, foundationPiles.get(j))) {
                        return true;
                    }
                }

                for (int j = 0; j < cascadePiles.size(); j++) {
                    if (i != j) {
                        Pile dest = cascadePiles.get(j);
                        if (dest.isEmpty() && card.getValue() == Value.K) {
                            return true;
                        } else if (!dest.isEmpty()) {
                            if (isValidCascadeMove(card, dest.getLastCard())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        if (!drawPile.isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public int getScore() {
        checkGameStarted();
        int score = 0;
        for (Pile foundation : foundationPiles) {
            score += foundation.size();
        }
        return score;
    }

    @Override
    public int getPileHeight(int pileNum) {
        checkGameStarted();
        if (pileNum < 0 || pileNum >= cascadePiles.size()) {
            throw new IllegalArgumentException("Invalid pile");
        }
        return cascadePiles.get(pileNum).size();
    }

    @Override
    public KlondikeCard getCardAt(int pileNum, int card) {
        checkGameStarted();
        if (pileNum < 0 || pileNum >= cascadePiles.size()) {
            throw new IllegalArgumentException("Invalid pile");
        }
        Pile pile = cascadePiles.get(pileNum);
        if (card < 0 || card >= pile.size()) {
            throw new IllegalArgumentException("Invalid card index");
        }
        if (!pile.isVisible(card)) {
            throw new IllegalArgumentException("Card not visible");
        }
        return pile.getCard(card);
    }

    @Override
    public KlondikeCard getCardAt(int foundationPile) {
        checkGameStarted();
        if (foundationPile < 0 || foundationPile >= foundationPiles.size()) {
            throw new IllegalArgumentException("Invalid foundation");
        }
        return foundationPiles.get(foundationPile).getLastCard();
    }

    @Override
    public boolean isCardVisible(int pileNum, int card) {
        checkGameStarted();
        if (pileNum < 0 || pileNum >= cascadePiles.size()) {
            throw new IllegalArgumentException("Invalid pile");
        }
        Pile pile = cascadePiles.get(pileNum);
        if (card < 0 || card >= pile.size()) {
            throw new IllegalArgumentException("Invalid card");
        }
        return pile.isVisible(card);
    }

    @Override
    public List<KlondikeCard> getDrawCards() {
        checkGameStarted();
        List<KlondikeCard> visible = new ArrayList<>();
        for (int i = 0; i < Math.min(numDraw, drawPile.size()); i++) {
            visible.add(drawPile.get(i));
        }
        return visible;
    }

    @Override
    public int getNumFoundations() {
        checkGameStarted();
        return foundationPiles.size();
    }

    private void checkGameStarted() {
        if (!gameStarted) {
            throw new IllegalStateException("Game not started");
        }
    }
}

//package klondike.model.hw02;
//
//import java.util.*;
//
///**
// * Implementation of the KlondikeModel interface.
// * [ADD MORE]
// */
//public class BasicKlondike implements KlondikeModel<Card> {
//    private boolean started = false;
//    private List<List<Slot>> cascades = new ArrayList<>();
//    private List<Deque<KlondikeCard>> foundations = new ArrayList<>();
//    private Deque<KlondikeCard> drawPile = new ArrayDeque<>();
//    private LinkedList<KlondikeCard> visibleDraw = new LinkedList<>();
//    private int numDraw;
//    private int score = 0;
//
//
//    @Override
//    public List<Card> createNewDeck() {
//        List<Card> deck = new ArrayList<>();
//        for (Suit s : Suit.values()) {
//            for (Value v : Value.values()) {
//                deck.add(new KlondikeCard(s, v));
//            }
//        }
//        return deck;
//    }
//
//    private static class Slot {
//        final KlondikeCard card;
//        boolean faceUp;
//
//        Slot(KlondikeCard c, boolean faceUp) {
//            this.card = c;
//            this.faceUp = faceUp;
//        }
//    }
//
//    @Override
//    public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
//        if (started) {
//            throw new IllegalStateException("Game already started");
//        }
//        if (deck == null || numPiles <= 0 || numDraw <= 0) {
//            throw new IllegalArgumentException("Invalid inputs");
//        }
//
//        int needed = numPiles * (numPiles + 1) / 2;
//        if (deck.size() < needed) {
//            throw new IllegalArgumentException("not enough cards for triangle");
//        }
//
//        this.numDraw = numDraw;
//        List<KlondikeCard> d = new ArrayList<>(deck.size());
//        for (Card c : deck) {
//            d.add((KlondikeCard) c); // tests pass createNewDeck(), so this cast is fine here
//        }
//
////        // --- Deck validation: must be equal-length, unbroken runs starting at Ace ---
////        Map<Suit, List<Value>> runs = new HashMap<>();
////        for (KlondikeCard c : d) {
////            runs.computeIfAbsent(c.getSuit(), k -> new ArrayList<>()).add(c.getValue());
////        }
////
////        // check all suits have equal run length
////        int runLength = -1;
////        for (Map.Entry<Suit, List<Value>> e : runs.entrySet()) {
////            List<Value> values = e.getValue();
////            values.sort(Comparator.comparingInt(v -> v.rank));
////
////            // must start at Ace
////            if (values.isEmpty() || values.get(0) != Value.A) {
////                throw new IllegalArgumentException("Runs must start at Ace");
////            }
////
////            // must be consecutive
////            for (int i = 1; i < values.size(); i++) {
////                if (values.get(i).rank != values.get(i - 1).rank + 1) {
////                    throw new IllegalArgumentException("Run is broken");
////                }
////            }
////
////            if (runLength == -1) {
////                runLength = values.size();
////            } else if (values.size() != runLength) {
////                throw new IllegalArgumentException("Unequal run lengths across suits");
////            }
////        }
//
//        if (shuffle) {
//            Collections.shuffle(d);
//        }
//
//        cascades.clear();
//        foundations.clear();
//        drawPile.clear();
//        visibleDraw.clear();
//
//        for (int i = 0; i < numPiles; i++) {
//            cascades.add(new ArrayList<>());
//        }
//
//        int index = 0;
//        for (int pile = 0; pile < numPiles; pile++) {
//            for (int j = 0; j <= pile; j++) {
//                KlondikeCard c = d.get(index++);
//                cascades.get(pile).add(new Slot(c, j == pile));
//            }
//        }
//
//        while (index < d.size()) {
//            drawPile.addLast(d.get(index++));
//        }
////        System.out.println(drawPile.toString());
//
//        // Exactly four foundations for standard Klondike
//        for (int i = 0; i < 4; i++) {
//            foundations.add(new ArrayDeque<>());
//        }
//
//        score = 0;
//        started = true;
//
//        refreshDraw();
//    }
//
//    @Override
//    public void movePile(int srcPile, int numCards, int destPile) {
//        ensureStarted();
//        requirePileIndex(srcPile);
//        requirePileIndex(destPile);
//        List<Slot> src = cascades.get(srcPile);
//        if (numCards <= 0 || numCards > src.size()) {
//            throw new IllegalArgumentException();
//        }
//
//        List<Slot> moving = src.subList(src.size() - numCards, src.size());
//        if (!moving.get(0).faceUp) {
//            throw new IllegalArgumentException("must move visible cards");
//        }
//
//        // Validate build (descending by 1, alternating colors)
//        for (int i = 0; i < moving.size() - 1; i++) {
//            KlondikeCard top = moving.get(i).card;
//            KlondikeCard below = moving.get(i + 1).card;
//            if (!(top.getValue().rank == below.getValue().rank + 1 && top.isRed() != below.isRed())) {
//                throw new IllegalArgumentException("bad dest");
//            }
//        }
//
//        // Validate destination
//        List<Slot> dst = cascades.get(destPile);
//        KlondikeCard movingTop = moving.get(0).card; // <- important
//        if (dst.isEmpty()) {
//            if (movingTop.getValue() != Value.K) {
//                throw new IllegalArgumentException("only King to empty");
//            }
//        } else {
//            KlondikeCard bottomDst = dst.get(dst.size() - 1).card;
//            if (!(movingTop.getValue().rank + 1 == bottomDst.getValue().rank
//                    && movingTop.isRed() != bottomDst.isRed())) {
//                throw new IllegalArgumentException("bad dest");
//            }
//        }
//
//        // Perform move
//        List<Slot> moved = new ArrayList<>(moving);
//        moving.clear();
//        dst.addAll(moved);
//
//        if (!src.isEmpty()) {
//            Slot newTop = src.get(src.size() - 1);
//            if (!newTop.faceUp) newTop.faceUp = true; // ensure visibility flips after move
//        }
//    }
//
//    @Override
//    public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
//        ensureStarted();
//        requirePileIndex(destPile);
//        if (visibleDraw.isEmpty()) {
//            throw new IllegalStateException("must start draw");
//        }
//        KlondikeCard c = visibleDraw.removeFirst();
//        moveSingleToPile(c, cascades.get(destPile));
//        if (!drawPile.isEmpty()) {
//            visibleDraw.addFirst(drawPile.removeFirst());
//        }
//    }
////    @Override
////    public void moveDraw(int destPile) {
////        ensureStarted();
////        requirePileIndex(destPile);
////        if (visibleDraw.isEmpty()) {
////            throw new IllegalStateException("must start draw");
////        }
////
////        KlondikeCard c = visibleDraw.removeFirst();
////        moveSingleToPile(c, cascades.get(destPile));
////
////        // Reveal next draw card if any
////        if (!drawPile.isEmpty()) {
////            visibleDraw.addLast(drawPile.removeFirst());
////        }
////    }
//
//        @Override
//    public void moveDrawToFoundation(int foundationPile) {
//        ensureStarted();
//        requireFoundationIndex(foundationPile);
//        if (visibleDraw.isEmpty()) {
//            throw new IllegalStateException("must start draw");
//        }
//
//        KlondikeCard c = visibleDraw.removeFirst();
//        moveCardToFoundation(c, foundations.get(foundationPile));
//        if (!drawPile.isEmpty()) {
//            visibleDraw.addLast(drawPile.removeFirst());
//        }
//    }
////    @Override
////    public void moveDrawToFoundation(int foundationPile) {
////        ensureStarted();
////        requireFoundationIndex(foundationPile);
////        if (visibleDraw.isEmpty()) {
////            throw new IllegalStateException("must start draw");
////        }
////
////        KlondikeCard c = visibleDraw.removeFirst();
////        moveCardToFoundation(c, foundations.get(foundationPile));
////
////        // Reveal next draw card if any
////        if (!drawPile.isEmpty()) {
////            visibleDraw.addLast(drawPile.removeFirst());
////        }
////    }
//
//
//    @Override
//    public void moveToFoundation(int srcPile, int foundationPile) throws IllegalArgumentException, IllegalStateException {
//        ensureStarted();
//        requirePileIndex(srcPile);
//        requireFoundationIndex(foundationPile);
//        List<Slot> src = cascades.get(srcPile);
//        if (src.isEmpty()) {
//            throw new IllegalArgumentException("src is empty");
//        }
//
//        Slot s = src.get(src.size() - 1);
//        if (!s.faceUp) {
//            throw new IllegalArgumentException("must move visible cards");
//        }
//        moveCardToFoundation(s.card, foundations.get(foundationPile));
//
//        src.remove(src.size() - 1);
//
//        if (!src.isEmpty()) {
//            src.get(src.size() - 1).faceUp = true;
//        }
//    }
//
////    @Override
////    public void discardDraw() {
////        ensureStarted();
////        if (visibleDraw.isEmpty()) {
////            throw new IllegalStateException("must start draw");
////        }
////        // visibleDraw = [newest, ..., oldest]
////        // Push oldest first to bottom of stock to preserve chronological order
//////        for (KlondikeCard c : visibleDraw) {
//////            drawPile.addLast(c);
//////        }
//////        visibleDraw.clear();
//////        refreshDraw();
////
////        KlondikeCard first = visibleDraw.removeFirst();
////        drawPile.addLast(first);
////        refreshDraw();
////    }
//
//    @Override
//    public void discardDraw() {
//        ensureStarted();
//        if (visibleDraw.isEmpty()) {
//            throw new IllegalStateException("must start draw");
//        }
//
//        // Remove the first visible card (the one being discarded)
//        KlondikeCard first = visibleDraw.removeFirst();
//
//        // Was there any card left in the draw pile *before* we placed the discarded card?
//        boolean hadMore = !drawPile.isEmpty();
//
//        // Place the discarded card at the bottom
//        drawPile.addLast(first);
//
//        // If there was at least one card in the stock before the discard,
//        // reveal (pull) it into the visible window's tail.
//        if (hadMore) {
//            visibleDraw.addLast(drawPile.removeFirst());
//        }
//    }
//
//
//    @Override
//    public int getNumRows() {
//        ensureStarted();
//        int max = 0;
//        for (List<Slot> pile : cascades) {
//            if (pile.size() > max) {
//                max = pile.size();
//            }
//        }
//        return max;
//    }
//
//    @Override
//    public int getNumPiles() {
//        ensureStarted();
//        return cascades.size();
//    }
//
//    @Override
//    public int getNumDraw() {
//        ensureStarted();
//        return numDraw;
//    }
//
//    @Override
//    public boolean isGameOver() {
//        ensureStarted();
//        return score == 52;
//    }
//
//    @Override
//    public int getScore() {
//        ensureStarted();
//        return score;
//    }
//
//    @Override
//    public int getPileHeight(int pileNum) {
//        ensureStarted();
//        requirePileIndex(pileNum);
//        return cascades.get(pileNum).size();
//    }
//
//    @Override
//    public Card getCardAt(int pileNum, int card) {
//        ensureStarted();
//        requirePileIndex(pileNum);
//        List<Slot> pile = cascades.get(pileNum);
//        if (card < 0 || card >= pile.size()) {
//            throw new IllegalArgumentException("card out of bounds");
//        }
//        return pile.get(card).card;
//    }
//
//    @Override
//    public Card getCardAt(int foundationPile) {
//        ensureStarted();
//        requireFoundationIndex(foundationPile);
//        Deque<KlondikeCard> f = foundations.get(foundationPile);
//        return f.isEmpty() ? null : f.peekLast();
//    }
//
//    @Override
//    public boolean isCardVisible(int pileNum, int card) {
//        ensureStarted();
//        requirePileIndex(pileNum);
//        List<Slot> pile = cascades.get(pileNum);
//        if (card < 0 || card >= pile.size()) {
//            throw new IllegalArgumentException();
//        }
//        return pile.get(card).faceUp;
//    }
//
////        @Override
////    public List<Card> getDrawCards() {
////        ensureStarted();
////        List<Card> out = new ArrayList<>(visibleDraw.size());
////        for (KlondikeCard c : visibleDraw) {
////            out.add(c);
////        }
////        System.out.println(out.toString());
////        return Collections.unmodifiableList(out);
////
////    }
//    @Override
//    public List<Card> getDrawCards() {
//        ensureStarted();
//        return new ArrayList<>(visibleDraw); // defensive copy
//    }
//
//
//    @Override
//    public int getNumFoundations() {
//        ensureStarted();
//        return foundations.size();
//    }
//
//    private void moveSingleToPile(KlondikeCard card, List<Slot> dest) {
//        if (dest.isEmpty()) {
//            if (card.getValue() != Value.K) {
//                throw new IllegalArgumentException();
//            }
//        } else {
//            KlondikeCard bottom = dest.get(dest.size() - 1).card;
//            if (!(card.getValue().rank + 1 == bottom.getValue().rank && card.isRed() != bottom.isRed())) {
//                throw new IllegalArgumentException();
//
//            }
//        }
//        dest.add(new Slot(card, true));
//    }
//
//    private void moveCardToFoundation(KlondikeCard card, Deque<KlondikeCard> foundation) {
//        if (foundation.isEmpty()) {
//            if (card.getValue() != Value.A) {
//                throw new IllegalArgumentException();
//            }
//        } else {
//            KlondikeCard top = foundation.peekLast();
//            if (card.getSuit() != top.getSuit() || card.getValue().rank != top.getValue().rank + 1) {
//                throw new IllegalArgumentException();
//
//            }
//        }
//        foundation.addLast(card);
//        score++;
//    }
//
//    private void refreshDraw() {
//        visibleDraw.clear();
//        for (int i = 0; i < numDraw && !drawPile.isEmpty(); i++) {
//            visibleDraw.addLast(drawPile.removeFirst());
//        }
//    }
//
//    private void requirePileIndex(int pileIndex) {
//        if (pileIndex < 0 || pileIndex >= cascades.size()) {
//            throw new IllegalArgumentException();
//        }
//    }
//
//    private void requireFoundationIndex(int foundationIndex) {
//        if (foundationIndex < 0 || foundationIndex >= foundations.size()) {
//
//            throw new IllegalArgumentException();
//        }
//    }
//
//    private void ensureStarted() {
//        if (!started) {
//            throw new IllegalStateException("game not started");
//        }
//    }
//
//    @Override
//    public void showCascades(int index) {
//
//        List<Slot> cascade = this.cascades.get(index);
//        System.out.println("========== cascade ===========");
//        for (Slot cascadeItem : cascade) {
//            System.out.println(cascadeItem.card.toString());
//        }
//    }
//
//}