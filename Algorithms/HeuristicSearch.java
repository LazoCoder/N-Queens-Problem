package Algorithms;

import Utility.*;

import java.util.HashSet;
import java.util.Random;

/**
 * This algorithm is similar to the Iterative Repair algorithm
 * but has a few differences.
 * <br/>
 * First initialize the board by putting n Queens on the diagonal.
 * <br/>
 * Move all the Queens that are being attacked to a COMPLETELY SAFE
 * location on the same column (as opposed to the position with the
 * least amount of Queens attacking it). If such a location does not
 * exist, move it to a random location on the same column. This
 * process is done for all Queens per child and it generates 10
 * children.
 * <br/>
 * The best child is then found using the minimum conflict heuristic.
 * This is the same heuristic used in the Iterative Repair algorithm.
 * <br/>
 * Before starting the loop again from the beginning, check to see if
 * the child is stuck at a local optimum (this is very unlikely), if
 * it is, then scrap the board and create an entirely new board with
 * each Queen randomly placed in a unique y value per column.
 * <br/>
 * This algorithm takes more "steps" than the Iterative Repair
 * algorithm but it is much faster since it is very unlikely to get
 * stuck on a local optimum and have to restart. This significantly
 * reduces the total number of configurations attempted, and thus
 * reduces the time.
 * <br/>
 * This algorithm can solve n = 100 in about 5 seconds and it can
 * solve n = 150 in about 20 seconds (on my Dell Latitude).
 */
public class HeuristicSearch {

    private static long seed;

    /**
     * Begin the search.
     * @param n         size of the board and number of Queens to place on the board
     * @param timeLimit the amount of time given to find a solution before giving up
     * @param debug     if true, slows down the search and prints what the search is doing
     */
    public static boolean search (int n, long seed, int timeLimit, boolean debug) {

        if (n < 4) return false;

        HeuristicSearch.seed = seed;

        long before = System.nanoTime(); // For measuring the time elapsed.
        HashSet<String> history = new HashSet<>(); // Keeps track of configurations attempted.
        int configurations = -1; //The initial configuration doesn't count.
        int localOptimums = 0;
        int steps = -1;

        Random r = new Random(seed);

        PriorityQueue<Board> priorityQueue = new PriorityQueue<Board>();
        priorityQueue.add(Board.makeBoardWithQueensOnDiagonal(n));

        while (!priorityQueue.isEmpty()) {

            // If the time elapsed exceeds the time limit, then give up.
            int timeElapsed = (int)((System.nanoTime() - before) / 1_000_000_000L);
            if (!debug && timeElapsed > timeLimit) {
                System.out.println("Exceeded " + timeLimit + " seconds.");
                return false;
            }

            Board board = priorityQueue.removeMax();

            // Check to see if duplicate (or if stuck at local optimum).
            if (!history.add(board.toString())) {
                priorityQueue.clear();
                steps = -1;
                localOptimums++;
                priorityQueue.add(Board.makeBoardWithOneQueenPerColumn(n, r));
                if (debug) System.out.println("Stuck at local optimum. Utility.Utility.Board was reset.\n");
                continue;
            }

            configurations++;
            steps++;

            if (debug) Utility.debug(board);

            // Check to see if achieved final goal.
            if (board.numberOfQueensUnderAttack() == 0) {
                Utility.printResults(timeElapsed, configurations, board, localOptimums, steps, debug, seed);
                return true;
            }

            generateNextStates(priorityQueue, board, n);

        }
        return false;
    }

    /**
     * Helper method to generate the next states.
     * @param queue     the priority queue that the states will be added to
     * @param board     the parent board to generate child states from
     * @param n         the number of Queens in the problem
     */
    private static void generateNextStates (PriorityQueue<Board> queue, Board board, int n) {

        // Keeping these two variables outside of the loop greatly increases speed.
        HashSet<Location> queens = new HashSet<>();
        Random r = new Random(seed);

        // Generates 10 children.
        for (int i = 0; i < 10; i++) {
            Board childBoard = generateNextState(board, queens, r, n);
            queue.add(childBoard);
        }

    }

    /**
     * Helper method to generate the next state.
     * <br/>
     * Move all the Queens that are being attacked to a safe
     * location on the same column, if such a location exists.
     * Otherwise move it to a random location on the same column.
     * @param board     the parent board to generate the child state from
     * @param queens    the Queens on the board
     * @param r         the random number generator
     * @param n         the number of Queens in the problem
     * @return          the next state (child board)
     */
    private static Board generateNextState (Board board, HashSet<Location> queens, Random r, int n) {

        Board childBoard = board.clone();
        queens.clear();
        queens.addAll(childBoard.getQueens());

        // For each Queen on the parent board.
        for (Location queen : queens) {

            // If Queen is safe, do not move it.
            if (childBoard.isSafe(queen.getX(), queen.getY()))
                continue;

            // Otherwise remove it.
            childBoard.removeQueen(queen.getX(), queen.getY());

            // New location will have a random y value, if no spot is safe.
            int y = (queen.getY() + r.nextInt(n)) % n;

            // Look for safe spot on the same column.
            for (int j = 0; j < n; j++) {
                if (childBoard.isSafe(queen.getX(), j)) {
                    y =  j;
                    break;
                }
            }

            childBoard.addQueen(queen.getX(), y);
        }

        return childBoard;
    }


}
