/*
 * Represent a single location (tile) on the maze
 */

public class Tile {
    private char symbol;

    public Tile(char sym, int numColors) {

            if (!((sym >= 'A' && sym <= 'Z') ||
                    (sym >= 'a' && sym <= 'z') ||
                    sym == '#' || sym == '.' ||
                    sym == '^' || sym == '@' ||
                    sym == '?')) {
                System.err.println("Invalid character in map");
                System.exit(1);
            }

            if ((sym >= 'A' && sym <= 'Z') && (sym >= ('A' + numColors + 1))) {
                System.err.println("Invalid door in map");
                System.exit(1);
            }

            if ((sym >= 'a' && sym <= 'z') && (sym >= ('a' + numColors + 1))) {
                System.err.println("Invalid button in map");
                System.exit(1);
            }
        symbol = sym;
    }

    public char getSymbol() {
        return symbol;
    }

    public char render(ColorValue v) {
        if (symbol == v.asButton() || symbol == v.asDoor())
            return '.';

        return symbol;

    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
