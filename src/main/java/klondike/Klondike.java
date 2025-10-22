package klondike;

import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw04.KlondikeCreator;

import java.io.InputStreamReader;
import java.util.List;

/**
 * Main class for running Klondike Solitaire game.
 */
public final class Klondike {
    /**
     * Main method to start the game.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Must specify game type");
        }
//        basic 7 3 -> ["basic", "7", "3"]
        String gameType = args[0].toLowerCase();
        KlondikeCreator.GameType type;

        switch (gameType) {
            case "basic":
                type = KlondikeCreator.GameType.BASIC;
                break;
            case "whitehead":
                type = KlondikeCreator.GameType.WHITEHEAD;
                break;
            default:
                throw new IllegalArgumentException("Invalid game type: " + gameType);
        }

        int numPiles = 7;  // default
        int numDraw = 3;   // default

        try {
            if (args.length >= 2) {
                numPiles = Integer.parseInt(args[1]);
            }
            if (args.length >= 3) {
                numDraw = Integer.parseInt(args[2]);
            }
        } catch (NumberFormatException e) {
            // Invalid numbers, but don't crash - use defaults
            System.err.println("Warning: Invalid number format, using defaults");
        }

        // Create model
        KlondikeModel<KlondikeCard> model = KlondikeCreator.create(type);

        // Create deck
        List<KlondikeCard> deck = model.createNewDeck();

        // Create controller
        KlondikeController controller = new KlondikeTextualController(
                new InputStreamReader(System.in), System.out);

        // Play game
        controller.playGame(model, deck, true, numPiles, numDraw);
    }
}