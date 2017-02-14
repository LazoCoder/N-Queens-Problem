package Algorithms;

import Utility.Board;

/**
 * Uses a formula to calculate where to put the Queens.
 * This is not a search algorithm, it is an explicit solution.
 * <br/>
 * The solution for n = 1000 can be completed after 6 seconds.
 * The solution for n = 2000 can be completed after 44 seconds.
 * These solutions have been tested on my Dell Latitude.
 */
public class ExplicitSolution {

    public static void run (int n) {

        Board board = new Board(n);

        long before = System.nanoTime();

        // Determines which formula to follow based on the size of n.
        if (n%2 == 0 && (n-2)%6 != 0)
            equationOne(board, board.getSize());
        else if (n%2 == 0 && (n)%6 != 0)
            equationTwo(board, board.getSize());
        else
            board = equationThree(board, board.getSize());

        int timeElapsed = (int)((System.nanoTime() - before) / 1_000_000_000L);

        System.out.println("N = " + n);
        System.out.println("Time Elapsed:\t\t\t" + timeElapsed + " seconds");
        //System.out.println("Total Queens:\t\t\t" + board.totalQueens());
        //System.out.println("Queens Under Attack:\t" + board.numberOfQueensUnderAttack());
        System.out.println(board);
        System.out.println();
        System.out.println();
    }

    private static void equationOne (Board board, int n) {
        for (int i = 1; i <= n/2; i++) {
            board.addQueen(i-1, 2*i-1);
            board.addQueen(n/2 + i-1, 2*i - 1-1);
        }
    }

    private static void equationTwo (Board board, int n) {
        for (int i = 1; i <= n/2; i++) {
            int y = n - (2*i + n/2 - 3%n)-1;
            if (y < 0) y += n;
            board.addQueen(i-1, (1 + (2*i + n/2 - 3%n)-1)%n);
            board.addQueen(n+1-i-1, y);
        }
    }

    private static Board equationThree (Board board, int n) {

        Board b = board.clone();
        equationOne(b, n-1);
        b.addQueen(board.getSize()-1, board.getSize()-1);

        if (b.numberOfQueensUnderAttack() != 0) {
            b = board.clone();
            equationTwo(b, n-1);
            b.addQueen(board.getSize()-1, board.getSize()-1);
        }

        return b;
    }


}
