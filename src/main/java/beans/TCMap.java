package beans;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class TCMap {
    private TreeSet<Quadruple> tcMap;

    public TCMap() {
        this.tcMap = new TreeSet<>();
    }

    public TCMap(TCMap tcMap) {
        this.tcMap = new TreeSet<>(tcMap.getTCMap());
    }


    public TreeSet<Quadruple> getTCMap() {
        return this.tcMap;
    }

    public boolean add(Quadruple quadruple) {
        return tcMap.add(quadruple);
    }

    public boolean addAll(Set<Quadruple> quadruples) {
        return tcMap.addAll(quadruples);
    }

    public BigDecimal getMin() {
        return this.tcMap.first().getD1();
    }

    public BigDecimal getMax() {
        return this.tcMap.last().getD2();
    }

    public Quadruple getFirst() {
        return this.tcMap.first();
    }

    public Quadruple getLast() {
        return this.tcMap.last();
    }

    public Quadruple getT(double d) {
        for (Quadruple t : this.tcMap
        ) {
            if (t.getD1().doubleValue() <= d && d < t.getD2().doubleValue()) {
                return t;
            }
        }
        return null;
    }

    public BigDecimal[] getRange() {
        BigDecimal[] range = new BigDecimal[2];
        range[0] = getMin();
        range[1] = getMax();
        return range;
    }

    public boolean isFunctional() {
        ArrayList<Quadruple> list = new ArrayList<>(this.getTCMap());
        int counter = 1;

        Iterator<Quadruple> iterator = this.tcMap.iterator();
        while (iterator.hasNext()) {
            if (counter == list.size()) {
                break;
            }
            Quadruple listq = list.get(counter);
            Quadruple tcmapq = iterator.next();
            if (listq.getD1().doubleValue() <= tcmapq.getD1().doubleValue() && tcmapq.getD1().doubleValue() < listq.getD2().doubleValue()) {
                return false;
            }
            counter++;
        }
        return true;
    }

    public Set<BigDecimal> getPointSet() {
        HashSet<BigDecimal> points = new HashSet<>();

        for (Quadruple q : this.tcMap) {
            points.add(q.getD1());
            points.add(q.getD2());
        }

        return points;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("  TimeCostMap\n---------------\n");
        for (Quadruple q : this.tcMap) {
            sb.append(q.toString());
            sb.append("\n----------------------------------\n");
        }

        return sb.toString();
    }

    public int size() {
        return this.tcMap.size();
    }

    public boolean isEmpty() {
        return this.tcMap.isEmpty();
    }

    public boolean containsQuadruple(Quadruple quadruple) {
        return this.tcMap.contains(quadruple);
    }

    public Quadruple removeFirst() {
        return this.tcMap.pollFirst();
    }

    public Quadruple removeLast() {
        return this.tcMap.pollLast();
    }

    public void clear() {
        this.tcMap.clear();
    }

    public TCMap clone() {
        return (TCMap) this.tcMap.clone();
    }


    public void writeToNewExcelFile(String pathToFile, String sheetname) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetname);
        Object[][] field = new Object[this.tcMap.size()][4];
        int i = 0;
        for (Quadruple quadruple : this.tcMap) {
            field[i][0] = quadruple.getD1();
            field[i][1] = quadruple.getC1();
            field[i][2] = quadruple.getD2();
            field[i][3] = quadruple.getC2();
            i++;
        }
        int rowNum = 0;
        for (Object[] objects : field) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object o : objects) {
                Cell cell = row.createCell(colNum++);
                if (o == null) {
                    cell.setCellValue("null");
                } else if (o instanceof Double) {
                    cell.setCellValue((Double) o);
                } else {
                    cell.setCellValue(o.toString());
                }
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(pathToFile);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean normalize() {
        if (!this.isFunctional()) {
            return false;
        }

        TCMap simple = new TCMap();
        Quadruple current = this.tcMap.first();
        Quadruple next = null;
        boolean first = true;
        boolean wasCurAdded = true;
        Iterator iterator = this.tcMap.iterator();

        while (iterator.hasNext()) {
            if (first) {
                current = (Quadruple) iterator.next();
                first = false;
            }
            next = (Quadruple) iterator.next();
            if (current.getC1() != null) {
                if (current.joinable(next)) {
                    current = new Quadruple(current.getD1(), current.getC1(), next.getD2(), next.getC2());
                    wasCurAdded = false;
                } else {
                    wasCurAdded = simple.add(current);
                    current = next;
                }
            } else current = next;
        }

        if (!wasCurAdded) {
            wasCurAdded = simple.add(current);
            current = next;
        } else if (next != null) {
            simple.add(next);
        }
        this.tcMap = simple.tcMap;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TCMap that = (TCMap) o;
        return this.tcMap.equals(that.tcMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcMap);
    }
}
