import java.util.ArrayList;
import java.util.Scanner;

public class Maze {
    // store dimensions
    private int width, height, numColors;

    // 2D ArrayList to store all the tiles
    // outer AL stores rows
    // inner AL stores columns
    private ArrayList<ArrayList<Tile>> map;

    public Maze(Scanner in) {
        // get info from scanner
        numColors = in.nextInt();
        height = in.nextInt();
        width = in.nextInt();

        if (width < 1) {
            System.err.println("Height is less than 1");
            System.exit(1);
        }

        if (height < 1) {
            System.err.println("Width is less than 1");
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

                if (line.charAt(i) == '@')
                    countStart++;
                else if (line.charAt(i) == '?')
                    countEnd++;

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
