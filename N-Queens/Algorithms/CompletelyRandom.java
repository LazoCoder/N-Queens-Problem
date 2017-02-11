package Algorithms;

import Utility.Board;
import Utility.Utility;

import java.util.Random;

/**
 * Adds Queens randomly to the board.
 * If a Queen is placed on an invalid spot (another Queen or an attacked spot)
 * then clear the board and try again from the beginning.
 * <br/>
 * The largest board that this algorithm can solve in under 20
 * seconds on my Dell Latitude is n = 7, which takes 0 seconds.
 */
public class CompletelyRandom {

    /**
     * Begin the search.
     * @param n         size of the board and number of Queens to place on the board
     * @param debug     if true, slows down the search and prints what the search is doing
     */
    public static void search (int n, long seed, boolean debug) {

        if (n < 4) return;

        long startTime = System.nanoTime();
        Board board = new Board(n);
        Random r = new Random(seed);
        int configurations = -1; //The initial configuration doesn't count.

        while (true) {

            configurations++;

            while (board.containsValidSpot()) {

                int x = r.nextInt(n);
                int y = r.nextInt(n);

                if (!board.containsQueen(x ,y) && board.isSafe(x, y)) {
                    board.addQueen(x, y);
                } else {
                    // This else block is the only difference between this
                    // algorithm and the semi random algorithm. The semi
                    // random algorithm does not break if it lands a Queen
                    // on an invalid position, it tries again.
                    break;
                }
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
