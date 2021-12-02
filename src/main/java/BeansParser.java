public interface BeansParser {
    beans.Node parseTree(generated.Tree tree);
    beans.Node parseNode(generated.Node node);
    beans.Quadruple parseQuadruple(generated.Quadruple quadruple);
    beans.Type parseType(generated.Type type);
    beans.BasicService parseBasicService(generated.BasicService basicService);
    beans.Constructor parseConstructor(generated.Constructor constructor);

}
