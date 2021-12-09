import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.SAXException;

import java.io.*;

public class ExperimentsMain {
    static final int NUMTESTRUNS=100;
    public static void main(String[] args) {
        StringBuilder pathData = new StringBuilder("src/main/resources/Examples");

        File file = new File(pathData.toString());
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        String pathToExampleInputs = pathData.toString();

        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "SpeedTestResults";
        Object[][] neededFields = null;
        XSSFSheet sheet = null;

        String[] subDirectories = null;

        for (int i = 0; i < directories.length; i++) {
            File examples = new File(pathData.append("/" + directories[i]).toString());
            subDirectories = examples.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isFile();
                }
            });

            pathData = new StringBuilder(pathToExampleInputs);
        }

        for (int i = 0; i < directories.length; i++) {
            sheet = workbook.createSheet(sheetName + directories[i]);
            String pathToExampleInputsClasses = pathData.append("/" + directories[i]).toString();
            neededFields = new Object[subDirectories.length][NUMTESTRUNS+ 1];

            for (int j = 0; j < subDirectories.length; j++) {
                neededFields[j][0] = subDirectories[j];
                XMLImporter importer = null;
                try {
                    importer = new XMLImporter(new File("src/main/resources/TCP.xsd"), new File(pathData.append("/" + subDirectories[j]).toString()));
                    importer.importXMLData();
                } catch (SAXException e) {
                    e.printStackTrace();
                    System.out.println("Fail!");
                }

                beans.Node root = importer.getTree();


                long elapsedTime = 0;

                for (int k = 0; k < NUMTESTRUNS; k++) {
                    long startTime = System.currentTimeMillis();
                    TCCalculator.getInstance().calcTree(root);
                    long stopTime = System.currentTimeMillis();
                    elapsedTime = stopTime - startTime;
                    neededFields[j][k + 1] = elapsedTime;
                }


                System.out.println(subDirectories[j] + " completed.");
                pathData = new StringBuilder(pathToExampleInputsClasses);
            }
            pathData = new StringBuilder(pathToExampleInputs);

            int rowNum = 0;
            if (neededFields != null) {
                for (Object[] objects : neededFields) {
                    Row row = sheet.createRow(rowNum++);
                    int colNum = 0;
                    for (Object o : objects) {
                        Cell cell = row.createCell(colNum++);
                        if (o == null) {
                            cell.setCellValue("null");
                        } else if (o instanceof Long) {
                            cell.setCellValue((Long) o);
                        } else {
                            cell.setCellValue(o.toString());
                        }
                    }
                }
            }
            System.out.println(directories[i] + " completed.");
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(file + "/SpeedTestResults.xlsx");
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

