package Utility;

import java.nio.BufferUnderflowException;
import java.util.NoSuchElementException;

/**
 * I didn't like the default Java Priority Queue so I implemented my own.
 * It uses a max heap, as opposed to Java's which uses a min heap.
 * <br/>
 * In retrospect I could have negated the values to get them to work
 * with a min heap but nevertheless it was fun to practice writing
 * a Priority Queue from scratch!
 */
public class PriorityQueue <T extends Comparable<? super T>> {

    private T[] array;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Constructs the Priority Queue.
     */
    public PriorityQueue () {
        clear();
    }

    /**
     * Clears the Priority Queue.
     */
    public void clear () {

        if (size != 0) {
            for (int i = 0; i < array.length; i++) {
                array[i] = null;
            }
        }

        size = 0;

        updateCapacity(DEFAULT_CAPACITY);
    }

    /**
     * Gets the size of the Priority Queue.
     * @return  the size
     */
    public int size () {
        return size;
    }

    /**
     * Checks to see if the Priority Queue is empty.
     * @return  true if empty
     */
    public boolean isEmpty () {
        return size == 0;
    }

    /**
     * Resizes the Priority Queue.
     * @param newCapacity   the new size
     */
    @SuppressWarnings("unchecked")
    private void updateCapacity (int newCapacity) {

        if (newCapacity < size) return;

        T[] oldArray = array;
        array = (T[]) new Comparable[newCapacity];

        for (int i = 1; i <= size; i++)
            array[i] = oldArray[i];
    }

    /**
     * Adds an item to the Priority Queue.
     * @param item      the item to add
     */
    public void add (T item) {

        if (array.length - 1 == size)
            updateCapacity(array.length * 2 + 1);

        array[++size] = item;

        // percolate up.
        int child = size;
        int parent = child / 2;

        while (parent > 0) {
            if (array[parent].compareTo(item) < 0)
                array[child] = array[parent];
            else break;
            child = parent;
            parent /= 2;
        }
        array[child] = item;

    }

    /**
     * Removes the item with the maximum value.
     * @return      the item with the maximum value
     */
    public T removeMax () {
        if (isEmpty())
            throw new NoSuchElementException();

        T item = array[1];

        array[1] = array[size--];

        percolateDown(1);

        return item;
    }

    public T peek () {
        if (isEmpty())
            throw new BufferUnderflowException();
        return array[1];
    }

    /**
     * Restructures the heap (used when an item is removed).
     * @param parent    the index of the item that was removed.
     */
    private void percolateDown (int parent) {

        int child = parent * 2;
        T item = array[parent];

        while (child <= size) {

            if (child != size && array[child+1].compareTo(array[child]) > 0) {
                child = child+1;
            }

            if (array[child].compareTo(item) <= 0)
                break;

            array[parent] = array[child];
            parent = child;
            child *= 2;

        }

        array[parent] = item;

    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length; i++)
            sb.append(array[i] + " ");

        return new String(sb);
    }


}
