import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
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
    private ArrayList<ArrayList<ArrayList<Character>>> reached;

    // reachable collection
    private ArrayDeque<State> reachableCollection;

    private State start, finish;

    private StringBuilder output;

    private static final char FROM_START = '@';
    private static final char NOT_REACHED = '#';
    private static final char GO_NORTH = 'N';
    private static final char GO_EAST = 'E';
    private static final char GO_SOUTH = 'S';
    private static final char GO_WEST = 'W';

    private static boolean solution = false;

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

                // add state as start
                if (sym == '@') {
                    start = new State(ColorValue.fromIndex(0), new Point(i, row));
                    // error checking for one state
                    countStart++;
                }

                // add state as finush
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

        output = new StringBuilder();

        // fill in our reached AL with false
        reached = new ArrayList<>(numColors + 1);

        for (int color = 0; color < numColors + 1; color++) {
            // make an array list to store rows for this color
            ArrayList<ArrayList<Character>> rowList = new ArrayList<>(height);
            for (int row = 0; row < height; row++) {
                // array list for column values
                ArrayList<Character> colList = new ArrayList<>(width);
                for (int col = 0; col < width; col++) {
                    colList.add(NOT_REACHED);
                } // col for()
                rowList.add(colList);
            } // row for()
            reached.add(rowList);
        } // c for()

        // initialize the reachable collection
        reachableCollection = new ArrayDeque<>();

        // step 2
        markReached(start, FROM_START);
        reachableCollection.addFirst(start);

        State curr = start;
        if (c.isCheckpoint2()) {
            output.append("  adding ");
            output.append(curr.printState());
        }

        int count = 1;

        // step 3
        while (!reachableCollection.isEmpty()) {

            curr = reachableCollection.removeFirst();

            if (c.isCheckpoint2()) {
                output.append(count++);
                output.append(": processing ");
                output.append(curr.printState());
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
                State button = new State(new ColorValue(checkButton.getSymbol()), curr.getPoint());
                if (canBeReached(button)) {
                    // System.out.println("Current color value: " + curr.getColorValue());
                    markReached(button, curr.getColorValue().asButton());

                    if (c.isQueueMode())
                        reachableCollection.addLast(button);
                    if (c.isStackMode())
                        reachableCollection.addFirst(button);

                    if (c.isCheckpoint2()) {
                        output.append("  adding ");
                        output.append(button.printState());
                    }
                }
            } else {
                // check north
                State north = new State(curr.getColorValue(), curr.getPoint().north());
                if (canBeReached(north)) {
                    markReached(north, GO_NORTH);

                    if (c.isQueueMode())
                        reachableCollection.addLast(north);
                    if (c.isStackMode())
                        reachableCollection.addFirst(north);

                    if (c.isCheckpoint2()) {
                        output.append("  adding ");
                        output.append(north.printState());
                    }
                }

                // check east
                State east = new State(curr.getColorValue(), curr.getPoint().east());
                if (canBeReached(east)) {
                    markReached(east, GO_EAST);

                    if (c.isQueueMode())
                        reachableCollection.addLast(east);
                    if (c.isStackMode())
                        reachableCollection.addFirst(east);

                    if (c.isCheckpoint2()) {
                        output.append("  adding ");
                        output.append(east.printState());
                    }
                }

                // check south
                State south = new State(curr.getColorValue(), curr.getPoint().south());
                if (canBeReached(south)) {
                    markReached(south, GO_SOUTH);

                    if (c.isQueueMode())
                        reachableCollection.addLast(south);
                    if (c.isStackMode())
                        reachableCollection.addFirst(south);

                    if (c.isCheckpoint2()) {
                        output.append("  adding ");
                        output.append(south.printState());
                    }
                }

                // check west
                State west = new State(curr.getColorValue(), curr.getPoint().west());
                if (canBeReached(west)) {
                    markReached(west, GO_WEST);

                    if (c.isQueueMode())
                        reachableCollection.addLast(west);
                    if (c.isStackMode())
                        reachableCollection.addFirst(west);

                    if (c.isCheckpoint2()) {
                        output.append("  adding ");
                        output.append(west.printState());
                    }
                }

                if (checkFinish(north) || checkFinish(east) || checkFinish(south) || checkFinish(west)) {
                    solution = true;
                    if (c.isCheckpoint2())
                        break;
                }
            }
        }

        if (c.isCheckpoint2())
            System.out.print(output);
    }

    public void printSolution(Config c) {
        List<State> backtrack = backtrack();

        StringBuilder list = new StringBuilder();

        if (!solution) {
            System.out.println("No solution.");
            System.out.println("Reachable:");

            printNoSolution();
        }
        // output with the correct version
        else if (c.isMapOutputMode()) {
            printMap(backtrack);
        }
        else {
            // print the list
            for (State s : backtrack) {
                list.insert(0, s.printState());
            }
            System.out.print(list);
        }

    }

    private void printNoSolution() {

        for (int row = 0; row < height; row++) {
            // create a string then print all at once
            StringBuilder sb = new StringBuilder();

            for (int col = 0; col < width; col++) {
                if (wasReached(row, col))
                    sb.append(map.get(row).get(col));
                else
                    sb.append("#");
            }

            System.out.print(sb);
            // row is output
            System.out.print("\n");
        }
    }

    private boolean wasReached(int row, int col) {
        for (int color = 0; color <= numColors; color++) {
            if (!reached.get(color).get(row).get(col).equals('#'))
                return true;
        }
        return false;
    }


    private void printMap(List<State> backtrack) {
        // create a duplicate map for outputs
        // look very much like reached setup
        ArrayList<ArrayList<ArrayList<Character>>> output = new ArrayList<>(numColors + 1);


        for (int color = 0; color < numColors + 1; color++) {
            // make an array list to store rows for this color
            ArrayList<ArrayList<Character>> rowList = new ArrayList<>(height);
            for (int row = 0; row < height; row++) {
                // array list for column values
                ArrayList<Character> colList = new ArrayList<>(width);
                for (int col = 0; col < width; col++) {
                    colList.add(map.get(row).get(col).renderFinal(ColorValue.fromIndex(color)));
                } // col for()
                rowList.add(colList);
            } // row for()
            output.add(rowList);
        } // c for()

        // starter map
        // walk through the solution and update our characters stored along the path
        for (State curr : backtrack) {
            if (curr == backtrack.get(0) || curr == backtrack.get(backtrack.size() - 1)) {
                // skip if we are in the start state
                continue;
            }

            Tile currTile = map.get(curr.getPoint().getRow()).get(curr.getPoint().getCol());
            if (currTile.getSymbol() == '.') {
                output.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '+');
            } else if ((currTile.getSymbol() >= 'a' && currTile.getSymbol() <= 'z') || currTile.getSymbol() == '^') {
                // touched a button
                char tmp = getBacktrack(curr);
                if ((tmp >= 'a' && tmp <= 'z' || tmp == '^') && tmp != curr.getColorValue().asButton())
                    // starting from a button
                    output.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '@');
                else
                    output.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '%');
            } else if ((currTile.getSymbol() >= 'A' && currTile.getSymbol() <= 'Z')) {

                if (currTile.getSymbol() == curr.getColorValue().asDoor())
                    output.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '+');

            } else if (currTile.getSymbol() >= '@' && curr.getColorValue().asIndex() != start.getColorValue().asIndex()) {
                output.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '+');

            }

        }

        StringBuilder sb = new StringBuilder();
        // print out map
        for (int co = 0; co < numColors + 1; co++) {
            ColorValue curr = ColorValue.fromIndex(co);
            sb.append("// color ");
            sb.append(curr);
            sb.append("\n");
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    sb.append(output.get(co).get(row).get(col));
                }
                sb.append("\n");
            }
        }
        System.out.print(sb);
    }

    /**
     * backtrack through the reached items to find the start
     *
     * @return a list of the states
     */
    private List<State> backtrack() {
        // start from end point
        State curr = null;
        ArrayList<State> backtrack = new ArrayList<>();

        for (int color = 0; color <= numColors; color++) {
            // check backtrack to see if reached
            char tmp = reached.get(color).get(finish.getPoint().getRow()).get(finish.getPoint().getCol());

            if (tmp != NOT_REACHED) {
                // this was the color we reached the finish in
                curr = new State(ColorValue.fromIndex(color), finish.getPoint());
                // stop looping
                break;
            }

        }

        // at this point there should be a value in curr
        // if no value (null) --> no solution
        if (curr == null) {
            solution = false;
            return backtrack;
        }

        // commence backtracking
        // keep looping while our current state is not the starting point, while backtrack
        // for current is not '@'
        backtrack.add(curr);

        while (getBacktrack(curr) != FROM_START) {
            // get one more state and add to out list
            char dir = getBacktrack(curr);

            if (dir == GO_NORTH)
                // backtrack south
                curr = new State(curr.getColorValue(), curr.getPoint().south());
            else if (dir == GO_SOUTH)
                // backtrack north
                curr = new State(curr.getColorValue(), curr.getPoint().north());
            else if (dir == GO_EAST)
                // backtrack west
                curr = new State(curr.getColorValue(), curr.getPoint().west());
            else if (dir == GO_WEST)
                // backtrack east
                curr = new State(curr.getColorValue(), curr.getPoint().east());
            else if (dir >= 'a' && dir <= 'z' || dir == '^')
                // pressed a button
                curr = new State(new ColorValue(dir), curr.getPoint());
            else
                // catch all
                throw new IllegalStateException("Reached a backtrack location with no place to go: " + curr);

            backtrack.add(curr);

        }
        return backtrack;
    }


    private char getBacktrack(State curr) {
        return reached.get(curr.getColorValue().asIndex()).get(curr.getPoint().getRow()).get(curr.getPoint().getCol());
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
        if (reached.get(colorIdx).get(p.getRow()).get(p.getCol()) != NOT_REACHED)
            return false;

        // check if this particular location is traversable
        // not a # not a Door of the wrong color

        // wall check
        Tile curr = map.get(p.getRow()).get(p.getCol());
        if (curr.getSymbol() == '#')
            return false;

        // door of diff color check
        if (curr.getSymbol() >= 'A' && curr.getSymbol() <= 'Z')
            return !curr.isDoor(st.getColorValue());

        return true;
    }


    /**
     * mark that a state was reached and from whence we came
     *
     * @param st   the state to visit
     * @param from the direction or button color
     */
    private void markReached(State st, char from) {
        int colorIdx = st.getColorValue().asIndex();
        Point p = st.getPoint();
        reached.get(colorIdx).get(p.getRow()).set(p.getCol(), from);
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
