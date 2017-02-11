package Utility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * A representation of the board with the Queens on it.
 * <br/>
 * Contains methods for adding & removing Queens, as well as
 * counting how many Queens are on the board, checking if
 * particular locations are safe and more...
 * <br/>
 * This class does not contain a 2D array to represent the board.
 * Instead it contains a hash set of the locations of the Queens
 * and a map of which locations are being attacked by Queens.
 * <br/>
 * The heuristic used for the searches is the number of Queens
 * being attacked, as per the method numberOfQueensUnderAttack().
 * <br/>
 * Less Queens being attacked means that the board is better.
 * This is reflected in the compareTo() method where the result
 * will be positive if this board has less Queens under attack
 * than the board it is being compared to.
 */
public class Board implements Comparable<Board> {

    private int n; // The number of Queens in N-Queens.

    private HashSet<Location> setOfQueenLocations;

    /**
     * The key is a location and the value is the number of Queens
     * that can attack that location. Queens can attack through
     * each other, meaning that if there are 3 Queens lined up in
     * a row, the location of the Queen on the right will have a
     * score of 2.
     */
    private HashMap<Location, Integer> mapOfQueenAttackLocations;

    /**
     * Constructs the board.
     * @param n     the size of the board
     */
    public Board (int n) {
        this.n = n;
        clear();
    }

    /**
     * Clears the entire board.
     */
    public void clear () {
        mapOfQueenAttackLocations = new HashMap<>();
        setOfQueenLocations = new HashSet<>();
    }

    /**
     * Adds a Queen to a location on the board.
     * @param x     the x coordinate of the location
     * @param y     the y coordinate of the location
     */
    public void addQueen (int x, int y) {

        Location loc = new Location(x, y);

        if (!isInBounds(loc)) {
            throw new IndexOutOfBoundsException();
        }

        if (containsQueen(loc)) {
            throw new IllegalStateException();
        }

        setOfQueenLocations.add(loc);
        loadAttack(loc);
    }

    /**
     * Removes a Queen that is at a particular location on the board.
     * @param x     the x coordinate of the location
     * @param y     the y coordinate of the location
     */
    public void removeQueen (int x, int y) {

        if (!containsQueen(x, y)) {
            throw new RuntimeException("No Queen here.");
        }

        setOfQueenLocations.remove(new Location(x, y));
        mapOfQueenAttackLocations.clear();
        setOfQueenLocations.forEach((Location l) -> loadAttack(l));

    }

    /**
     * Checks to see if there is a Queen at a particular location.
     * @param x     the x coordinate of the location
     * @param y     the y coordinate of the location
     * @return      true if there is a Queen at the location
     */
    public boolean containsQueen(int x, int y) {
        Location loc = new Location(x, y);

        if (!isInBounds(loc)) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return containsQueen(loc);
    }

    /**
     * Checks to see if there is a Queen at a particular location.
     * @param location  the location to check
     * @return          true if there is a queen at the location
     */
    private boolean containsQueen (Location location) {
         return setOfQueenLocations.contains(location);
    }

    /**
     * Checks to see if there are any spots on the board that do not
     * contain a Queen and that are safe from all the Queens.
     * @return      returns true if there is at least one valid spot left
     */
    public boolean containsValidSpot () {
        int validSpots = n * n;
        validSpots -= mapOfQueenAttackLocations.size();

        for (Location l : setOfQueenLocations) {
            if (!mapOfQueenAttackLocations.containsKey(l)) {
                validSpots--;
            }
        }

        return validSpots != 0;
    }

    /**
     * Checks to see if the specified location is not off the board.
     * @param loc       the location to check
     * @return          true if the location is on the board
     */
    private boolean isInBounds (Location loc) {
        return
                loc.getX() < n
                        && loc.getX() >= 0
                        && loc.getY() < n
                        && loc.getY() >= 0;
    }

