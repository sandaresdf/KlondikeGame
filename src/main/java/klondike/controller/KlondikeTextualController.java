package klondike.controller;

import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.view.KlondikeTextualView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Textual controller for Klondike Solitaire game.
 */
public class KlondikeTextualController implements KlondikeController {
    private final Readable readable;
    private final Appendable appendable;

    /**
     * Creates a new textual controller.
     * @param rd the input source
     * @param ap the output destination
     * @throws IllegalArgumentException if either argument is null
     */
    public KlondikeTextualController(Readable rd, Appendable ap) {
        if (rd == null || ap == null) {
            throw new IllegalArgumentException("Readable and Appendable cannot be null");
        }
        this.readable = rd;
        this.appendable = ap;
    }

    @Override
    public <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck,
                                          boolean shuffle, int numPiles, int numDraw) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }

        Scanner scanner = new Scanner(readable);
        KlondikeTextualView view = new KlondikeTextualView(model, appendable);

        try {
            model.startGame(deck, shuffle, numPiles, numDraw);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new IllegalStateException("Cannot start game: " + e.getMessage());
        }

        while (!model.isGameOver()) {
            renderGameState(view, model);

            if (!scanner.hasNext()) {
                throw new IllegalStateException("No more input available");
            }

            String command = scanner.next();

            if (command.equalsIgnoreCase("q")) {
                handleQuit(view, model);
                return;
            }

            try {
                processCommand(command, scanner, model);
            } catch (IllegalArgumentException | IllegalStateException e) {
                transmit("Invalid move. Play again. " + e.getMessage() + "\n");
            }
        }

        renderGameState(view, model);

        if (isGameWon(model)) {
            transmit("You win!\n");
        } else {
            transmit("Game over. Score: " + model.getScore() + "\n");
        }
    }

    private void renderGameState(KlondikeTextualView view, KlondikeModel<?> model) {
        try {
            view.render();
            transmit("\n");
            transmit("Score: " + model.getScore() + "\n");
        } catch (IOException e) {
            throw new IllegalStateException("Cannot render game state");
        }
    }

    private void processCommand(String command, Scanner scanner, KlondikeModel<?> model) {
        switch (command.toLowerCase()) {
            case "mpp": // mpp 4 2 5
                handleMovePile(scanner, model);
                break;
            case "md":
                handleMoveDraw(scanner, model);
                break;
            case "mpf":
                handleMoveToFoundation(scanner, model);
                break;
            case "mdf":
                handleMoveDrawToFoundation(scanner, model);
                break;
            case "dd":
                model.discardDraw();
                break;
            default:
                transmit("Invalid command. Try again.\n");
                break;
        }
    }

    private void handleMovePile(Scanner scanner, KlondikeModel<?> model) {
        Integer srcPile = readNumber(scanner);
        if (srcPile == null) {
            return;
        }

        Integer numCards = readNumber(scanner);
        if (numCards == null) {
            return;
        }

        Integer destPile = readNumber(scanner);
        if (destPile == null) {
            return;
        }

        model.movePile(srcPile - 1, numCards, destPile - 1);
    }

    private void handleMoveDraw(Scanner scanner, KlondikeModel<?> model) {
        Integer destPile = readNumber(scanner);
        if (destPile == null) {
            return;
        }

        model.moveDraw(destPile - 1);
    }

    private void handleMoveToFoundation(Scanner scanner, KlondikeModel<?> model) {
        Integer srcPile = readNumber(scanner);
        if (srcPile == null) {
            return;
        }

        Integer foundationPile = readNumber(scanner);
        if (foundationPile == null) {
            return;
        }

        model.moveToFoundation(srcPile - 1, foundationPile - 1);
    }

    private void handleMoveDrawToFoundation(Scanner scanner, KlondikeModel<?> model) {
        Integer foundationPile = readNumber(scanner);
        if (foundationPile == null) {
            return;
        }

        model.moveDrawToFoundation(foundationPile - 1);
    }

    private Integer readNumber(Scanner scanner) {
        while (scanner.hasNext()) { // mpp a 4 5
            String input = scanner.next();

            if (input.equalsIgnoreCase("q")) {
                return null;
            }

            try {
                int number = Integer.parseInt(input);
                if (number >= 0) {
                    return number;
                } else {
                    transmit("Invalid input. Please enter a non-negative number.\n");
                }
            } catch (NumberFormatException e) {
                transmit("Invalid input. Please enter a number.\n");
            }
        }

        throw new IllegalStateException("No more input available");
    }

    private void handleQuit(KlondikeTextualView view, KlondikeModel<?> model) {
        try {
            transmit("Game quit!\n");
            transmit("State of game when quit:\n");
            view.render();
            transmit("\n");
            transmit("Score: " + model.getScore() + "\n");
        } catch (IOException e) {
            throw new IllegalStateException("Cannot render quit state");
        }
    }

    private boolean isGameWon(KlondikeModel<?> model) {
        int totalCards = 0;

        try {
            for (int i = 0; i < model.getNumPiles(); i++) {
                totalCards += model.getPileHeight(i);
            }
            totalCards += model.getDrawCards().size();
        } catch (Exception e) {
            return false;
        }

        return totalCards == 0 && model.isGameOver();
    }

    private void transmit(String message) {
        try {
            appendable.append(message);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot transmit output");
        }
    }
}