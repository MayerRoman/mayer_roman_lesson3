/**
 * Created by Mayer Roman on 11.05.2016.
 */
public interface TwoWayIterable <T extends Comparable>{
    TwoWayIterator<T> getTwoWayIterator();
}
