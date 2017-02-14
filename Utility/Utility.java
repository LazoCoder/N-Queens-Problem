package Utility;

import java.util.Random;

/**
 * Just a little class for some miscellaneous methods to help with debugging.
 */
public class Utility {

    /**
     * Used for printing the final results of a search.
     * <br/>
     * If debug mode was on during the search then this method
     * will not print out the time elapsed. This is because debug
     * mode slows down the algorithm significantly and therefore
     * the time elapsed would not be a fair representation of the
     * algorithm
     * @param timeElapsed       the time it took to complete the search
     * @param configurations    the total configurations attempted
     * @param board             the final board with the solution
     * @param localOptimums     the number of local optimums encountered
     * @param steps             the steps it took from generating a board
     *                          to getting to the global optimum
     * @param debug             true if debug mode was on
     * @param seed              the seed used for the random generator
     */
    public static void printResults (int timeElapsed,
                                     int configurations,
                                     Board board,
                                     int localOptimums,
                                     int steps,
                                     boolean debug,
                                     long seed) {

        System.out.println("N = " + board.getSize());

        if (!debug && timeElapsed != -1) {
            System.out.println("Time Elapsed:\t\t\t" + timeElapsed + " seconds");
        }

        if (configurations != -1) {
            System.out.println("Configurations Attempted:\t" + configurations);
        }

        if (localOptimums != -1) {
            System.out.println("Local Optimums Encountered:\t" +
                    localOptimums);
        }

        if (steps != -1) {
            System.out.println("Steps for Global Optimum :\t" + steps);
        }

        if (seed != -1) {
            System.out.println("Seed used:\t\t\t" + seed + "L");
        } else {
            System.out.println("Seed used:\t\t\t" + "none");
        }

        if (board != null) {
            System.out.println(board);
        }
    }

    /**
     * Print the board as the search is finding a solution.
     * This significantly slows down the search algorithm.
     * @param board     the board to print
     */
    public static void debug (Board board) {
        System.out.println(board);
        delay();
    }

    /**
     * Make the thread sleep for 250 milliseconds.
     * Useful for debugging.
     */
    private static void delay () {
        double seconds = 0.5;
        try {
            Thread.sleep((int)(1000.0 * seconds));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Prints out the heuristic values of each location on a
     * particular board.
     * @param board     the board to print heuristic values from.
     */
    public static void printScore (Board board) {
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                System.out.print(board.numberOfQueensAttackingHere(x, y) + " ");
            }
            System.out.println();
        }
    }

    /**
     * Prints out the heuristic values of each location on a
     * particular board for only the Queens.
     * @param board     the board to print heuristic values from.
     */
    public static void printScoreForQueens (Board board) {
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                if (board.containsQueen(x, y))
                    System.out.print(board.numberOfQueensAttackingHere(x, y) + " ");
                else
                    System.out.print("- ");
            }
            System.out.println();
        }
    }

    /**
     * Searches for a seed that would solve N-Queens for a particular
     * value of n under a specified time limit (using the Heuristic
     * Search).
     * <br/>
     * Example: find(200, 60) will search for a seed that would solve
     * N-Queens where n = 200, under 60 seconds if such a seed exists.
     * @param n             the number of Queens
     * @param timeLimit     the time limit in seconds
     */
    public static long findSeed (int n, int timeLimit) {

        long seed;
        int count = 0;

        while(true) {
            System.out.println("\nAttempt " + ++count
                    + " (N = " + n + ", T = " + timeLimit + "): \t");
            seed = new Random().nextLong();
            boolean result = Algorithms.HeuristicSearch.search(n, seed, timeLimit, false);

            if (result) {
                System.out.println("Seed: " + seed);
                break;
            }
        }

        return seed;
    }

    /**
     * Used for finding seeds more easily from the terminal.
     * <br/>
     * Example:
     * The command "java Utility 100 5" will search for a seed that
     * solves the N-Queens problem where n = 100 in under 5 seconds
     * using the Population Based Search.
     * @param args      terminal parameters (n and the time limit)
     */
    public static void main(String[] args) {

        try {

            int n = Integer.parseInt(args[0]);
            int t = Integer.parseInt(args[1]);

            System.out.println("Searching for a seed that solves N-Queens " +
                    "under " + t + " second(s) where N = " + n + ".");

            findSeed(n, t);

        } catch (Exception ex) {
            System.out.println("Invalid syntax.");
            System.out.println("The first parameter should be the number of Queens.");
            System.out.println("The second parameter should be the time limit.");
            System.out.println("Example: 'java Utility.Utility 100 10' searches for " +
                    "a seed that solves N-Queens under 10 seconds where N = 100.");
        }

    }

}
