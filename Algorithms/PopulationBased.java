package Algorithms;

import Utility.*;

import java.util.HashSet;
import java.util.Random;

/**
 * Initializes the board by putting n Queens on the diagonal.
 * <br/>
 * Uses the minimum conflict heuristic to move the Queen with the
 * most Queens attacking it to the safest position in the same column
 * (to a position with the least amount of Queens attacking it).
 * <br/>
 * If it gets stuck on a local optimum, it scraps the Board and
 * generates a new Board with each Queen placed on a random unique
 * row in its own column.
 * <br/>
 * The largest board that this algorithm can solve in under 20
 * seconds on my Dell Latitude is n = 19, which takes 7 seconds.
 */
public class PopulationBased {

    private static long seed;

    /**
     * Begin the search.
     * @param n         size of the board and number of Queens to place on the board
     * @param seed      the seed used for the random number generator
     * @param timeLimit the amount of time given to find a solution before giving up
     * @param debug     if true, slows down the search and prints what the search is doing
     */
    public static boolean search (int n, long seed, int timeLimit, boolean debug) {

        if (n < 4) return false;

        PopulationBased.seed = seed;

        long before = System.nanoTime(); // For measuring the time elapsed.
        HashSet<String> history = new HashSet<>(); // For catching duplicates.
        int configurations = -1; //The initial configuration doesn't count.
        int localOptimums = 0;
        int steps = -1;
        int timeElapsed = 0;

        Random r = new Random(seed);

        Board board = Board.makeBoardWithQueensOnDiagonal(n);

        while (board.numberOfQueensUnderAttack() != 0) {

            // If the time elapsed exceeds the time limit, then give up.
            timeElapsed = (int)((System.nanoTime() - before) / 1_000_000_000L);
            if (!debug && timeElapsed > timeLimit) {
                System.out.println("Exceeded " + timeLimit + " seconds.");
                return false;
            }

            // Check to see if duplicate (or if stuck at local optimum).
            if (!history.add(board.toString())) {
                steps = -1;
                localOptimums++;
                board = Board.makeBoardWithOneQueenPerColumn(n, r);
                if (debug) System.out.println("Stuck at local optimum. Utility.Utility.Board was reset.");
                continue;
            }

            configurations++;
            steps++;

            if (debug) Utility.debug(board);

            generateNextState(board, n);

        }
        Utility.printResults(timeElapsed, configurations, board, localOptimums, steps, debug, seed);
        return true;
    }

    /**
     * Helper method to generate the next state.
     * @param board     the parent board to generate child states from
     * @param n         the number of Queens in the problem
     */
    private static void generateNextState (Board board, int n) {

        HashSet<Location> queens = board.getQueens();

        PriorityQueue<Square> pq = new PriorityQueue<Square>();
        for (Location q : queens)
            pq.add(new Square(q, -1*board.numberOfQueensAttackingHere(q.getX(), q.getY())));

        generateNextState(board, pq.removeMax().location, n);

    }

    /**
     * Helper method to generate the next state.
     * <br/>
     * Move all the Queens that are being attacked to a safe
     * location on the same column, if such a location exists.
     * Otherwise move it to a random location on the same column.
     * @param board     the parent board to generate the child state from
     * @param queen     a Queen on the board
     * @param n         the number of Queens in the problem
     * @return          the next state (child board)
     */
    private static Board generateNextState (Board board, Location queen, int n) {

        // If Queen is safe, do not move it.
        if (board.isSafe(queen.getX(), queen.getY()))
            return board;

        PriorityQueue<Square> priorityQueue = new PriorityQueue<Square>();

        for (int y = 0; y < n; y++) {
            Location loc = new Location(queen.getX(), y);
            Integer value = board.numberOfQueensAttackingHere(loc.getX(), y);
            priorityQueue.add(new Square(loc, value));
        }

        board.removeQueen(queen.getX(), queen.getY());

        Square bestPosition = priorityQueue.removeMax();
        board.addQueen(queen.getX(), bestPosition.location.getY());

        return board;
    }

    private static class Square implements Comparable<Square> {
        private Location location;
        private Integer value;

        private Square (Location location, Integer value) {
            this.location = location;
            this.value = value;
        }

        @Override
        public int compareTo(Square square) {
            return square.value.compareTo(value);
        }

        @Override
        public String toString() {
            return location + " Value: " + value;
        }
    }

}
