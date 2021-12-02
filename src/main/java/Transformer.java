import beans.*;
import generated.Tree;

public class Transformer implements BeansParser {
    private static Transformer instance;

    private Transformer() {
    }

    public static Transformer getInstance() {
        if (instance == null) {
            instance = new Transformer();
        }
        return instance;
    }

    @Override
    public Node parseTree(Tree tree) {
        return parseNode(tree.getNode());
    }

    @Override
    public Node parseNode(generated.Node node) {
        if (node == null){return null;}
        Node rootNode;
        if (node instanceof generated.BasicService) {
            rootNode = parseBasicService((generated.BasicService) node);
        } else {
            rootNode = parseConstructor((generated.Constructor) node);
        }
        return rootNode;
    }

    @Override
    public Quadruple parseQuadruple(generated.Quadruple quadruple) {
        if (quadruple == null){return null;}
        return new Quadruple(quadruple.getD1(), quadruple.getC1(), quadruple.getD2(), quadruple.getC2());
    }

    @Override
    public Type parseType(generated.Type type) {
        if (type == null){return null;}
        switch (type) {
            case AND:
                return Type.AND;
            case XORC:
                return Type.XORC;
            case XORD:
                return Type.XORD;
            case SEQ:
                return Type.SEQ;
        }
        return null;
    }

    @Override
    public BasicService parseBasicService(generated.BasicService basicService) {
        if (basicService == null){return null;}
        TCMap tcMap = new TCMap();
        for (generated.Quadruple q : basicService.getQuadruple()) {
            tcMap.add(parseQuadruple(q));
        }
        return new BasicService(tcMap);
    }

    @Override
    public Constructor parseConstructor(generated.Constructor constructor) {
        if (constructor == null){return null;}
        Node child1;
        Node child2;
        if (constructor.getLeft() instanceof generated.BasicService) {
            child1 = parseBasicService((generated.BasicService) constructor.getLeft());
        } else {
            child1 = parseConstructor((generated.Constructor) constructor.getLeft());
        }
        if (constructor.getRight() instanceof generated.BasicService) {
            child2 = parseBasicService((generated.BasicService) constructor.getRight());
        } else {
            child2 = parseConstructor((generated.Constructor) constructor.getRight());
        }
        return new Constructor(parseType(constructor.getType()),child1,child2);
    }
}
