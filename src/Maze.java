import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze {
    // store dimensions
    private final int width;
    private final int height;
    private final int numColors;

    // 2D ArrayList to store all the tiles
    // outer AL stores rows
    // inner AL stores columns
    private final ArrayList<ArrayList<Tile>> map;

    // 3D ArrayList to store whether we reach a tile in a given order

    // color -- row -- col
    private ArrayList<ArrayList<ArrayList<Boolean>>> reached;

    // reachable collection
    private ArrayDeque<State> reachableCollection;

    private State start, finish;

    public Maze(Scanner in) {
        // get info from scanner
        numColors = in.nextInt();
        height = in.nextInt();
        width = in.nextInt();

        if (!(numColors >= 0 && numColors <= 26)) {
            System.err.println("Invalid color");
            System.exit(1);
        }

        if (height < 1) {
            System.err.println("Width is less than 1");
            System.exit(1);
        }

        if (width < 1) {
            System.err.println("Height is less than 1");
            System.exit(1);
        }

        in.nextLine();

        // allocate enough space for height rows;
        map = new ArrayList<>(height);

        // helper var to track how many rows we've read
        int row = 0;

        // keep track of start and end characters
        int countStart = 0;
        int countEnd = 0;

        while (in.hasNextLine()) {
            if (row >= height)
                break;

            String line = in.nextLine();

            // check if comment and skip
            if (line.length() >= 2 && line.charAt(0) == '/' && line.charAt(1) == '/')
                continue;

            // we know we have a line of tiles to process
            ArrayList<Tile> rowOfTiles = new ArrayList<>(width);

            // loop through chars in line and add to row of tiles
            for (int i = 0; i < line.length(); i++) {
                char sym = line.charAt(i);
                if (sym == '@') {
                    start = new State(ColorValue.fromIndex(0), new Point(i, row));
                    // error checking for one state
                    countStart++;
                }

                if (sym == '?') {
                    finish = new State(ColorValue.fromIndex(0), new Point(i, row));
                    countEnd++;
                }

                if (countStart > 1) {
                    System.err.println("@ character appears more than once");
                    System.exit(1);
                }

                if (countEnd > 1) {
                    System.err.println("? character appears more than once");
                    System.exit(1);
                }

                rowOfTiles.add(new Tile(line.charAt(i), numColors));

            }
            map.add(rowOfTiles);
            row++;
        }

        if (start == null || finish == null) {
            System.err.println("Start or finish does not exist");
            System.exit(1);
        }

    }

    public void search(Config c) {
        // fill in our reached AL with false
        reached = new ArrayList<>(numColors + 1);

        for (int color = 0; color < numColors + 1; color++) {
            // make an array list to store rows for this color
            ArrayList<ArrayList<Boolean>> rowList = new ArrayList<>(height);
            for (int row = 0; row < height; row++) {
                // array list for column values
                ArrayList<Boolean> colList = new ArrayList<>(width);
                for (int col = 0; col < width; col++) {
                    colList.add(false);
                } // col for()
                rowList.add(colList);
            } // row for()
            reached.add(rowList);
        } // c for()

        // initialize the reachable collection
        reachableCollection = new ArrayDeque<>();

        // step 2
        markReached(start);
        reachableCollection.addFirst(start);

        State curr = start;
        if (c.isCheckpoint2()) {
            System.out.print("  adding ");
            curr.printState();
        }

        int count = 1;

        // step 3
        while (!reachableCollection.isEmpty()) {

            if (c.isQueueMode()) {
                curr = reachableCollection.removeFirst();

                if (c.isCheckpoint2()) {
                    System.out.printf("%d: processing ", count++);
                    curr.printState();
                }

                // if current tile is a button and hasn't been visited
                /*
                if S = (c, (row, col)) is the location of a button b of a different color than c or is a
                trap, and b has not yet reachable, add (b, (row, col)) to the
                <reachable_collection> and mark it as reachable.
                 */

                Tile checkButton = map.get(curr.getPoint().getRow()).get(curr.getPoint().getCol());

                // if
                if (checkButton.isButton(curr.getColorValue())) {
                        if (!canBeReached(curr) ) {
                            State button = new State(new ColorValue(checkButton.getSymbol()), curr.getPoint());
                            // System.out.println("Current color value: " + curr.getColorValue());
                            markReached(button);
                            reachableCollection.addLast(button);
                            if (c.isCheckpoint2()) {
                                System.out.print("  adding ");
                                button.printState();
                            }
                            if (checkFinish(button))
                                break;
                        }
                } else {
                    // check north
                    State north = new State(curr.getColorValue(), curr.getPoint().north());
                    if (canBeReached(north)) {
                        markReached(north);
                        reachableCollection.addLast(north);
                        if (c.isCheckpoint2()) {
                            System.out.print("  adding ");
                            north.printState();
                        }
                        if (checkFinish(north))
                            break;
                    }

                    // check east
                    State east = new State(curr.getColorValue(), curr.getPoint().east());
                    if (canBeReached(east)) {
                        markReached(east);
                        reachableCollection.addLast(east);
                        if (c.isCheckpoint2()) {
                            System.out.print("  adding ");
                            east.printState();
                        }
                        if (checkFinish(east))
                            break;
                    }

                    // check south
                    State south = new State(curr.getColorValue(), curr.getPoint().south());
                    if (canBeReached(south)) {
                        markReached(south);
                        reachableCollection.addLast(south);
                        if (c.isCheckpoint2()) {
                            System.out.print("  adding ");
                            south.printState();
                        }
                        if (checkFinish(south))
                            break;
                    }

                    // check west
                    State west = new State(curr.getColorValue(), curr.getPoint().west());
                    if (canBeReached(west)) {
                        markReached(west);
                        reachableCollection.addLast(west);
                        if (c.isCheckpoint2()) {
                            System.out.print("  adding ");
                            west.printState();
                        }
                        if (checkFinish(west))
                            break;
                    }

                    if (count > numColors * width * height){
                        System.err.println("Too many states");
                        System.exit(1);
                    }
                }
            }


            if (c.isStackMode()) {
                curr = reachableCollection.removeFirst();

                if (curr.getPoint().getRow() == finish.getPoint().getRow() && curr.getPoint().getCol() == finish.getPoint().getCol())
                    break;

                if (c.isCheckpoint2()) {
                    System.out.printf("%d: processing ", count++);
                    curr.printState();
                }

                Tile checkButton = map.get(curr.getPoint().getRow()).get(curr.getPoint().getCol());

                if (checkButton.isButton(curr.getColorValue()) && !canBeReached(curr)) {
                    State button = new State(new ColorValue(checkButton.getSymbol()), curr.getPoint());
                    markReached(button);
                    reachableCollection.addFirst(button);
                    if (c.isCheckpoint2()) {
                        System.out.print("  adding ");
                        button.printState();
                    }
                    if (checkFinish(button))
                        break;

                } else {
                    // check north
                    State north = new State(curr.getColorValue(), curr.getPoint().north());
                    if (canBeReached(north)) {
                        markReached(north);
                        reachableCollection.addFirst(north);
                        if (c.isCheckpoint2()) {
                            System.out.print("  adding ");
                            north.printState();
                        }
                        if (checkFinish(north))
                            break;
                    }

                    // check east
                    State east = new State(curr.getColorValue(), curr.getPoint().east());
                    if (canBeReached(east)) {
                        markReached(east);
                        reachableCollection.addFirst(east);
                        if (c.isCheckpoint2()) {
                            System.out.print("  adding ");
                            east.printState();
                        }
                        if (checkFinish(east))
                            break;
                    }

                    // check south
                    State south = new State(curr.getColorValue(), curr.getPoint().south());
                    if (canBeReached(south)) {
                        markReached(south);
                        reachableCollection.addFirst(south);
                        if (c.isCheckpoint2()) {
                            System.out.print("  adding ");
                            south.printState();
                        }
                        if (checkFinish(south))
                            break;
                    }

                    // check west
                    State west = new State(curr.getColorValue(), curr.getPoint().west());
                    if (canBeReached(west)) {
                        markReached(west);
                        reachableCollection.addFirst(west);
                        if (c.isCheckpoint2()) {
                            System.out.print("  adding ");
                            west.printState();
                        }
                        if (checkFinish(west))
                            break;
                    }

                    if (count > (numColors * width * height)){
                        System.err.println("Too many states");
                        System.exit(1);
                    }
                }
            }
        }
    }


    private boolean canBeReached(State st) {
        if (st == null) {
            System.err.println("State does not exist");
            System.exit(1);
        }

        int colorIdx = st.getColorValue().asIndex();
        Point p = st.getPoint();

        if (p.getCol() < 0 || p.getCol() >= width || p.getRow() < 0 || p.getRow() >= height)
            return false;

        // already reached
        if (reached.get(colorIdx).get(p.getRow()).get(p.getCol()))
            return false;

        // check if this particular location is traversable
        // not a # not a Door of the wrong color

        // wall check
        Tile curr = map.get(p.getRow()).get(p.getCol());
        if (curr.getSymbol() == '#')
            return false;

        // door of diff color check
        if (curr.getSymbol() >= 'A' && curr.getSymbol() <= 'Z')
            if (curr.isDoor(st.getColorValue()))
                return false;

        // check for diff color

        return true;
    }


    private void markReached(State st) {
        if (st == null) {
            System.err.println("State does not exist");
            System.exit(1);
        }

        int colorIdx = st.getColorValue().asIndex();
        Point p = st.getPoint();
        reached.get(colorIdx).get(p.getRow()).set(p.getCol(), true);
    }

    private boolean checkFinish(State curr) {
        return curr.getPoint().getRow() == finish.getPoint().getRow() &&
                curr.getPoint().getCol() == finish.getPoint().getCol();
    }

    public void printMap() {
        // numColors + 1: all closed + each of the colors
        // variables to check for start and end

        for (int i = 0; i < numColors + 1; i++) {
            ColorValue curr = ColorValue.fromIndex(i);
            System.out.print("// color " + curr + "\n");
            for (ArrayList<Tile> row : map) {
                // create a string then print all at once
                StringBuilder sb = new StringBuilder();

                for (Tile col : row) {
                    sb.append(col.render(curr));
                }

                System.out.print(sb);
                // row is output
                System.out.print("\n");
            }
        }
    }
}
