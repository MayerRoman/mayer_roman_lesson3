package main;

import converter.impl.ConverterFromIntToString;
import my_linked_list.MyLinkedList;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Mayer Roman on 13.05.2016.
 */
public class Main {

    public static void main(String[] args) {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        try {
            list.get(15);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e);
        }

        Iterator<Integer> iterator = list.iterator();

        try {
            while (true) {
                iterator.next();
            }
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }

        try {
            for (int i : list) {
                list.add(33);
            }

        } catch (ConcurrentModificationException e) {
            System.out.println(e);
        }

        System.out.println("List:");
        for (int i : list) {
            System.out.print(i + " ");
        }
        System.out.println();


        MyLinkedList<String> listOfStrings = list.map(new ConverterFromIntToString());

        System.out.println("New list returned list.map():");
        for (String s : listOfStrings) {
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
