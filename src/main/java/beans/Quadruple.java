package beans;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Quadruple implements Comparable {
    private BigDecimal d1;
    private BigDecimal c1;
    private BigDecimal d2;
    private BigDecimal c2;
    private BigDecimal gradient = null;

    public Quadruple(BigDecimal d1, BigDecimal c1, BigDecimal d2, BigDecimal c2) {
        if (d1 == null || d2 == null) {
            throw new IllegalArgumentException("Durations need to be a positive decimal, not null!", new Throwable());
        } else if (d1.doubleValue() < 0 || d2.doubleValue() < 0) {
            throw new IllegalArgumentException("Durations need to be greater than or equal to 0!", new Throwable());
        } else if (c1 != null && c1.doubleValue() < 0) {
            throw new IllegalArgumentException("Cost value c1 needs to be greater than or equal to 0!", new Throwable());
        } else if (c2 != null && c2.doubleValue() < 0) {
            throw new IllegalArgumentException("Cost value c2 needs to be greater than or equal to 0!", new Throwable());
        } else if ((c1 == null && c2 != null) || (c1 != null && c2 == null)) {
            throw new IllegalArgumentException("If one cost value is null, the other needs to be null as well!", new Throwable());
        } else if (d1.doubleValue() >= d2.doubleValue()) {
            throw new IllegalArgumentException("Second duration argument needs to be greater than the first one!", new Throwable());
        } else {
            this.d1 = d1;
            this.c1 = c1;
            this.d2 = d2;
            this.c2 = c2;
            if (c2 != null) {
                this.gradient = c2.subtract(c1).divide(d2.subtract(d1), 10, RoundingMode.HALF_UP);
            }
        }
    }

    public Quadruple(int d1, int c1, int d2, int c2) {
        if (d1 < 0 && d2 < 0) {
            throw new IllegalArgumentException("Durations need to be greater than 0!", new Throwable());
        } else if (c1 <= 0 && c2 <= 0) {
            throw new IllegalArgumentException("Cost values need to be greater than or equal to 0!", new Throwable());
        } else if (d1 >= d2) {
            throw new IllegalArgumentException("Second argument needs to be greater than the first one!", new Throwable());
        } else {
            this.d1 = new BigDecimal(d1);
            this.c1 = new BigDecimal(c1);
            this.d2 = new BigDecimal(d2);
            this.c2 = new BigDecimal(c2);
            if (this.c2 != null) {
                this.gradient = this.c2.subtract(this.c1).divide(this.d2.subtract(this.d1), 10, RoundingMode.HALF_UP);
            }
        }
    }

    public Quadruple(Quadruple quadruple) {
        this.d1 = quadruple.getD1();
        this.d2 = quadruple.getD2();
        this.c1 = quadruple.getC1();
        this.c2 = quadruple.getC2();
        this.gradient = quadruple.getGradient();
    }

    public BigDecimal getD1() {
        return this.d1;
    }

    public BigDecimal getD2() {
        return this.d2;
    }

    public BigDecimal getC1() {
        return this.c1;
    }

    public BigDecimal getC2() {
        return this.c2;
    }

    public BigDecimal getGradient() {
        return this.gradient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quadruple quadruple = (Quadruple) o;
        return d1.equals(quadruple.d1) && d2.equals(quadruple.d2) && c1.equals(quadruple.c1) && c2.equals(quadruple.c2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(d1, c1, d2, c2);
    }

    @Override
    public String toString() {
        if (c1 == null || c2 == null) {
            return "Quadruple{" +
                    "d1=" + d1.toString() +
                    ", c1= null" +
                    ", d2=" + d2.toString() +
                    ", c2= null" +
                    '}';
        }
        return "Quadruple{" +
                "d1=" + d1.toString() +
                ", c1=" + c1.toString() +
                ", d2=" + d2.toString() +
                ", c2=" + c2.toString() +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        Quadruple q = (Quadruple) o;
        if (c1 == null || q.c1 == null) {
            if (this.d1.compareTo(q.d1) == 0 && this.d2.compareTo(q.d2) == 0 && c1 == q.c1 && c2 == q.c2) {
                return EQUAL;
            }
        } else if (this.d1.compareTo(q.d1) == 0 && this.d2.compareTo(q.d2) == 0 && this.c1.compareTo(q.c1) == 0 && this.c2.compareTo(q.c2) == 0) {
            return EQUAL;
        }
        if (this.d1.doubleValue() < q.d1.doubleValue()) {
            return BEFORE;
        } else if (this.d1.doubleValue() >= q.d1.doubleValue()) {
            return AFTER;
        }
        return EQUAL;
    }

    public boolean joinable(Quadruple q) {
        if (q.getD1() == null) {
            return false;
        }

        if (d1.doubleValue() < q.getD1().doubleValue() && d2.equals(q.getD1()) && c2.equals(q.getC1())) {
            if (this.gradient == q.getGradient()) {
                return true;
            }
        } else if (d1.doubleValue() > q.getD1().doubleValue() && q.getD2().equals(d1) && q.getC2().equals(c1)) {
            if (this.gradient == q.getGradient()) {
                return true;
            }
        }
        return false;
    }
}
