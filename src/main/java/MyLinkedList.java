import java.util.Iterator;

/**
 * Created by Mayer Roman on 05.05.2016.
 */
public class MyLinkedList <T extends Comparable<T>> implements Iterable<T> {
    private int size = 0;
    private Node<T> firstNode;
    private Node<T> lastNode;


    public void add(T element) {
        Node<T> newNode = new Node<T>(element, lastNode, firstNode);
        if (size == 0) {
            firstNode = newNode;
            lastNode = newNode;
        } else {
            lastNode.next = newNode;
            firstNode.prev = newNode;
            lastNode = newNode;
        }
        size++;
    }

    public void add(int elementIndex, T element) {
        if (elementIndex < 0 || elementIndex > size) {
            return;
        }

        Node<T> newElement = new Node<T>(element);

        if (size == 0) {
            firstNode = newElement;
            lastNode = newElement;
        }

        else {
            Node<T> nextElement = findNode(elementIndex);
            Node<T> prevElement = nextElement.prev;;


            newElement.next = nextElement;
            newElement.prev = prevElement;
            prevElement.next = newElement;
            nextElement.prev = newElement;

            if (elementIndex == 0) {
                firstNode = newElement;
            }
            if (elementIndex == size) {
                lastNode = newElement;
            }
        }

        size++;
    }

    public T get(int elementIndex) {
        if (elementIndex < 0 || elementIndex >= size) {
            return null;
        }

        return findNode(elementIndex).element;
    }

    public void remove(int elementIndex) {
        if (elementIndex < 0 || elementIndex >= size) {
            return;
        }

        if (size == 1) {
            unlinkNode(firstNode);
            firstNode = null;
            lastNode = null;
        }

        else {
            Node<T> deletedNode = findNode(elementIndex);

            Node<T> prevNode = deletedNode.prev;
            Node<T> nextNode = deletedNode.next;

            unlinkNode(deletedNode);

            prevNode.next = nextNode;
            nextNode.prev = prevNode;
            if (elementIndex == 0) {
                firstNode = nextNode;
            }
            if (elementIndex == size - 1) {
                lastNode = prevNode;
            }
        }

        size--;
    }


    private void unlinkNode(Node<T> node) {
        node.prev = null;
        node.next = null;
        node.element = null;
    }

    private Node<T> findNode(int nodeIndex) {
        int fromWhichSideToSearch = size / 2;

        Node<T> tempNode;

        if (nodeIndex <= fromWhichSideToSearch) {
            tempNode = firstNode;
            for (int i = 0; i < nodeIndex; i++) {
                tempNode = tempNode.next;
            }
            return tempNode;
        }

        else {
            tempNode = lastNode;
            for (int i = size - 1; i > nodeIndex; i--) {
                tempNode = tempNode.prev;
            }
            return tempNode;
        }
    }



    public int size() {
        return size;
    }

    public void sort() {
        firstNode.element.compareTo(lastNode.element);

        if (size == 0) {
            return;
        }

        int left = 1;
        int right = size;
        boolean swapped = true;
        T temp;

        Node<T> prev = firstNode;
        Node<T> next = firstNode.next;

        while (left < right && swapped) {
            swapped = false;

            for (int i = left; i < right; i++) {
                if (prev.element.compareTo(next.element) > 0) {
                    temp = prev.element;
                    prev.element = next.element;
                    next.element = temp;
                    swapped = true;
                }
                if (i < right - 1) {
                    prev = next;
                    next = next.next;
                }
            }
            right--;
            next = prev;
            prev = prev.prev;

            if (swapped) {
                swapped = false;
                for (int i = right; i > left; i--) {
                    if (prev.element.compareTo(next.element) > 0) {
                        temp = prev.element;
                        prev.element = next.element;
                        next.element = temp;
                        swapped = true;
                    }
                    if (i > left + 1) {
                        next = prev;
                        prev = prev.prev;
                    }
                }
                left++;
                prev = next;
                next = next.next;
            }
        }
    }


    public TwoWayIteratorImpl getIterator() {
        return new TwoWayIteratorImpl(firstNode, size);
    }

    public Iterator<T> iterator() {
        return new OneWayIterator(firstNode, size);
    }


    private static class Node<T extends Comparable> {
        T element;
        Node<T> prev;
        Node<T> next;

        Node(T element, Node<T> prev, Node<T> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }

        Node(T element) {
            this.element = element;
        }
    }

    private class TwoWayIteratorImpl implements TwoWayIterator {
        private Node<T> position;

        private int counter = 0;
        private int listSize;

        TwoWayIteratorImpl(Node<T> firstElement, int listSize) {
            position = firstElement;
            this.listSize = listSize;
        }

        public boolean hasPrev() {
            if (counter > 1) {
                return true;
            }
            return false;
        }

        public boolean hasNext() {
            if (counter < listSize) {
                return true;
            }
            return false;
        }

        public T getPrev() {
            if (counter == 1) {
                return null;
            }
            position = position.prev;
            counter--;
            return position.element;
        }

        public T getNext() {
            if (counter == 0) {
                counter++;
                return position.element;
            }
            if (counter == listSize) {
                return null;
            }
            position = position.next;
            counter++;
            return position.element;
        }
    }

    private class OneWayIterator implements Iterator<T> {
        private Node<T> position;
        private int counter = 0;
        private int listSize;

        OneWayIterator(Node<T> firstElement, int listSize) {
            position = firstElement;
            this.listSize = listSize;
        }

        public boolean hasNext() {
            if (counter < listSize) {
                return true;
            }
            return false;
        }

        public T next() {
            if (counter == 0) {
                counter++;
                return position.element;
            }
            if (counter < listSize) {
                position = position.next;
                counter++;
                return position.element;
            }
            return null;
        }
    }
}
