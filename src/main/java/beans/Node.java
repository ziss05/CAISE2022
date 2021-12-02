package beans;

public abstract class Node {
    TCMap tcMap;
    Type type;

    public void setTCMap(TCMap tcMap) {
        this.tcMap = tcMap;
    }

    public Type getType() {
        return this.type;
    }

    protected abstract void setType(Type type);

    @Override
    public String toString() {
        return "Node{" + type.toString() +
                ": tcMap= " + tcMap.toString() + '}';
    }
}
