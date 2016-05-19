package my_linked_list;

import converter.Converter;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Mayer Roman on 05.05.2016.
 */
public class MyLinkedList<T extends Comparable<T>> implements Iterable<T>, TwoWayIterable<T> {
    private int modCount = 0;
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
        modCount++;
        size++;
    }

    public void add(int elementIndex, T element) {
        if (elementIndex < 0 || elementIndex > size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(elementIndex));
        }

        Node<T> newNode = new Node<T>(element);

        if (size == 0) {
            firstNode = newNode;
            lastNode = newNode;
        } else {
            Node<T> nextNode = findNode(elementIndex);
            Node<T> prevNode = nextNode.prev;


            newNode.next = nextNode;
            newNode.prev = prevNode;
            prevNode.next = newNode;
            nextNode.prev = newNode;

            if (elementIndex == 0) {
                firstNode = newNode;
            }
            if (elementIndex == size) {
                lastNode = newNode;
            }
        }

        modCount++;
        size++;
    }

    public T get(int elementIndex) {
        if (elementIndex < 0 || elementIndex >= size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(elementIndex));
        }

        return findNode(elementIndex).element;
    }

    public void remove(int elementIndex) {
        if (elementIndex < 0 || elementIndex >= size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(elementIndex));
        }

        if (size == 1) {
            unlinkNode(firstNode);
            firstNode = null;
            lastNode = null;
        } else {
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

        modCount++;
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
        } else {
            tempNode = lastNode;
            for (int i = size - 1; i > nodeIndex; i--) {
                tempNode = tempNode.prev;
            }
            return tempNode;
        }
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }


    public int size() {
        return size;
    }

    public void sort() {
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

    public <OUT extends Comparable<OUT>> MyLinkedList<OUT> map(Converter<T, OUT> converter) {
        MyLinkedList<OUT> newList = new MyLinkedList<OUT>();
        for (T elementFromThisList : this) {
            newList.add(converter.apply(elementFromThisList));
        }
        return newList;
    }


    public TwoWayIterator<T> twoWayIterator() {
        return new TwoWayIteratorImpl(firstNode, size);
    }

    public Iterator<T> iterator() {
        return new OneWayIterator(firstNode, size, modCount);
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

    private class TwoWayIteratorImpl implements TwoWayIterator<T> {
        private Node<T> position;

        private int expectedNumberOfModifications;
        private int counter = 0;
        private int listSize;

        TwoWayIteratorImpl(Node<T> firstElement, int listSize) {
            position = firstElement;
            this.listSize = listSize;
        }

        public boolean hasPrev() {
            return counter > 1;
        }

        public boolean hasNext() {
            return counter < listSize;
        }

        public T getPrev() {
            checkForModification();
            if (counter == 1) {
                throw new NoSuchElementException();
            }
            position = position.prev;
            counter--;
            return position.element;
        }

        public T getNext() {
            checkForModification();
            if (counter == 0) {
                counter++;
                return position.element;
            }
            if (counter == listSize) {
                throw new NoSuchElementException();
            }
            position = position.next;
            counter++;
            return position.element;
        }

        private void checkForModification() {
            if (expectedNumberOfModifications != modCount)
                throw new ConcurrentModificationException();
        }
    }

    private class OneWayIterator implements Iterator<T> {
        private Node<T> position;
        private int expectedNumberOfModifications;
        private int counter = 0;
        private int listSize;

        OneWayIterator(Node<T> firstElement, int listSize, int modCount) {
            position = firstElement;
            this.listSize = listSize;
            this.expectedNumberOfModifications = modCount;
        }

        public boolean hasNext() {
            if (expectedNumberOfModifications != modCount) {
                throw new ConcurrentModificationException();
            }
            return counter < listSize;
        }

        public T next() {
            if (expectedNumberOfModifications != modCount) {
                throw new ConcurrentModificationException();
            }
            if (counter == 0) {
                counter++;
                return position.element;
            }
            if (counter < listSize) {
                position = position.next;
                counter++;
                return position.element;
            }
            throw new NoSuchElementException();
        }
    }
}
