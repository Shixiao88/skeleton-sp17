package synthesizer;

/**
 * Created by Administrator on 2017/4/12.
 */
public interface BoundedQueue<T> {

    int capacity();          // return size of the buffer

    int fillCount();         // return number of items currently in the buffer

    void enqueue(T x);  // add item x to the end

    T dequeue();        // delete and return item from the front

    T peek();           // return (but do not delete) item from the front

    default boolean isEmpty() {       // is the buffer empty (fillCount equals zero)?
        return fillCount() == 0;
    }


    default boolean isFull() {       // is the buffer full (fillCount is same as capacity)?
        return fillCount() == capacity();
    }

    boolean isIn(T x);
}