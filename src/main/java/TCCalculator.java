import beans.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class TCCalculator {
    private static TCCalculator instance;

    private TCCalculator() {
    }

    public static TCCalculator getInstance() {
        if (instance == null) {
            instance = new TCCalculator();
        }
        return instance;
    }

//    Basic Operations

    public BigDecimal addCostValues(BigDecimal c1, BigDecimal c2) {
        if (c1 == null || c2 == null) {
            return null;
        }
        return c1.add(c2);
    }

    public BigDecimal getMin(BigDecimal a, BigDecimal b) {
        HashSet<BigDecimal> set = new HashSet<>();
        set.add(a);
        set.add(b);
        return getMin(set);
    }

    public BigDecimal getMin(HashSet<BigDecimal> s) {
        if (s == null) {
            return null;
        } else if (s.size() == 0 ||  s.size() == 1&& s.contains(null)) {
                return null;
            } else {
                s.remove(null);
            }
        if (s.size()==1){return (BigDecimal) (s.toArray())[0];}
        return s.stream().min(BigDecimal::compareTo).get();
    }

    public BigDecimal getMax(BigDecimal a, BigDecimal b) {
        HashSet<BigDecimal> set = new HashSet<>();
        set.add(a);
        set.add(b);
        return getMax(set);
    }

    public BigDecimal getMax(HashSet<BigDecimal> s) {
        if (s == null || s.size() == 0 || s.contains(null)) {
            return null;
        }
        if (s.size()==1){return (BigDecimal) (s.toArray())[0];}
        return s.stream().max(BigDecimal::compareTo).get();
    }


    public BigDecimal selectLowerCost(BigDecimal d, TCMap t) {
        if (d == null) {
            return null;
        }
        HashSet<BigDecimal> set= new HashSet<>();
        TreeSet<Quadruple> tSet=t.getTCMap();
        for (Quadruple q : tSet
        ) {
            if (q.getD1().doubleValue() <= d.doubleValue() && d.doubleValue() < q.getD2().doubleValue()) {
                if (q.getGradient()==null){return null;}
                else set.add(q.getC1().add(q.getGradient().multiply(d.subtract(q.getD1()))));
            }
        }
        return getMin(set);
    }

    public BigDecimal selectUpperCost(BigDecimal d, TCMap t) {
        if (d == null) {
            return null;
        }
        HashSet<BigDecimal> set= new HashSet<>();
        TreeSet<Quadruple> tSet=t.getTCMap();
        for (Quadruple q : tSet
        ) {
            if (q.getD1().doubleValue() <d.doubleValue() && d.doubleValue() <= q.getD2().doubleValue()) {
                if (q.getGradient()==null){return null;}
                else set.add(q.getC1().add(q.getGradient().multiply(d.subtract(q.getD1()))));
            }
        }
        return getMin(set);
    }

    public BigDecimal selectMaxLowerCost(BigDecimal d, TCMap t) {
        if (d == null) {
            return null;
        }
        HashSet<BigDecimal> set= new HashSet<>();
        TreeSet<Quadruple> tSet=t.getTCMap();
        for (Quadruple q : tSet
        ) {
            if (q.getD1().doubleValue() <= d.doubleValue() && d.doubleValue() < q.getD2().doubleValue()) {
                if (q.getGradient()==null){return null;}
                else set.add(q.getC1().add(q.getGradient().multiply(d.subtract(q.getD1()))));
            }
        }
        return getMax(set);
    }

    public BigDecimal selectMaxUpperCost(BigDecimal d, TCMap t) {
        if (d == null) {
            return null;
        }
        HashSet<BigDecimal> set= new HashSet<>();
        TreeSet<Quadruple> tSet=t.getTCMap();
        for (Quadruple q : tSet
        ) {
            if (q.getD1().doubleValue() <d.doubleValue() && d.doubleValue() <= q.getD2().doubleValue()) {
                if (q.getGradient()==null){return null;}
                else set.add(q.getC1().add(q.getGradient().multiply(d.subtract(q.getD1()))));
            }
        }
        return getMax(set);
    }

    public Set<Tuple> getIntervalSet(Set<BigDecimal> pointSet) {
        if (pointSet == null) {
            return null;
        }
        HashSet<Tuple> intervals = new HashSet<>();
        TreeSet<BigDecimal> points = new TreeSet<>(pointSet);
        Iterator<BigDecimal> iterator = points.iterator();
        BigDecimal last;
        if (points.size() >= 2) {
            last = iterator.next();
        } else return null;
        while (iterator.hasNext()) {
            BigDecimal current = iterator.next();
            intervals.add(new Tuple(last, current));
            last = current;
        }
        return intervals;
    }

    //    calculate TCMap for overall service
    public TCMap calcTree(Node root) {
        if (root == null) {
            return null;
        }
        TCMap result = decideIfConstructor(root);
        return result==null?null:new TCMap(result);
    }

    private TCMap decideIfConstructor(Node node) {
        if (node instanceof Constructor) {
            TCMap map=calcComplex(node.getType(), ((Constructor) node).getLeftChild(), ((Constructor) node).getRightChild());
            node.setTCMap(map);
            return map;
        } else {
            return ((BasicService) node).getTCS();
        }
    }

    private TCMap calcComplex(Type type, Node node1, Node node2) {
        TCMap a;
        TCMap b;
        TCMap res;

        a = decideIfConstructor(node1);
        b = decideIfConstructor(node2);

        switch (type) {
            case SEQ:
                res = calcSEQ(a, b);
                break;
            case AND:
                res = calcAND(a, b);
                break;
            case XORC:
                res = calcXOR(a, b, Type.XORC);
                break;
            case XORD:
                res = calcXOR(a, b, Type.XORD);
                break;
            default:
                res = null;
                break;
        }
        return res;
    }

    private Set<Quadruple> concat(Quadruple a, Quadruple b) {
        TreeSet<Quadruple> concats = new TreeSet<>();
        boolean isNull=false;
        if (a.getGradient()==null||b.getGradient()==null){
            isNull=true;
        }

        if (!isNull && a.getGradient().doubleValue() < b.getGradient().doubleValue()) {
            concats.add(new Quadruple(a.getD1().add(b.getD1()), addCostValues(a.getC1(), b.getC1()),
                    a.getD2().add(b.getD1()), addCostValues(a.getC2(), b.getC1())));
            concats.add(new Quadruple(a.getD2().add(b.getD1()), addCostValues(a.getC2(), b.getC1()),
                    a.getD2().add(b.getD2()), addCostValues(a.getC2(), b.getC2())));
        } else {
            concats.add(new Quadruple(a.getD1().add(b.getD1()), addCostValues(a.getC1(), b.getC1()),
                    a.getD1().add(b.getD2()), addCostValues(a.getC1(), b.getC2())));
            concats.add(new Quadruple(a.getD1().add(b.getD2()), addCostValues(a.getC1(), b.getC2()),
                    a.getD2().add(b.getD2()), addCostValues(a.getC2(), b.getC2())));
        }

        return concats;
    }

    public TCMap calcSEQ(TCMap a, TCMap b) {
        if (a == null || b == null || !a.isFunctional() || !b.isFunctional()) {
            return null;
        }
        TCMap result = new TCMap();
        TreeSet<Quadruple> aSet=a.getTCMap();
        TreeSet<Quadruple> bSet=b.getTCMap();
        for (Quadruple qa : aSet) {
            for (Quadruple qb : bSet) {
                result.addAll(concat(qa, qb));
            }
        }
        return compress(result);
    }

    public TCMap calcEXT(TCMap tcMap, BigDecimal d) {
        if (tcMap == null || d == null) {
            return null;
        }
        TCMap map = new TCMap();
        map.add(new Quadruple(BigDecimal.ZERO, BigDecimal.ZERO, d, BigDecimal.ZERO));
        return (calcSEQ(tcMap, map));
    }

    private TreeSet<BigDecimal> getCombinedPointSet(TCMap a, TCMap b) {
        TreeSet<BigDecimal> points = new TreeSet<>();
        points.addAll(a.getPointSet());
        points.addAll(b.getPointSet());

        return points;
    }

    public TCMap calcAND(TCMap a, TCMap b) {
        if (a == null || b == null || !a.isFunctional() || !b.isFunctional()) {
            return null;
        }
        BigDecimal m = a.getMax().max(b.getMax());
        TCMap exta = calcEXT(a, m);
        TCMap extb = calcEXT(b, m);


        TCMap result = new TCMap();
        TreeSet<Tuple> intervals = new TreeSet<>();
        TreeSet<BigDecimal> points = getCombinedPointSet(exta, extb);
        intervals.addAll(getIntervalSet(points));

        BigDecimal c1;
        BigDecimal c2;
        for (Tuple t : intervals) {
            c1=addCostValues(selectLowerCost(t.getLow(),exta), selectLowerCost(t.getLow(),extb));
            c2=addCostValues(selectUpperCost(t.getUp(),exta), selectUpperCost(t.getUp(),extb));
            if (c1==null||c2==null){c1=null; c2=null;}
            result.add(new Quadruple(t.getLow(),c1 ,t.getUp(),c2));

        }

        return compress(result);
    }


    public TCMap calcXOR(TCMap a, TCMap b, Type xorPattern) {
        if (a == null || b == null || !a.isFunctional() || !b.isFunctional()) {
            return null;
        }
        TCMap result = new TCMap();
        TreeSet<Tuple> intervals = new TreeSet<>();
        TreeSet<BigDecimal> points = getCombinedPointSet(a, b);
        TCMap map = new TCMap();
        map.addAll(b.getTCMap());
        TreeSet<BigDecimal> intersects=(TreeSet<BigDecimal>) calcIntersectionPoints(map);
        for (Object o : intersects) {
            if (o instanceof BigDecimal && ((BigDecimal) o).compareTo(BigDecimal.ZERO) != 0&& ((BigDecimal) o).compareTo(BigDecimal.ZERO) !=-1) {
                points.add((BigDecimal) o);
            }
        }

        intervals.addAll(getIntervalSet(points));
        BigDecimal c1;
        BigDecimal c2;

        if (xorPattern == Type.XORC) {
            for (Tuple t : intervals) {
                c1=getMax(selectMaxLowerCost(t.getLow(),a), selectMaxLowerCost(t.getLow(),b));
                c2=getMax(selectMaxUpperCost(t.getUp(),a), selectMaxUpperCost(t.getUp(),b));
                if (c1==null||c2==null){c1=null; c2=null;}
                result.add(new Quadruple(t.getLow(),c1,t.getUp(), c2));
            }
        } else {

            for (Tuple t : intervals) {
                c1=getMin(selectLowerCost(t.getLow(),a), selectLowerCost(t.getLow(),b));
                c2=getMin(selectUpperCost(t.getUp(),a), selectUpperCost(t.getUp(),b));
                if (c1==null||c2==null){c1=null; c2=null;}
                result.add(new Quadruple(t.getLow(),c1,t.getUp(),c2));
            }
        }
        return compress(result);
    }

    public Set<BigDecimal> calcIntersectionPoints(TCMap a) {
        if (a == null) {
            return null;
        }
        TreeSet<BigDecimal> intersects = new TreeSet<>();
        TreeSet<Quadruple> aSet=a.getTCMap();

        for (Quadruple qa : aSet) {
            if (qa.getC1() != null) {
                for (Quadruple qb : aSet) {
                    if (qb.getC1() != null) {
                        if (qa.getD1().doubleValue()<=qb.getD1().doubleValue() && qb.getD1().doubleValue()< qa.getD2().doubleValue()) {
                            if (qa.getGradient().doubleValue() != qb.getGradient().doubleValue()) {
                                BigDecimal intersectingPoint= (qb.getC1().subtract(qa.getC1())
                                        .add(
                                                (qa.getGradient().multiply(qa.getD1()))
                                                        .subtract((qb.getGradient().multiply(qb.getD1()))))
                                )

                                        .divide((qa.getGradient().subtract(qb.getGradient())), 10, RoundingMode.HALF_UP);
                                if (qa.getD1().max(qb.getD1()).doubleValue()<= intersectingPoint.doubleValue() && intersectingPoint.doubleValue()<= qa.getD2().min(qb.getD2()).doubleValue())
                                intersects.add(intersectingPoint);
                            }
                        }
                    }
                }
            }
        }
        return intersects;
    }


    public TCMap compress(TCMap map) {
        if (map == null) {
            return null;
        }
        TreeSet<BigDecimal> set=new TreeSet<>(map.getPointSet());
        TreeSet<BigDecimal> intersects=(TreeSet<BigDecimal>) calcIntersectionPoints(map);
        for (BigDecimal d: intersects
             ) {
            set.add(d);
        }

        TreeSet<Tuple> intervals = new TreeSet<>(getIntervalSet(set));
        TCMap compressed = new TCMap();

        BigDecimal c1;
        BigDecimal c2;

        for (Tuple t : intervals) {
            c1=selectLowerCost(t.getLow(),map);
            c2=selectUpperCost(t.getUp(),map);
            if (c1==null || c2 ==null){
                c1=null;
                c2=null;
            }
            compressed.add(new Quadruple(t.getLow(), c1, t.getUp(),c2 ));
        }
        return compressed;
    }

}
