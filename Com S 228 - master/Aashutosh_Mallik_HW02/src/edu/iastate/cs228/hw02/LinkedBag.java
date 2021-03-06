package edu.iastate.cs228.hw02;

import java.util.Random;

/**
 * A class of bags whose entries are stored in a chain of linked nodes.
 * The bag is never full.
 *
 * @author Aashutosh Mallik
 */
public final class LinkedBag<T> implements BagInterface<T> {
    private Node firstNode;       // Reference to first node
    private int numberOfEntries;

    public LinkedBag() {
        firstNode = null;
        numberOfEntries = 0;
    } // end default constructor

    /**
     * Sees whether this bag is empty.
     *
     * @return True if this bag is empty, or false if not.
     */
    public boolean isEmpty() {
        return numberOfEntries == 0;
    } // end isEmpty

    /**
     * Gets the number of entries currently in this bag.
     *
     * @return The integer number of entries currently in this bag.
     */
    public int getCurrentSize() {
        return numberOfEntries;
    } // end getCurrentSize

    /**
     * Adds a new entry to this bag.
     *
     * @param newEntry The object to be added as a new entry
     * @return True if the addition is successful, or false if not.
     */
    public boolean add(T newEntry)          // OutOfMemoryError possible
    {
        // Add to beginning of chain:
        Node newNode = new Node(newEntry);
        newNode.next = firstNode; // Make new node reference rest of chain
        // (firstNode is null if chain is empty)
        firstNode = newNode;      // New node is at beginning of chain
        numberOfEntries++;

        return true;
    } // end add

    /**
     * Retrieves all entries that are in this bag.
     *
     * @return A newly allocated array of all the entries in this bag.
     */
    public Object[] toArray() {
        // The cast is safe because the new array contains null entries
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[numberOfEntries]; // Unchecked cast

        int index = 0;
        Node currentNode = firstNode;
        while ((index < numberOfEntries) && (currentNode != null)) {
            result[index] = currentNode.data;
            index++;
            currentNode = currentNode.next;
        } // end while

        return result;
    } // end toArray

    @Override
    public BagInterface<T> union(BagInterface<T> anotherBag) {

        LinkedBag<T> newBag = new LinkedBag<>();
        LinkedBag<T> otherBag = (LinkedBag<T>) anotherBag;


        int index = 0;
        Node currentNode = firstNode;

        while ((index < numberOfEntries) && (currentNode != null)) {

            newBag.add(currentNode.data);
            index++;
            currentNode = currentNode.next;
        }

        index = 0;

        currentNode = otherBag.firstNode;

        while ((index < otherBag.numberOfEntries) && (currentNode != null)) {

            newBag.add(currentNode.data);
            index++;
            currentNode = currentNode.next;
        }


        return newBag;

    }

    @Override
    public BagInterface<T> intersection(BagInterface<T> anotherBag) {

        LinkedBag<T> newBag = new LinkedBag<>();
        LinkedBag<T> a;
        LinkedBag<T> b;

        int currSize = numberOfEntries;
        int anotherBagSize = anotherBag.getCurrentSize();

        if (anotherBagSize < currSize) {

            a = (LinkedBag<T>) anotherBag;
            b = this;
        } else {
            a = this;
            b = (LinkedBag<T>) anotherBag;
        }


        int index = 0;
        Node currentNode = a.firstNode;

        while ((index < a.getCurrentSize()) && (currentNode != null)) {

            Node n = b.getReferenceTo(currentNode.data);
            if (n != null) {
                newBag.add(n.data);
            }

            index++;
            currentNode = currentNode.next;
        }


        return newBag;

    }

    @Override
    public BagInterface<T> difference(BagInterface<T> anotherBag) {

        LinkedBag<T> newBag = new LinkedBag<>();


        int index = 0;
        Node currentNode = this.firstNode;

        while ((index < this.getCurrentSize()) && (currentNode != null)) {

            newBag.add(currentNode.data);
            index++;
            currentNode = currentNode.next;
        }


        index = 0;
        currentNode = ((LinkedBag<T>) anotherBag).firstNode;

        while ((index < anotherBag.getCurrentSize()) && (currentNode != null)) {

            newBag.remove(currentNode.data);
            index++;
            currentNode = currentNode.next;
        }


        return newBag;


    }

