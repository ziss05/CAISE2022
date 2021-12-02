import beans.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class TCCalculatorTest {
    TCCalculator calc;
    TCMap a;
    TCMap b;
    TCMap c;
    TCMap d;
    TCMap e;
    TCMap f;
    TCMap g;
    TCMap h;
    TCMap i;
    TCMap j;
    TCMap exp;
    TCMap res;

    @BeforeEach
    void setUp() {
        calc = TCCalculator.getInstance();
        a = new TCMap();
        a.add(new Quadruple(1, 50, 3, 50));
        a.add(new Quadruple(3, 10, 10, 10));
        b = new TCMap();
        b.add(new Quadruple(2, 30, 4, 30));
        b.add(new Quadruple(4, 5, 6, 5));
        c = new TCMap();
        c.add(new Quadruple(1, 10, 3, 10));
        c.add(new Quadruple(3, 50, 10, 50));
        d = new TCMap();
        d.add(new Quadruple(2, 5, 4, 5));
        d.add(new Quadruple(4, 30, 6, 30));
        e = new TCMap();
        e.add(new Quadruple(1, 10, 3, 10));
        e.add(new Quadruple(3, 10, 10, 10));
        f = new TCMap();
        f.add(new Quadruple(2, 20, 4, 20));
        f.add(new Quadruple(4, 20, 6, 20));
        g = new TCMap();
        g.add(new Quadruple(1, 5, 3, 5));
        g.add(new Quadruple(3, 6, 10, 6));
        g.add(new Quadruple(10, 7, 12, 7));
        h = new TCMap();
        h.add(new Quadruple(2, 10, 4, 10));
        h.add(new Quadruple(4, 4, 6, 4));
        h.add(new Quadruple(6, 6, 9, 6));
        i = new TCMap();
        i.add(new Quadruple(1, 50, 11, 550));
        i.add(new Quadruple(11, 540, 20, 900));
        j = new TCMap();
        j.add(new Quadruple(1, 150, 4, 200));
        j.add(new Quadruple(4, 220, 6, 260));
        exp = new TCMap();
        res = new TCMap();
    }

    @AfterEach
    void tearDown() {
        calc = null;
        a = null;
        b = null;
        c = null;
        d = null;
        e = null;
        f = null;
        g = null;
        h = null;
        i = null;
        j = null;
        exp = null;
    }

    @Test
    void addCostValues() {
        BigDecimal dec = calc.addCostValues(new BigDecimal(1), new BigDecimal(2));
        assertEquals(new BigDecimal(3), dec);
    }

    @Test
    void addCostValuesNULLFirst() {
        BigDecimal dec = calc.addCostValues(null, new BigDecimal(2));
        assertNull(dec);
    }

    @Test
    void addCostValuesInklNULLSecond() {
        BigDecimal dec = calc.addCostValues(new BigDecimal(2), null);
        assertNull(dec);
    }

    @Test
    void testGetMin() {
        BigDecimal dec = calc.getMin(new BigDecimal(1), new BigDecimal(2));
        assertEquals(new BigDecimal(1), dec);
    }

    @Test
    void testGetMinInklNULL() {
        BigDecimal dec = calc.getMin(new BigDecimal(1), null);
        assertEquals(new BigDecimal(1), dec);
    }


    @Test
    void testGetMinSet() {
        HashSet<BigDecimal> set = new HashSet<>();
        set.add(new BigDecimal(1));
        set.add(new BigDecimal(3));
        set.add(null);

        assertEquals(new BigDecimal(1), calc.getMin(set));
    }

    @Test
    void testGetMax() {
        BigDecimal dec = calc.getMax(new BigDecimal(1), new BigDecimal(2));
        assertEquals(new BigDecimal(2), dec);
    }

    @Test
    void testGetMaxInklNULL() {
        BigDecimal dec = calc.getMax(new BigDecimal(1), null);
        assertNull(dec);
    }


    @Test
    void testGetMaxSet() {
        HashSet<BigDecimal> set = new HashSet<>();
        set.add(new BigDecimal(1));
        set.add(new BigDecimal(3));
        set.add(null);

        assertNull(calc.getMax(set));
    }

    @Test
    void testGetIntervalSet() {
        TreeSet<Tuple> set = new TreeSet<>();
        set.add(new Tuple(1, 2));
        set.add(new Tuple(2, 3));
        set.add(new Tuple(3, 4));

        TreeSet<BigDecimal> points = new TreeSet<>();
        points.add(new BigDecimal(1));
        points.add(new BigDecimal(2));
        points.add(new BigDecimal(3));
        points.add(new BigDecimal(4));

        assertEquals(set, calc.getIntervalSet(points));
    }

    @Test
    void testCalcTreeSingleService() {
        Node n = new BasicService(a);
        assertEquals(a, calc.calcTree(n));
    }

    @Test
    void testCalcTreeSimple() {
        Node n = new Constructor(Type.SEQ, new BasicService(a), new BasicService(b));
        exp.add(new Quadruple(3, 80, 5, 80));
        exp.add(new Quadruple(5, 40, 7, 40));
        exp.add(new Quadruple(7, 15, 9, 15));
        exp.add(new Quadruple(9, 15, 14, 15));
        exp.add(new Quadruple(14, 15, 16, 15));
        res = calc.calcTree(n);
        assertEquals(exp, res);
    }


    @Test
    void testCalcSEQID() {
        exp.add(new Quadruple(3, 80, 5, 80));
        exp.add(new Quadruple(5, 40, 7, 40));
        exp.add(new Quadruple(7, 15, 9, 15));
        exp.add(new Quadruple(9, 15, 14, 15));
        exp.add(new Quadruple(14, 15, 16, 15));
        res = calc.calcSEQ(a, b);
        assertEquals(exp, res);
    }

    @Test
    void testCalcSEQD() {
        exp.add(new Quadruple(3, 15, 5, 15));
        exp.add(new Quadruple(5, 15, 7, 15));
        exp.add(new Quadruple(7, 40, 9, 40));
        exp.add(new Quadruple(9, 55, 14, 55));
        exp.add(new Quadruple(14, 80, 16, 80));
        res = calc.calcSEQ(c, d);
        assertEquals(exp, res);
    }

    @Test
    void testCalcSEQC() {
        exp.add(new Quadruple(3, 30, 5, 30));
        exp.add(new Quadruple(5, 30, 7, 30));
        exp.add(new Quadruple(7, 30, 9, 30));
        exp.add(new Quadruple(9, 30, 14, 30));
        exp.add(new Quadruple(14, 30, 16, 30));
        res = calc.calcSEQ(e, f);
        assertEquals(exp, res);
    }

    @Test
    void testCalcSEQM() {
        exp.add(new Quadruple(3, 15, 5, 15));
        exp.add(new Quadruple(5, 9,7, 9));
        exp.add(new Quadruple(7,9, 9, 9));
        exp.add(new Quadruple(9,10, 10, 10));
        exp.add(new Quadruple(10,10, 12, 10));
        exp.add(new Quadruple(12, 10, 14, 10));
        exp.add(new Quadruple(14, 10, 16, 10));
        exp.add(new Quadruple(16, 11, 18, 11));
        exp.add(new Quadruple(18, 12, 19, 12));
        exp.add(new Quadruple(19, 13, 21, 13));
        res = calc.calcSEQ(g, h);
        assertEquals(exp, res);
    }


    @Test
    void testCalcEXTID() {
        exp.add(new Quadruple(1, 50, 3, 50));
        exp.add(new Quadruple(3, 10, 4, 10));
        exp.add(new Quadruple(4, 10, 6, 10));
        exp.add(new Quadruple(6, 10, 13, 10));
        res = calc.calcEXT(a, new BigDecimal(3));

        assertEquals(exp, res);
    }

    @Test
    void testCalcEXTD() {
        exp.add(new Quadruple(1, 10, 3, 10));
        exp.add(new Quadruple(3, 10, 4, 10));
        exp.add(new Quadruple(4, 10, 6, 10));
        exp.add(new Quadruple(6, 50, 13, 50));
        res=calc.calcEXT(c, new BigDecimal(3));
        assertEquals(exp,res);
    }

    @Test
    void testCalcEXTC() {
        exp.add(new Quadruple(1, 10, 3, 10));
        exp.add(new Quadruple(3, 10, 4, 10));
        exp.add(new Quadruple(4, 10, 6, 10));
        exp.add(new Quadruple(6, 10, 13, 10));
        res=calc.calcEXT(e,new BigDecimal(3));
        assertEquals(exp,res);
    }

    @Test
    void testCalcEXTM() {
        g=new TCMap();
        g.add(new Quadruple(1, 10, 3, 10));
        g.add(new Quadruple(3, 9, 4, 9));
        g.add(new Quadruple(4, 11, 6, 11));
        g.add(new Quadruple(6, 7, 13, 7));
        exp.add(new Quadruple(1, 10, 3, 10));
        exp.add(new Quadruple(3, 9, 4, 9));
        exp.add(new Quadruple(4, 9, 6, 9));
        exp.add(new Quadruple(6, 7, 7, 7));
        exp.add(new Quadruple(7, 7, 9, 7));
        exp.add(new Quadruple(9, 7, 16, 7));
        res=calc.calcEXT(g,new BigDecimal(3));
        assertEquals(exp,res);
    }


    @Test
    void testCalcANDID() { //TODO
        exp.add(new Quadruple(new BigDecimal(1), null, new BigDecimal(2), null));
        exp.add(new Quadruple(2, 80, 3, 80));
        exp.add(new Quadruple(3, 40, 4, 40));
        exp.add(new Quadruple(4, 15, 11, 15));
        exp.add(new Quadruple(11, 15, 12, 15));
        exp.add(new Quadruple(12, 15, 13, 15));
        exp.add(new Quadruple(13, 15, 14, 15));
        exp.add(new Quadruple(14, 15, 16, 15));
        exp.add(new Quadruple(new BigDecimal(16), null, new BigDecimal(20), null));
        res = calc.calcAND(a, b);
        assertEquals(exp, res);
    }

    @Test
    void testCalcANDD() {
        exp.add(new Quadruple(new BigDecimal(1), null, new BigDecimal(2), null));
        exp.add(new Quadruple(2, 15, 3, 15));
        exp.add(new Quadruple(3, 15, 4, 15));
        exp.add(new Quadruple(4, 15, 11, 15));
        exp.add(new Quadruple(11, 15, 12, 15));
        exp.add(new Quadruple(12, 15, 13, 15));
        exp.add(new Quadruple(13, 55, 14, 55));
        exp.add(new Quadruple(14, 80, 16, 80));
        exp.add(new Quadruple(new BigDecimal(16), null, new BigDecimal(20), null));
        res=calc.calcAND(c,d);
        assertEquals(exp,res);
    }

    @Test
    void testCalcANDC() {
        exp.add(new Quadruple(new BigDecimal(1), null, new BigDecimal(2), null));
        exp.add(new Quadruple(2, 30, 3, 30));
        exp.add(new Quadruple(3, 30, 4, 30));
        exp.add(new Quadruple(4, 30, 11, 30));
        exp.add(new Quadruple(11, 30, 12, 30));
        exp.add(new Quadruple(12, 30, 13, 30));
        exp.add(new Quadruple(13, 30, 14, 30));
        exp.add(new Quadruple(14, 30, 16, 30));
        exp.add(new Quadruple(new BigDecimal(16), null, new BigDecimal(20), null));
        res=calc.calcAND(e,f);
        assertEquals(exp,res);
    }

    @Test
    void testCalcANDM() {
        exp.add(new Quadruple(new BigDecimal(1), null, new BigDecimal(2), null));
        exp.add(new Quadruple(2, 15, 3, 15));
        exp.add(new Quadruple(3, 15, 4, 15));
        exp.add(new Quadruple(4, 9, 6, 9));
        exp.add(new Quadruple(6, 9, 10, 9));
        exp.add(new Quadruple(10, 9, 13, 9));
        exp.add(new Quadruple(13, 9, 14, 9));
        exp.add(new Quadruple(14, 9, 15, 9));
        exp.add(new Quadruple(15, 10, 16, 10));
        exp.add(new Quadruple(16, 10, 18, 10));
        exp.add(new Quadruple(18, 12, 21, 12));
        exp.add(new Quadruple(new BigDecimal(21), null, new BigDecimal(22), null));
        exp.add(new Quadruple(new BigDecimal(22), null, new BigDecimal(24), null));
        res=calc.calcAND(g,h);
        assertEquals(exp,res);
    }


    @Test
    void testCalcXORDID() {
        exp.add(new Quadruple(1, 50, 2, 50));
        exp.add(new Quadruple(2, 30, 3, 30));
        exp.add(new Quadruple(3, 10, 4, 10));
        exp.add(new Quadruple(4, 5, 6, 5));
        exp.add(new Quadruple(6, 10, 10, 10));
        res = calc.calcXOR(a, b, Type.XORD);
        assertEquals(exp, res);
    }

    @Test
    void testCalcXORDD() {
        exp.add(new Quadruple(1, 10, 2, 10));
        exp.add(new Quadruple(2, 5, 3, 5));
        exp.add(new Quadruple(3, 5, 4, 5));
        exp.add(new Quadruple(4, 30, 6, 30));
        exp.add(new Quadruple(6, 50, 10, 50));
        res=calc.calcXOR(c,d,Type.XORD);
        assertEquals(exp,res);
    }

    @Test
    void testCalcXORDC() {
        exp.add(new Quadruple(1, 10, 2, 10));
        exp.add(new Quadruple(2, 10, 3, 10));
        exp.add(new Quadruple(3, 10, 4, 10));
        exp.add(new Quadruple(4, 10, 6, 10));
        exp.add(new Quadruple(6, 10, 10, 10));
        res=calc.calcXOR(e,f,Type.XORD);
        assertEquals(exp,res);
    }

    @Test
    void testCalcXORDM() {
        exp.add(new Quadruple(1, 5, 2, 5));
        exp.add(new Quadruple(2, 5, 3, 5));
        exp.add(new Quadruple(3, 6, 4, 6));
        exp.add(new Quadruple(4, 4, 6, 4));
        exp.add(new Quadruple(6, 6, 9, 6));
        exp.add(new Quadruple(9, 6, 10, 6));
        exp.add(new Quadruple(10, 7, 12, 7));
        res=calc.calcXOR(g,h,Type.XORD);
        assertEquals(exp,res);
    }


    @Test
    void testCalcXORCID() {
        exp.add(new Quadruple(new BigDecimal(1), null, new BigDecimal(2), null));
        exp.add(new Quadruple(new BigDecimal(2), new BigDecimal(50), new BigDecimal(3), new BigDecimal(50)));
        exp.add(new Quadruple(new BigDecimal(3), new BigDecimal(30), new BigDecimal(4), new BigDecimal(30)));
        exp.add(new Quadruple(new BigDecimal(4), new BigDecimal(10), new BigDecimal(6), new BigDecimal(10)));
        exp.add(new Quadruple(new BigDecimal(6), null, new BigDecimal(10), null));
        res = calc.calcXOR(a, b, Type.XORC);
        assertEquals(exp, res);
    }

    @Test
    void testCalcXORCD() {
        exp.add(new Quadruple(new BigDecimal(1), null, new BigDecimal(2), null));
        exp.add(new Quadruple(2, 10, 3, 10));
        exp.add(new Quadruple(3, 50, 4, 50));
        exp.add(new Quadruple(4, 50, 6, 50));
        exp.add(new Quadruple(new BigDecimal(6), null, new BigDecimal(10), null));

        res=calc.calcXOR(c,d,Type.XORC);
        assertEquals(exp,res);
    }

    @Test
    void testCalcXORCC() {
        exp.add(new Quadruple(new BigDecimal(1), null, new BigDecimal(2), null));
        exp.add(new Quadruple(2, 20, 3, 20));
        exp.add(new Quadruple(3, 20, 4, 20));
        exp.add(new Quadruple(4, 20, 6, 20));
        exp.add(new Quadruple(new BigDecimal(6), null, new BigDecimal(10), null));
        res=calc.calcXOR(e,f,Type.XORC);
        assertEquals(exp,res);
    }

    @Test
    void testCalcXORCM() {
        exp.add(new Quadruple(new BigDecimal(1), null, new BigDecimal(2), null));
        exp.add(new Quadruple(2, 10, 3, 10));
        exp.add(new Quadruple(3, 10, 4, 10));
        exp.add(new Quadruple(4, 6, 6, 6));
        exp.add(new Quadruple(6, 6, 9, 6));
        exp.add(new Quadruple(new BigDecimal(9), null, new BigDecimal(10), null));
        exp.add(new Quadruple(new BigDecimal(10), null, new BigDecimal(12), null));
        res=calc.calcXOR(g,h,Type.XORC);
        assertEquals(exp,res);
    }

    @Test
    void testCalcIntersectionPoints() {
        TreeSet<BigDecimal> set=new TreeSet<>();
        set.add(new BigDecimal(4));
        set.add(new BigDecimal(4.6666666667).setScale(10, RoundingMode.HALF_UP));
        i.addAll(j.getTCMap());
        TreeSet<BigDecimal> res = new TreeSet<>(calc.calcIntersectionPoints(i));
        boolean check=true;
        for (BigDecimal d:res) {
            check=set.contains(d);
        }
        assertTrue(check);
    }

    @Test
    void testCompress() {
        TCMap map= new TCMap();
        map.add(new Quadruple(3,80,5,80));
        map.add(new Quadruple(5,80,7,80));
        map.add(new Quadruple(5,55,7,55));
        map.add(new Quadruple(7,55,9,55));
        map.add(new Quadruple(5,40,7,40));
        map.add(new Quadruple(7,40,14,40));
        map.add(new Quadruple(7,15,9,15));
        map.add(new Quadruple(9,15,16,15));

        exp.add(new Quadruple(3, 80, 5, 80));
        exp.add(new Quadruple(5, 40, 7, 40));
        exp.add(new Quadruple(7, 15, 9, 15));
        exp.add(new Quadruple(9, 15, 14, 15));
        exp.add(new Quadruple(14, 15, 16, 15));

        TCMap res=calc.compress(map);
        assertEquals(exp,res);
    }

}