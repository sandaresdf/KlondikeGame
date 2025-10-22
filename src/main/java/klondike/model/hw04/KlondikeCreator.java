package klondike.model.hw04;

import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeModel;

/**
 * Factory class for creating different Klondike game variants.
 */
public class KlondikeCreator {

    /**
     * Enum representing different game types.
     */
    public enum GameType {
        BASIC, WHITEHEAD
    }

    /**
     * Creates a Klondike model of the specified type.
     * @param type the game type to create
     * @return a new instance of the appropriate model
     */
    public static KlondikeModel<KlondikeCard> create(GameType type) {
        switch (type) {
            case BASIC:
                return new BasicKlondike();
            case WHITEHEAD:
                return new WhiteheadKlondike();
            default:
                throw new IllegalArgumentException("Unknown game type");
        }
    }
}