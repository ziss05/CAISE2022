package beans;

public class Constructor extends Node {
    Node leftChild;
    Node rightChild;

    public Constructor(Type type, Node leftChild, Node rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        setType(type);
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    @Override
    protected void setType(Type type) {
        super.type = type;
    }

    @Override
    public String toString() {
        if (tcMap==null){
            return "Constructor{" + type.toString()+
                    ": tcMap=null, leftChild=" + leftChild.toString() +
                    ", rightChild=" + rightChild.toString() +
                    "}";
        }
        return "Constructor{" + type.toString()+
                ": tcMap=" + tcMap.toString() +
                "; leftChild=" + leftChild.toString() +
                ", rightChild=" + rightChild.toString() +'}';
    }
}
