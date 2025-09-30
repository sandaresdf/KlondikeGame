package klondike.model.hw02;

/**
 * Represents the numeric value of each card.
 * Note that A, J, Q, K are represented as numbers for rank.
 */
public enum Value {
    A("Ace", 1),
    two("2", 2),
    three("3", 3),
    four("4", 4),
    five("5", 5),
    six("6", 6),
    seven("7", 7),
    eight("8", 8),
    nine("9", 9),
    ten("10", 10),
    J("Jack", 11),
    Q("Queen", 12),
    K("King", 13);

    public final String label;
    public final int rank;

    Value(String label, int rank) {
        this.label = label;
        this.rank = rank;
    }
}
