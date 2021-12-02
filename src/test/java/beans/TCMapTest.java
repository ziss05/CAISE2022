package beans;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class TCMapTest {
    TCMap map = new TCMap();

    @BeforeEach
    public void setUp() throws Exception {
        map.add(new Quadruple(3, 80, 5, 80));
        map.add(new Quadruple(5, 40, 7, 40));
        map.add(new Quadruple(7, 15, 9, 15));
        map.add(new Quadruple(9, 15, 14, 15));
        map.add(new Quadruple(14, 15, 16, 15));
    }

    @AfterEach
    public void tearDown() throws Exception {
        map =null;
    }

    @Test
    public void normalize() {
        TCMap exp = new TCMap();
        exp.add(new Quadruple(3, 80, 5, 80));
        exp.add(new Quadruple(5, 40, 7, 40));
        exp.add(new Quadruple(7, 15, 16, 15));
        map.normalize();
        assertEquals(exp,map);
    }

    @Test
    public void getRange() {
        BigDecimal[] range=map.getRange();
        BigDecimal[] exp={BigDecimal.valueOf(3), BigDecimal.valueOf(16)};

        assertArrayEquals(exp,range);
    }

    @Test
    public void isFunctional() {
        assertTrue(map.isFunctional());
    }

    @Test
    public void isNonFunctional() {
        map.add(new Quadruple(9,10,15,10));
        assertFalse(map.isFunctional());
    }

    @Test
    public void getPointSet() {
        HashSet<BigDecimal> set=new HashSet<>();
        set.add(BigDecimal.valueOf(3));
        set.add(BigDecimal.valueOf(5));
        set.add(BigDecimal.valueOf(7));
        set.add(BigDecimal.valueOf(9));
        set.add(BigDecimal.valueOf(14));
        set.add(BigDecimal.valueOf(16));

        assertEquals(set,map.getPointSet());
    }

}