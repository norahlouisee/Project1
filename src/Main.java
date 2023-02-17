import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Config c = new Config(args);

        Scanner in = new Scanner(System.in);

        Maze puzzle = new Maze(in);

        if (c.isCheckpoint1()) {
            puzzle.printMap();
        }



    }

}