    /**
     * Puts all the possible moves of a Queen into the hashSet.
     * @param queen     the location of the Queen
     */
    private void loadAttack (Location queen) {

        // Load horizontal attacks.
        for (int x = 0; x < n; x++) {
            Location loc = new Location(x, queen.getY());
            addAttack(loc);
        }
        removeAttack(queen); // Queen cannot attack itself.

        // Load vertical attacks.
        for (int y = 0; y < n; y++) {
            Location loc = new Location(queen.getX(), y);
            addAttack(loc);
        }
        removeAttack(queen); // Queen cannot attack itself.

        // Load Diagonal attacks.
        for (int i = 1; i < n; i++) { // South-East diagonal.
            Location loc = new Location(queen.getX()+i, queen.getY()+i);
            if (!isInBounds(loc)) break;
            addAttack(loc);
        }
        for (int i = 1; i < n; i++) { // South-West diagonal.
            Location loc = new Location(queen.getX()-i, queen.getY()+i);
            if (!isInBounds(loc)) break;
            addAttack(loc);
        }
        for (int i = 1; i < n; i++) { // North-East diagonal.
            Location loc = new Location(queen.getX()+i, queen.getY()-i);
            if (!isInBounds(loc)) break;
            addAttack(loc);
        }
        for (int i = 1; i < n; i++) { // North-West diagonal.
            Location loc = new Location(queen.getX()-i, queen.getY()-i);
            if (!isInBounds(loc)) break;
            addAttack(loc);
        }

    }

    /**
     * Adds an attack to the hash map that contains the locations
     * that can be attacked by a Queen.
     * @param loc   the location to add
     */
    private void addAttack (Location loc) {
        if (!mapOfQueenAttackLocations.containsKey(loc)) {
            mapOfQueenAttackLocations.put(loc, 1);
        } else {
            int amountOfAttacks = mapOfQueenAttackLocations.get(loc);
            mapOfQueenAttackLocations.put(loc, amountOfAttacks+1);
        }
    }

    /**
     * Removes an attack from the hash map that contains the locations
     * that can be attacked by a Queen.
     * @param loc   the location to remove
     */
    private void removeAttack (Location loc) {
        if (!mapOfQueenAttackLocations.containsKey(loc)) {
            return;
        } else if (mapOfQueenAttackLocations.get(loc) == 1) {
            mapOfQueenAttackLocations.remove(loc);
        } else {
            int amountOfAttacks = mapOfQueenAttackLocations.get(loc);
            mapOfQueenAttackLocations.put(loc, amountOfAttacks-1);
        }
    }

    /**
     * Checks to see if a particular location is safe from being attacked.
     * @param x     the x coordinate of the location
     * @param y     the y coordinate of the location
     * @return      true if no Queens attack at the specified location
     */
    public boolean isSafe (int x, int y) {
        Location loc = new Location(x, y);

        if (!isInBounds(loc)) {
            throw new ArrayIndexOutOfBoundsException(loc.toString());
        }

        return !mapOfQueenAttackLocations.containsKey(loc);
    }

    /**
     * Gets a set of all the Queens currently on the board.
     * @return  the set of Queens on the board
     */
    public HashSet<Location> getQueens () {
        return setOfQueenLocations;
    }

    /**
     * Checks to see how many Queens are on an unsafe location.
     * <br/>
     * For example, if there are three Queens on the same row,
     * they will be counted as 1 each. The one in the middle
     * will not be counted as 2 even though it has two Queens
     * attacking it on either side.
     * @return  the number of Queens on an unsafe location.
     */
    public int numberOfQueensUnderAttack () {
        int count = 0;

        for (Location loc : setOfQueenLocations) {
            count += numberOfQueensAttackingHere(loc.getX(), loc.getY());
        }

        return count;
    }

    /**
     * Returns the number of Queens attacking at a particular location.
     * Queens can attack through each other. So if there are 3 Queens
     * in a row, the first one has a score of two.
     * @param x     the x coordinate of the location
     * @param y     the y coordinate of the location
     * @return      the number of Queens attacking at the location
     */
    public int numberOfQueensAttackingHere (int x, int y) {
        Location loc = new Location(x, y);

        if (!mapOfQueenAttackLocations.containsKey(loc)) {
            return 0;
        } else {
            return mapOfQueenAttackLocations.get(loc);
        }

    }

