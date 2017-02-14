package Algorithms;

import Utility.Board;
import Utility.Utility;

import java.util.Random;

/**
 * Adds Queens randomly to the board.
 * If a Queen is placed on an invalid spot (another Queen or an attacked spot)
 * then pick a different random spot (without clearing the board) and continue.
 * <br/>
 * When there are no valid spots left, check how many Queens are on the board.
 * If there are not n Queens on the board, clear the board and try again.
 * <br/>
 * The largest board that this algorithm can solve in under 20
 * seconds on my Dell Latitude is n = 49, which takes 3 seconds.
 */
public class RandomWithPropagation {

    /**
     * Begin the search.
     * @param n         size of the board and number of Queens to place on the board
     * @param debug     if true, slows down the search and prints what the search is doing
     */
    public static void search (int n, long seed, boolean debug) {

        if (n < 4) return;

        long startTime = System.nanoTime(); // For measuring the time elapsed.
        Board board = new Board(n);
        Random r = new Random(seed);
        int configurations = -1; //The initial configuration doesn't count.

        while (true) {

            configurations++;

            while (board.containsValidSpot()) {

                int x = r.nextInt(n);
                int y = r.nextInt(n);

                if (!board.containsQueen(x ,y) && board.isSafe(x, y))
                    board.addQueen(x, y);
            }

            if (board.totalQueens() == n)
                break;

            if (debug) Utility.debug(board);

            board.clear();
        }

        int timeElapsed = (int)((System.nanoTime() - startTime) / 1_000_000_000L);
        Utility.printResults(timeElapsed, configurations, board, -1, -1, debug, seed);
    }

}
