package my_linked_list;

/**
 * Created by Mayer Roman on 11.05.2016.
 */
public interface TwoWayIterator<T extends Comparable>{

    boolean hasPrev();

    boolean hasNext();

    T getPrev();

    T getNext();
}