    /**
     * Gets the total number of Queens on the board.
     * @return  the quantity of Queens
     */
    public int totalQueens () {
        return setOfQueenLocations.size();
    }

    /**
     * Gets all the position on the board that are not Queens.
     * @return      a hashSet containing the available positions.
     */
    public HashSet<Location> getAvailablePositions () {
        HashSet<Location> availablePositions = new HashSet<>();

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                Location loc = new Location(x, y);
                availablePositions.add(loc);
            }
        }

        setOfQueenLocations.forEach((Location l) -> availablePositions.remove(l));

        return availablePositions;
    }

    /**
     * Gets all the position on the board that are not being attacked
     * by Queens and are not Queens.
     * @return      a hashSet containing the safe available positions.
     */
    public HashSet<Location> getAvailableSafePositions () {
        HashSet<Location> availablePositions = new HashSet<>();

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                Location loc = new Location(x, y);
                if (!setOfQueenLocations.contains(loc)
                        && !mapOfQueenAttackLocations.containsKey(loc)) {
                    availablePositions.add(loc);
                }
            }
        }

        return availablePositions;
    }

    /**
     * Returns the size of the Board.
     * @return      the size of the board;
     */
    public int getSize () {
        return n;
    }

    /**
     * Returns a deep copy of this board instance. Useful for making children.
     * @return  a deep copy of the board
     */
    public Board clone () {
        Board newBoard = new Board(n);
        newBoard.mapOfQueenAttackLocations.putAll(mapOfQueenAttackLocations);
        newBoard.setOfQueenLocations.addAll(setOfQueenLocations);
        return newBoard;
    }

    /**
     * Uses the heuristic score for the comparison.
     * @param board     the board to compare to
     * @return          negative, 0, or positive depending on if this score is
     *                  better, equal to, or worse than board's score
     */
    @Override
    public int compareTo(Board board) {
        return board.numberOfQueensUnderAttack() - numberOfQueensUnderAttack();
    }

    /**
     * Checks equality based on the Queen locations.
     * @param o     the board to compare to
     * @return      true if both boards have Queens in identical locations
     */
    @Override
    public boolean equals (Object o) {
        if (!(o instanceof Board)) {
            return false;
        }

        Board board = (Board) o;

        return board.getQueens().equals(getQueens());

    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {

                Location loc = new Location(x, y);

                if (setOfQueenLocations.contains(loc)) {
                    sb.append('Q');
                } else if (mapOfQueenAttackLocations.containsKey(loc)) {
                    sb.append('*');
                } else {
                    sb.append('-');
                }

                sb.append(" ");
            }
            sb.append("\n");
        }

        return new String(sb);
    }

    /**
     * Creates a new Board with n Queens on the diagonal.
     * @param n     the number of Queens
     * @return      the board
     */
    public static Board makeBoardWithQueensOnDiagonal (int n) {
        Board board = new Board(n);
        for (int i = 0; i < n; i++) {
            board.addQueen(i, i);
        }
        return board;
    }

    /**
     * Creates a new Board with one Queen on each column.
     * The y value is selected randomly but no two Queens
     * will be in the same row.
     * @param n     the size of the Board
     * @param r     the random object for selecting a y-value
     * @return      a new Board with each column containing one Queen
     */
    public static Board makeBoardWithOneQueenPerColumn (int n, Random r) {

        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i;
        }

        shuffleArray(array, r);

        Board board = new Board(n);

        for (int i = 0; i < n; i++) {
            board.addQueen(i, array[i]);
        }

        return board;
    }

    /**
     * Helper method for makeBoardWithOneQueenPerColumn().
     * This shuffles the array that will determine the unique y-value
     * for where each Queen will be put on its column.
     * @param array     the array to shuffle
     * @param r         the random object for selecting a y-value
     */
    private static void shuffleArray (int[] array, Random r) {
        for (int i = 0; i < array.length; i++) {
            swap(array, i, r.nextInt(array.length));
        }
    }

    /**
     * Helper method for shuffleArray().
     * Swaps to elements based on their index in the array.
     * @param array     the array to do the swap in
     * @param i         the index of the first element
     * @param j         the index of the second element
     */
    private static void swap (int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


}
