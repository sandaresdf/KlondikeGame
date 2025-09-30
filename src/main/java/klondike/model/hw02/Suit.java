package klondike.model.hw02;

public enum Suit {
    CLUBS('♣', false),
    DIAMONDS('♢', true),
    HEARTS('♡', true),
    SPADES('♠', false);

    public final char symbol;
    public final boolean red;

    Suit(char symbol, boolean red) {
        this.symbol = symbol;
        this.red = red;
    }
}
