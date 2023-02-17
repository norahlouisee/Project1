import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * Configuration object for storing the settings running for the maze solver
 */
public class Config {
    // store mode as a private character
    private char mode = 0;  // 0 is our null character
    // store the output mode
    private char outputMode = 'm';

    // keep track of checkpoint mode
    private boolean checkpoint1 = false;
    private boolean checkpoint2 = false;

    // GetOpt options
    private static LongOpt[] longOptions = {
            new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
            new LongOpt("stack", LongOpt.NO_ARGUMENT, null, 's'),
            new LongOpt("queue", LongOpt.NO_ARGUMENT, null, 'q'),
            new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
            new LongOpt("checkpoint1", LongOpt.NO_ARGUMENT, null, 'x'),
            new LongOpt("checkpoint2", LongOpt.NO_ARGUMENT, null, 'y')
    };

    /**
     * construct our configuration object using getopt
     * @param args string array of command line arguments
     */
    public Config(String[] args) {
        // we will do all getopt parsing

        // make a getopt object
        Getopt g = new Getopt("mazesolver", args, "hsqo:xy", longOptions);
        g.setOpterr(true);

        int choice;

        // process each argument from the command line in turn
        while( (choice = g.getopt()) != -1 )  {

            // check which argument is procressing
            switch (choice) {
                case 'h':
                    // function to print help
                    printHelp();
                    break;
                case 's':
                case 'q':
                    // verify this was provided once
                    // if mode is not the null character, we already set a mode
                    if (mode != 0) {
                        System.err.println("Mode was already specified");
                        System.exit(1);
                    }
                    mode = (char) choice;
                    break;
                case 'o' :
                    String requestedOutput = g.getOptarg();
                    if (requestedOutput.equals("map") || requestedOutput.equals("list")) {
                        outputMode = requestedOutput.charAt(0);
                    }
                    else {
                        System.err.println("Unknown output type" + requestedOutput);
                        System.exit(1);
                    }
                    break;

                case 'x' :
                    checkpoint1 = true;
                    break;
                case 'y' :
                    checkpoint2 = true;
                    break;
                default:
                    // none of the above
                    System.err.println("Unknown command line option -" + (char) choice);;
                    System.exit(1);
            } // switch()

        } // while()

        // final error checking
        // check if --stack or --queue mode specified
        if (mode == 0) {
            System.err.println("You must specify --stack or --queue mode");
            System.exit(1);
        }

    }

    public boolean isQueueMode() {
        return mode == 'q';
    }

    public boolean isMapOutputMode() {
        return outputMode == 'm';
    }

    public boolean isCheckpoint1() {
        return checkpoint1;
    }

    public boolean isCheckpoint2() {
        return checkpoint2;
    }

    private void printHelp() {
        System.out.println("Usage: java [options] Main [-h] [-s | q] [-o] [-x] [-y] ");
        System.out.println("This program output information about a maze’s solvability and a description of\n" +
                "the maze’s solution");
        System.exit(0);
    }

}
