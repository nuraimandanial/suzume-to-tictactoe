package Suzume_Ahmed.MyStack;

import javax.swing.text.html.HTMLDocument;
import java.util.Iterator;
import java.util.LinkedList;

public class GenericStack<E> {
  private final java.util.ArrayList<E> list = new java.util.ArrayList<>();
  public int getSize() {
    //link.listIterator();
    System.out.println();
    return list.size();
  }

  public E peek() {
    if(!list.isEmpty())
     return list.get(getSize() - 1);
    return null;
  }

  public void push(E o) {
    list.add(o);   }

  public E pop() {
    if(!isEmpty()) {
      E o = list.get(getSize() - 1);
      list.remove(getSize() - 1);
      return o;
    }
    return null;
  }

  public boolean contains(E e) {
    return list.contains(e);
  }

  public boolean isEmpty() {
    return list.isEmpty();   }

  public E get(int i) {
    return list.get(i-1);
  }
  
  @Override
  public String toString() {
    return "stack: " + list.toString();
  }

  public static void main(String[] args) {
    GenericStack<String> ob = new GenericStack<>();
    ob.push("Ahmed");
    ob.push("Hussien");
  }
}

