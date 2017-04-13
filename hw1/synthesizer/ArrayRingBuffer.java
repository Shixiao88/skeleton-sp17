// TODO: Make sure to make this class a part of the synthesizer package
package synthesizer;
import java.util.Iterator;


//TODO: Make sure to make this class and all of its methods public
//TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        rb = (T[]) new Object[capacity];
        this.capacity = capacity;
        first = 0;
        last = 0;
        this.fillCount = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
        if (isFull()) {
            throw new RuntimeException("Ring Buffer Overflow");
        }
        rb[last] = x;
        last = getNext(last);
        this.fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T removed = rb[first];
        rb[first] = null;
        first = getNext(first);
        this.fillCount -= 1;
        return removed;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        // TODO: Return the first item. None of your instance variables should change.
        return rb[first];
    }

    /* helper function to get the next index with a given index */
    private int getNext(int index) {
        if (index < capacity-1) {
            return index+1;
        }
        return 0;
    }

    @Override
    public boolean isIn(T x) {
        if (isEmpty()) {return false;}
        for (int _ = 0; _ <= this.capacity; _ += 1) {
            int first_copy = first;

            if (rb[first_copy].equals(x)) {
                return true;
            }
            first_copy = getNext(first_copy);
        }
        return false;
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.

    @Override
    public Iterator<T> iterator(){
        return new ListIterator();
    }

    public class ListIterator implements Iterator<T>{
        private int pos;
        private int iterated;

        public ListIterator() {
            pos = first;
            iterated = 0;
        }
        public boolean hasNext() {
            return iterated < fillCount;
        }

        public T next() {
            T val = rb[pos];
            pos = getNext(pos);
            iterated += 1;
            return val;
        }
    }
}
