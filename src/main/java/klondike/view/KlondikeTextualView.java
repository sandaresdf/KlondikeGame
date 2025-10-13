package klondike.view;

import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

import java.io.IOException;
import java.util.List;

/**
 * Textual view for Klondike game.
 */
public class KlondikeTextualView implements TextualView {
    private final KlondikeModel<?> model;
    private Appendable appendable;

    /**
     * Creates a textual view for the given model.
     * @param model the game model
     */
    public KlondikeTextualView(KlondikeModel<?> model) {
        this.model = model;
        this.appendable = System.out;
    }

    /**
     * Creates a textual view for the given model with appendable output.
     * @param model the game model
     * @param appendable the output destination
     */
    public KlondikeTextualView(KlondikeModel<?> model, Appendable appendable) {
        this.model = model;
        this.appendable = appendable;
    }

    @Override
    public void render() throws IOException {
        appendable.append(this.toString());
    }

    /**
     * Renders the game state as a string.
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Draw: ");
        List<? extends Card> drawCards = model.getDrawCards();
        if (drawCards.isEmpty()) {
            sb.append("");
        } else {
            for (int i = 0; i < drawCards.size(); i++) {
                sb.append(drawCards.get(i).toString());
                if (i < drawCards.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append("\n");

        sb.append("Foundation:");
        for (int i = 0; i < model.getNumFoundations(); i++) {
            sb.append(" ");
            Card foundCard = model.getCardAt(i);
            if (foundCard == null) {
                sb.append("<none>");
            } else {
                sb.append(foundCard.toString());
            }
            if (i < model.getNumFoundations() - 1) {
                sb.append(",");
            }
        }
        sb.append("\n");

        int numRows = model.getNumRows();
        int numPiles = model.getNumPiles();

        for (int row = 0; row < numRows; row++) {
            for (int pile = 0; pile < numPiles; pile++) {
                int pileHeight = model.getPileHeight(pile);

                if (row < pileHeight) {
                    if (model.isCardVisible(pile, row)) {
                        String cardStr = model.getCardAt(pile, row).toString();
                        sb.append(String.format("%3s", cardStr));
                    } else {
                        sb.append("  ?");
                    }
                } else if (row == 0 && pileHeight == 0) {
                    sb.append("  X");
                } else {
                    sb.append("   ");
                }
            }

            if (row < numRows - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}

//package klondike.view;
//
//import klondike.model.hw02.Card;
//import klondike.model.hw02.KlondikeModel;
//
//import java.util.List;
//
///**
// * Textual view for Klondike game.
// */
//public class KlondikeTextualView implements TextualView {
//    private final KlondikeModel<?> model;
//
//    /**
//     * Creates a textual view for the given model.
//     * @param model the game model
//     */
//    public KlondikeTextualView(KlondikeModel<?> model) {
//        this.model = model;
//    }
//
//    /**
//     * Renders the game state as a string.
//     * @return the string representation
//     */
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("Draw: ");
//        List<? extends Card> drawCards = model.getDrawCards();
//        if (drawCards.isEmpty()) {
//            sb.append("");
//        } else {
//            for (int i = 0; i < drawCards.size(); i++) {
//                sb.append(drawCards.get(i).toString());
//                if (i < drawCards.size() - 1) {
//                    sb.append(", ");
//                }
//            }
//        }
//        sb.append("\n");
//
//        sb.append("Foundation:");
//        for (int i = 0; i < model.getNumFoundations(); i++) {
//            sb.append(" ");
//            Card foundCard = model.getCardAt(i);
//            if (foundCard == null) {
//                sb.append("<none>");
//            } else {
//                sb.append(foundCard.toString());
//            }
//            if (i < model.getNumFoundations() - 1) {
//                sb.append(",");
//            }
//        }
//        sb.append("\n");
//
//        int numRows = model.getNumRows();
//        int numPiles = model.getNumPiles();
//
//        for (int row = 0; row < numRows; row++) {
//            for (int pile = 0; pile < numPiles; pile++) {
//                int pileHeight = model.getPileHeight(pile);
//
//                if (row < pileHeight) {
//                    if (model.isCardVisible(pile, row)) {
//                        String cardStr = model.getCardAt(pile, row).toString();
//                        sb.append(String.format("%3s", cardStr));
//                    } else {
//                        sb.append("  ?");
//                    }
//                } else if (row == 0 && pileHeight == 0) {
//                    sb.append("  X");
//                } else {
//                    sb.append("   ");
//                }
//            }
//
//            if (row < numRows - 1) {
//                sb.append("\n");
//            }
//        }
//
//        return sb.toString();
//    }
//}