    @Override
    public T replace(T replacement) {
        Random generator = new Random();
        int randomPosition = generator.nextInt(numberOfEntries - 1);

        T result = null;

        if (!isEmpty() && (randomPosition >= 0)) {


            int index = 0;
            Node currentNode = firstNode;

            while (index <= randomPosition) {

                index++;
                currentNode = currentNode.next;
            }

            result = currentNode.data;
            currentNode.data = replacement;

        }
        return result;
    }

    @Override
    public void removeEvery(T anEntry) {

        Node nodeN = getReferenceTo(anEntry);
        while (nodeN != null) {
            //Replace located entry with entry in first node
            //then remove first node and adjust numberOfEntries.
            nodeN.data = firstNode.data;
            firstNode = firstNode.next;
            numberOfEntries--;
            nodeN = getReferenceTo(anEntry);
        }
    }

    /**
     * Counts the number of times a given entry appears in this bag.
     *
     * @param anEntry The entry to be counted.
     * @return The number of times anEntry appears in this bag.
     */
    public int getFrequencyOf(T anEntry) {
        int frequency = 0;

        int counter = 0;
        Node currentNode = firstNode;
        while ((counter < numberOfEntries) && (currentNode != null)) {
            if (anEntry.equals(currentNode.data)) {
                frequency++;
            } // end if

            counter++;
            currentNode = currentNode.next;
        } // end while

        return frequency;
    } // end getFrequencyOf

    /**
     * Tests whether this bag contains a given entry.
     *
     * @param anEntry The entry to locate.
     * @return True if the bag contains anEntry, or false otherwise.
     */
    public boolean contains(T anEntry) {
        boolean found = false;
        Node currentNode = firstNode;

        while (!found && (currentNode != null)) {
            if (anEntry.equals(currentNode.data))
                found = true;
            else
                currentNode = currentNode.next;
        } // end while

        return found;
    } // end contains

    // Locates a given entry within this bag.
    // Returns a reference to the node containing the entry, if located,
    // or null otherwise.
    private Node getReferenceTo(T anEntry) {
        boolean found = false;
        Node currentNode = firstNode;

        while (!found && (currentNode != null)) {
            if (anEntry.equals(currentNode.data))
                found = true;
            else
                currentNode = currentNode.next;
        } // end while

        return currentNode;
    } // end getReferenceTo

    /**
     * Removes all entries from this bag.
     */
    public void clear() {
        while (!isEmpty())
            remove();
    } // end clear

    /**
     * Removes one unspecified entry from this bag, if possible.
     *
     * @return Either the removed entry, if the removal
     * was successful, or null.
     */
    public T remove() {
        T result = null;
        if (firstNode != null) {
            result = firstNode.data;
            firstNode = firstNode.next; // Remove first node from chain
            numberOfEntries--;
        } // end if

        return result;
    } // end remove

    /**
     * Removes one occurrence of a given entry from this bag, if possible.
     *
     * @param anEntry The entry to be removed.
     * @return True if the removal was successful, or false otherwise.
     */
    public boolean remove(T anEntry) {
        boolean result = false;
        Node nodeN = getReferenceTo(anEntry);

        if (nodeN != null) {
            nodeN.data = firstNode.data; // Replace located entry with entry in first node

            firstNode = firstNode.next;  // Remove first node
            numberOfEntries--;

            result = true;
        } // end if

        return result;
    } // end remove

    private class Node {
        private T data; // Entry in bag
        private Node next; // Link to next node

        private Node(T dataPortion) {
            this(dataPortion, null);
        } // end constructor

        private Node(T dataPortion, Node nextNode) {
            data = dataPortion;
            next = nextNode;
        } // end constructor
    } // end Node

    @Override
    public String toString() {
        String x = "";
        for (Object n : toArray()) {
            x += n;
        }


        return x;
    }
} // end LinkedBag



