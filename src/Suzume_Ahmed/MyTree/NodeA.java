package Suzume_Ahmed.MyTree;

//next1,2,3 for normal traversal
//prev1 & 2 are reserved to postential crossovers
public class NodeA<E, U> {
    public E element;
    public U prevEdge;
    public NodeA<E, U> next1;
    public NodeA<E, U> next2;
    public NodeA<E, U> next3;

    public NodeA<E, U> prev1;
    public int height=0;
    public boolean station = false;
    NodeA<E, U> prev2;

    public NodeA(E element, U prevEdge) {
        this.element = element;
        this.prevEdge = prevEdge;
        next1 = next2 = next3 = null;
        prev1 = prev2 = null;
    }

}
