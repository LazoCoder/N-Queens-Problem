package Algorithms;

import Utility.Board;
import Utility.*;

import java.util.HashSet;
import java.util.Stack;

/**
 * A completely blind depth first search. This is essentially
 * a brute force algorithm. All positions that do not already
 * contain Queens are checked.
 * <br/>
 * The largest board that this algorithm can solve in under 20
 * seconds on my Dell Latitude is n = 6, which takes 2 seconds.
 */
public class BlindSearchNaive {

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
        int configurations = -1; // The initial configuration doesn't count.

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
            if (board.totalQueens() == n && board.numberOfQueensUnderAttack() == 0) {
                Utility.printResults(timeElapsed, configurations, board, -1, -1, debug, -1);
                return true;
            } else if (board.totalQueens() == n && board.numberOfQueensUnderAttack() != 0) {
                continue;
            }

            // Next state generator.
            // Each Queen will attempt to try every single available position.
            for (Location loc : board.getAvailablePositions()) {
                Board temp = board.clone();
                temp.addQueen(loc.getX(), loc.getY());
                stack.push(temp);
            }
        }
        return false;
    }


}
