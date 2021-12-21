/**
 * Heap.java
 * @author Henry Choy, Mario Panuco, Nigel Erlund, Weifeng Bai, Thanyared Wong
 * CIS 22C, Final Project
 */

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public class Heap<T> {

    private int heapSize;
    private ArrayList<T> heap;
    private Comparator<T> comparator;


    /**Constructors/
     /**
     * Constructor for the Heap class
     * @param data an unordered ArrayList
     * @param comparator determines organization
     * of heap based on priority
     */
    public Heap(ArrayList<T> data, Comparator<T> comparator) {
        heap = data;
        heapSize = data.size() - 1;
        this.comparator = comparator;
        buildHeap();
    }

    /**Mutators*/

    /**
     * Converts an ArrayList into a valid
     * max heap. Called by constructor
     * Calls helper method heapify
     */
    public void buildHeap() {
        int n = heapSize;
        for (int i = (int) Math.floor(n / 2); i >= 1; i--) { // start at floor(n/2); we can ignore leaf nodes
            heapify(i);
        }
    }

    /**
     * helper method to buildHeap, remove, and sort
     * bubbles an element down to its proper location within the heap
     * @param index an index in the heap
     */
    private void heapify(int index) {
        int index_of_max = index;
        int l = get_left(index); // get the index of the left child of A[i] and store as l
        int r = get_right(index); // get the index of the right child of A[i] and store r

        if (l <= getHeapSize() && comparator.compare(getElement(l), getElement(index)) > 0) { // Check if l is off the end of the array (heap)
            // AND compare heap[i] to its left child
            index_of_max = l; // update index_of_max if left is bigger
        }
        if (r <= getHeapSize() && comparator.compare(getElement(r), getElement(index_of_max)) > 0) { // Check if r is off the end of the array (heap)
            // AND compare heap[i] to its right child
            index_of_max = r; // update index_of_max if right is bigger
        }
        if (index != index_of_max) {
            T temp = heap.get(index_of_max);
            heap.set(index_of_max, getElement(index));
            heap.set(index, temp);
            heapify(index_of_max);
        }
    }

    /**
     * Inserts the given data into heap
     * Calls helper method heapIncreaseKey
     * @param key the data to insert
     */
    public void insert(T key){
        heapSize++;
        heapIncreaseKey(heapSize, key); //start at the last index, i=Heap_size(A)
    }

    /**
     * Helper method for insert
     * bubbles an element up to its proper location
     * @param index the current index of the key
     * @param key the data
     */
    private void heapIncreaseKey(int index, T key) {
        heap.add(index, key);// write over existing value at i with key
        while (index > 1 && comparator.compare(heap.get(getParent(index)), heap.get(index)) < 0) { // while the																					// node
            T temp = heap.get(getParent(index));
            heap.set(getParent(index), heap.get(index));
            heap.set(index, temp);
            index = getParent(index);
        }
    }

    /**
     * removes the element at the specified index
     * @precondition heapSize != 0
     * Calls helper method heapify
     * @param index the index of the element to remove
     * @throws NoSuchElementException when precondition is violated
     */
	public void remove(int index) {
	if (heapSize == 0) {
		throw new NoSuchElementException("remove: cannot get root from an empty heap.");
	}else if (heapSize == 1) { // edge case
			heapSize--;
			heap.remove(index);
		} else {
			heapSize--;
			heap.remove(index);
			for (int i = index; i >= 1; i--) { // start at floor(n/2); we can ignore leaf nodes
				heapify(i);
			}
		}
	}

    /**Accessors*/

    /**
     * returns the maximum element (highest priority)
     * @return the max value
     */
    public T getMax(){
        return heap.get(1);
    }

    /**
     * returns the location (index) of the
     * parent of the element stored at index
     * @param index the current index
     * @return the index of the parent
     * @precondition 0 < i <= heap_size
     * @throws IndexOutOfBoundsException
     */
    public int getParent(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > heapSize) {
            throw new IndexOutOfBoundsException("getParent(): index out of bounds!");
        } else {
            return (int) (Math.floor(index / 2));
        }
    }

    /**
     * returns the location (index) of the
     * left child of the element stored at index
     * @param index the current index
     * @return the index of the left child
     * @precondition 0 < i <= heap_size
     * @throws IndexOutOfBoundsException
     */
    public int get_left(int index) throws IndexOutOfBoundsException {
        if (index > heapSize || index <= 0) {
            throw new IndexOutOfBoundsException("get_left(): index out of bounds");
        } else {
            return index * 2;
        }
    }

    /**
     * returns the location (index) of the
     * right child of the element stored at index
     * @param index the current index
     * @return the index of the right child
     * @precondition 0 < i <= heap_size
     * @throws IndexOutOfBoundsException
     */
    public int get_right(int index) throws IndexOutOfBoundsException {
        if (index > heapSize || index <= 0) {
            throw new IndexOutOfBoundsException("get_right(): index out of bounds");
        } else {
            return (index * 2) + 1;
        }
    }

    /**
     * returns the heap size (current number of elements)
     * @return the size of the heap
     */
    public int getHeapSize() {
        return heapSize;
    }

    /**
     *
     * @param index
     * @return
     * @throws IndexOutOfBoundsException
     */
    public T getElement(int index) throws IndexOutOfBoundsException {
        if (index > heapSize || index < 0) {
            throw new IndexOutOfBoundsException("getElement(): index out of bounds");
        } else {
            return heap.get(index);
        }
    }

    /**Additional Operations*/

    /**
     * Creates a String of all elements in the heap
     */
    @Override public String toString(){
        String s = "";
        for (int i = 1; i <= heapSize; i++) {
            s += heap.get(i);
        }
        return s;
    }

    /**
     * Uses the heap sort algorithm to
     * sort the heap into ascending order
     * Calls helper method heapify
     * @return an ArrayList of sorted elements
     * @postcondition heap remains a valid heap
     */
    public ArrayList<T> sort() {
        int n = heapSize;
        ArrayList<T> tempHeap = new ArrayList<>(heap);
        for (int i = n; i >= 2; i--) {
            T temp = heap.get(1);
            heap.set(1, heap.get(i));
            heap.set(i, temp);
            heapSize--;
            heapify(1);
        }
        ArrayList<T> tempHeap2 = heap;
        heap = tempHeap;
        heapSize = n;
        return tempHeap2;
    }
}