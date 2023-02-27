public class ColorValue {

    // we will store this as a button value
    private final char value;

    public ColorValue (char sym) {
        if (!(sym == '^' || (sym >= 'a' && sym <= 'z') || (sym >= 'A' && sym <= 'Z'))) {
            System.err.println("Invalid color");
            System.exit(1);
        }

        value = sym;
    }

    public static ColorValue fromIndex(int idx) {
        if (idx > 26 || idx < 0) {
            System.err.println("Color out of range 0-26");
            System.exit(1);
        }

        if (idx == 0)
            return new ColorValue('^');

        return new ColorValue((char) ('a' + idx - 1));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public char asButton() {
        return value;
    }

    public char asDoor() {
        return (char) (value - 'a' + 'A');
    }

    public int asIndex() {
        if (value == '^')
            return 0;
        else
            return value - 'a' + 1;
    }
}
