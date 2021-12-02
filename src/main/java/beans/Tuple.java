package beans;

import java.math.BigDecimal;
import java.util.Objects;

public class Tuple implements Comparable {
    private BigDecimal d1;
    private BigDecimal d2;

    public Tuple(BigDecimal d1, BigDecimal d2) {
        if (d1 == null || d2 == null) {
            throw new IllegalArgumentException("Durations need to be a positive decimal, not null!", new Throwable());
        } else if (d1.doubleValue() <= 0 || d2.doubleValue() <= 0) {
            throw new IllegalArgumentException("Durations need to be greater than 0!", new Throwable());
        } else if (d1.doubleValue() >= d2.doubleValue()) {
            throw new IllegalArgumentException("Second duration argument needs to be greater than the first one!", new Throwable());
        } else {
            this.d1 = d1;
            this.d2 = d2;

        }
    }

    public Tuple(int d1, int d2) {
        if (d1 <= 0 && d2 <= 0) {
            throw new IllegalArgumentException("Durations need to be greater than 0!", new Throwable());
        } else if (d1 >= d2) {
            throw new IllegalArgumentException("Second argument needs to be greater than the first one!", new Throwable());
        } else {
            this.d1 = new BigDecimal(d1);
            this.d2 = new BigDecimal(d2);
        }
    }

    public Tuple(Tuple tuple) {
        this.d1 = tuple.getLow();
        this.d2 = tuple.getUp();
    }

    public BigDecimal getLow() {
        return this.d1;
    }

    public BigDecimal getUp() {
        return this.d2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return d1.equals(tuple.d1) &&
                d2.equals(tuple.d2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(d1, d2);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "d1=" + d1.toString() +
                ", d2=" + d2.toString() +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        Tuple t = (Tuple) o;
        if (this.d1.compareTo(t.d1)==0 && this.d2.compareTo(t.d2)==0) {
            return EQUAL;
        } else if (this.d1.doubleValue() <= t.d1.doubleValue()) {
            return BEFORE;
        } else if (this.d1.doubleValue() > t.d1.doubleValue()) {
            return AFTER;
        }
        return EQUAL;
    }
}
