package Algorithms;

import Utility.Board;
import Utility.*;

import java.util.HashSet;
import java.util.Stack;

/**
 * A blind depth first search. This one is similar to the naive
 * blind search but instead of trying every single possible
 * position, it limits each Queen to its own column.
 * <br/>
 * The largest board that this algorithm can solve in under 20
 * seconds on my Dell Latitude is n = 8, which takes 13 seconds.
 */
public class BlindSearchIntermediate {

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

            int column = findFirstColumnWithNoQueen(board);

            // Next state generator.
            // Each Queen will attempt to try every position in its column.
            for (int y = 0; y < n; y++) {
                if (!board.containsQueen(column, y)) {
                    Board temp = board.clone();
                    temp.addQueen(column, y);
                    stack.push(temp);
                }
            }

        }
        return false;
    }

    /**
     * Helper method that finds the first empty column.
     * @param board     the board to search
     * @return          returns the index of the first empty column
     */
    private static int findFirstColumnWithNoQueen (Board board) {
        for (int x = 0; x < board.getSize(); x++) {

            if (!doesThisColumnHaveQueen(board, x)) {
                return x;
            }

        }

        // This method makes the assumption that such a column exists.
        throw new RuntimeException("All columns contain a Queen.");
    }

    /**
     * Helper method to check if a particular column contains a Queen.
     * @param board     the board to search
     * @param column    the column to search
     * @return          true if the column is empty
     */
    private static boolean doesThisColumnHaveQueen (Board board, int column) {

        for (int y = 0; y < board.getSize(); y++) {
            if (board.containsQueen(column, y)) {
                return true; // Found Queen, go to next column.
            }
        }

        return false;
    }

}
