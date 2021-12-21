/**
 * List.java
 * @author Henry Choy, Mario Panuco, Nigel Erlund, Weifeng Bai, Thanyared Wong
 * CIS 22C, Final Project
 * Defines a doubly-linked list class
 */

import java.util.NoSuchElementException;

public class List<T> {
    private class Node {
        private T data;
        private Node next, prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private int length;
    private Node first, last, iterator;

    /**** CONSTRUCTOR ****/

    /**
     * Instantiates a new empty List
     * @postcondition new, empty list created
     */
    public List() {
        first = last = iterator = null;
        length = 0;
    }

    /**
     * Instantiates a new List by copying another List
     * @param original the List to make a copy of
     * @postcondition a new List object, which is an identical
     * but separate copy of the List original
     */
    public List(List<T> original) {
        if (original == null) {
            return;
        }
        if (original.length == 0) {
            length = 0;
            first = null;
            last = null;
            iterator = null;
        } else {
            Node temp = original.first;
            while (temp != null) {
                addLast(temp.data);
                temp = temp.next;
            }
            iterator = null;
        }
    }

    /**** ACCESSORS ****/

    /**
     * Returns the value stored in the first node
     * @precondition length != 0
     * @return the value stored at node first
     * @throws NoSuchElementException when precondition is violated
     */
    public T getFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("getFirst(): List is Empty. "
                    + "No data to access!");
        } else {
            return first.data;
        }
    }

    /**
     * Returns the value stored in the last node
     * @precondition list length is != 0
     * @return the value stored in the node last
     * @throws NoSuchElementException when precondition is violated
     */
    public T getLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("getLast(): List is Empty."
                    + " No data to access!");
        } else {
            return last.data;
        }
    }

    /**
     * Returns the current length of the list
     * @return the length of the list from 0 to n
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns value stored in Node referenced by iterator
     * @precondition !offEnd()
     * @return value referenced by iterator
     * @throws NullPointerException if precondition is violated
     */
    public T getIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("getIterator(): "
                    + "Iterator is off end of the list");
        } else {
            return iterator.data;
        }
    }

    /**
     * Returns whether the list is currently empty
     * @return whether the list is empty
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Determines whether two Lists have the same data in the same order
     * @param o - the List to compare to this List
     * @return whether the two Lists are equal
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof List)) {
            return false;
        } else {
            List<T> L = (List<T>) o;
            if (this.length != L.length) {
                return false;
            } else {
                Node temp1 = this.first;
                Node temp2 = L.first;
                while (temp1 != null) {
                    if (!(temp1.data.equals(temp2.data))) {
                        return false;
                    }
                    temp1 = temp1.next;
                    temp2 = temp2.next;
                }
                return true;
            }
        }
    }

    /**
     * Checks if iterator is off the end of the list
     * @return if the iterator is null
     */
    public boolean offEnd() {
        return iterator == null;
    }

    /**
     * Searches the List for the specified
     * value using the linear  search algorithm
     * @param value the value to search for
     * @return the location of value in the
     * List or -1 to indicate not found
     * Note that if the List is empty we will
     * consider the element to be not found
     * post: position of the iterator remains
     * unchanged
     */
    public int linearSearch(T value) {
        Node temp = first;
        for (int i = 1; i <= length; i++) {
            if (temp.data.equals(value)) {
                return i;
            }
            temp = temp.next;
        }
        return -1;
    }

    /**** MUTATORS ****/

    /**
     * Creates a new first element
     * @param data - the data to insert at the front of the list
     * @postcondition new Node object added to beginning of list
     */
    public void addFirst(T data) {
        if (length == 0) {
            first = last = new Node(data);
        } else {
            Node temp = new Node(data);
            temp.next = first;
            first.prev = temp;
            first = temp;
        }
        length++;
    }

    /**
     * Creates a new last element
     * @param data - the data to insert at the end of the list
     * @postcondition new Node object added to end of list
     */
    public void addLast(T data) {
        if (length == 0) {
            first = last = new Node(data);
        } else {
            Node temp = new Node(data);
            last.next = temp;
            temp.prev = last;
            last = temp;
        }
        length++;
    }

    /**
     * removes the element at the front of the list
     * @precondition length != 0
     * @postcondition Node object removed from beginning of list
     * @throws NoSuchElementException when precondition is violated
     */
    public void removeFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("removeFirst(): "
                    + "Cannot remove from an empty List!");
        } else if (length == 1) {
            first = last = iterator = null;
        } else {
            if (iterator == first) {
                iterator = null;
            }
            first = first.next;
            first.prev = null;
        }
        length--;
    }

    /**
     * removes the element at the end of the list
     * @precondition length != 0
     * @postcondition Node object removed from end of list
     * @throws NoSuchElementException when precondition is violated
     */
    public void removeLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("removeLast(): "
                    + "Cannot remove from an empty List!");
        } else if (length == 1) {
            first = last = iterator = null;
        } else {
            if (iterator == last) {
                iterator = null;
            }
            last = last.prev;
            last.next = null;
        }
        length--;
    }

    /**
     * removes the element at the place of iterator
     * @precondition iterator != null
     * @postcondition iterator points to null
     * @throws NullPointerException when precondition is violated
     */
    public void removeIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("removeIterator(): "
                    + "iterator is off end.");
        } else if (iterator == first) {
            removeFirst();
        } else if (iterator == last) {
            removeLast();
        } else {
            iterator.next.prev = iterator.prev;
            iterator.prev.next = iterator.next;
            iterator = null;
            length--;
        }
    }

    /**** ADDITIONAL OPERATIONS ****/

    /**
     * List with each value on its own line
     * At the end of the List a new line
     * @return the List as a String for display
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        Node temp = first;
        while (temp != null) {
            s.append(temp.data);
            temp = temp.next;
        }
        return s.toString();
    }

    /**
     * List with each value on its own numbered line
     * @return the List as a String for display
     */
    public String printNumberedList() {
        StringBuilder s = new StringBuilder();
        Node temp = first;
        int count = 1;
        while (temp != null) {
            s.append(count).append(": ");
            s.append(temp.data).append("\n");
            temp = temp.next;
            count++;
        }
        return s.toString();
    }

    /**
     * Places iterator at the start of the list
     * @postcondition iterator references the first node
     */
    public void placeIterator() {
        iterator = first;
    }

    /**
     * Creates element after iterator
     * @precondition !offEnd()
     * @param data - data to inserted after iterator
     * @postcondition new Node added after the iterator
     * @throws NullPointerException when precondition is violated
     */
    public void addIterator(T data) throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("addIterator(): "
                    + "iterator is off end of the list");
        }
        else if (iterator == last) {
            addLast(data);
        } else {
            Node N = new Node(data);
            Node temp = iterator;
            Node temp2 = iterator.next;
            N.next = temp2;
            temp.next = N;
            temp2.prev = N;
            N.prev = temp;
            length++;
        }
    }

    /**
     * Advances the iterator into the next position
     * @precondition !offEnd()
     * @postcondition Sets iterator to the node that is after the iterator
     * @throws NullPointerException when precondition is violated
     */
    public void advanceIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("advanceIterator(): "
                    + "iterator is off end and can not advance");
        } else {
            iterator = iterator.next;
        }
    }

    /**
     * Reverses the iterator into the previous position
     * @precondition !offEnd()
     * @postcondition Sets iterator to the node that is before the iterator
     * @throws NullPointerException when precondition is violated
     */
    public void reverseIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("reverseIterator():"
                    + " iterator is off end of the list");
        } else {
            iterator = iterator.prev;
        }
    }

    /**
     * Points the iterator at first
     * and then advances it to the
     * specified index
     * @param index the index where
     * the iterator should be placed
     * @precondition 0 < index <= length
     * @throws IndexOutOfBoundsException
     * when precondition is violated
     */
    public void iteratorToIndex(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > length) {
            throw new IndexOutOfBoundsException("iteratorToIndex():"
                    + "index is out of bounds");
        } else {
            placeIterator();
            for (int i = 1; i < index; i++) {
                advanceIterator();
            }
        }
    }
}