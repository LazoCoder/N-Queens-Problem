package Algorithms;

import Utility.Board;
import Utility.*;

import java.util.HashSet;
import java.util.Stack;

/**
 * A blind depth first search that uses propagation.
 * Only safe positions that do not contain a Queen on them are checked.
 * If the final result has less than n Queens on the board, then back-
 * tracking occurs.
 * <br/>
 * The largest board that this algorithm can solve in under 20
 * seconds on my Dell Latitude is n = 17, which takes 2 seconds.
 */
public class BlindSearchAdvanced {

    /**
     * Begin the search.
     * @param n         size of the board and number of Queens to place on the board
     * @param timeLimit the amount of time given to find a solution before giving up
     * @param debug     if true, slows down the search and prints what the search is doing
     */
    public static boolean search (int n, int timeLimit, boolean debug) {

        if (n < 4) return false;

        // Initialize variables.
        long before = System.nanoTime(); // For measuring the time elapsed.
        HashSet<String> history = new HashSet<>(); // Keeps track of configurations attempted.
        int configurations = -1; //The initial configuration doesn't count.

        // Create stack and push the initial state of the board.
        Stack<Board> stack = new Stack<>();
        stack.push(new Board(n));

        // Depth first search.
        while (!stack.isEmpty()) {

            // If the time elapsed exceeds the time limit, then give up.
            int timeElapsed = (int)((System.nanoTime() - before) / 1_000_000_000L);
            if (!debug && timeElapsed > timeLimit) {
                System.out.println("Exceeded " + timeLimit + " seconds");
                return false;
            }

            Board board = stack.pop();
            configurations++;

            // Check for duplicates.
            if (!history.add(board.toString())) continue;

            if (debug) Utility.debug(board);

            // Check to see if achieved final goal.
            if (!board.containsValidSpot() && board.totalQueens() == n) {
                Utility.printResults(timeElapsed, configurations, board, -1, -1, debug, -1);
                return true;
            } else if (!board.containsValidSpot() && board.totalQueens() != n) {
                continue;
            }

            // Next state generator.
            // Each Queen will attempt to try only safe positions.
            for (Location loc : board.getAvailableSafePositions()) {
                Board temp = board.clone();
                temp.addQueen(loc.getX(), loc.getY());
                stack.push(temp);
            }
        }
        return false;
    }


}
