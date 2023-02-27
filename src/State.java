import java.util.HashMap;

public class State {

    private final ColorValue c;
    private final Point p;

    public State(ColorValue c, Point p) {
        this.c = c;
        this.p = p;
    }

    public ColorValue getColorValue() { return c; }

    public Point getPoint() { return p; }

    public void printState() {
        System.out.printf("(%s, (%d, %d))\n", getColorValue().toString(), getPoint().getRow(), getPoint().getCol());
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
