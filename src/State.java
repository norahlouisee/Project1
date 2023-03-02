public class State {

    private final ColorValue c;
    private final Point p;

    public State(ColorValue c, Point p) {
        this.c = c;
        this.p = p;
    }

    public ColorValue getColorValue() { return c; }

    public Point getPoint() { return p; }

    public String printState() {
        return "(" + getColorValue().toString() + ", (" + getPoint().getRow() + ", " + getPoint().getCol() + "))\n";
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof State s)) {
            // wrong type
            return false;
        }

        // we know that o is a State
        // so it is safe to cast o to type State

        return s.p.equals(this.p);
    }
}
