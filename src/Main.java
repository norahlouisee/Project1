import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Config c = new Config(args);

        Scanner in = new Scanner(System.in);

        Maze puzzle = new Maze(in);

        if (c.isCheckpoint1()) {
            puzzle.printMap();
        }
        else if (c.isCheckpoint2()) {
            puzzle.search(c);
        } else {
            puzzle.search(c);
            puzzle.printSolution(c);
        }



    }

}
