import org.xml.sax.SAXException;

import java.io.File;
import java.io.FilenameFilter;

public class CalculateOverallTCMap {
    public static void main(String[] args) {
        StringBuilder pathData = new StringBuilder("src/main/resources/Input");
        StringBuilder outputPath = new StringBuilder("src/main/resources/Output");

        File file = new File(pathData.toString());
        String[] files = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        });

        String pathToInputs = pathData.toString();
        String pathToOutputs = outputPath.toString();
        String sheetName = "OverallTCMap";
        String ending = ".xlsx";

        for (int i = 0; i < files.length; i++) {
            XMLImporter importer = null;
            try {
                importer = new XMLImporter(new File("src/main/resources/TCP.xsd"), new File(pathData.append("/").append(files[i]).toString()));
                importer.importXMLData();
            } catch (SAXException e) {
                e.printStackTrace();
                System.out.println("Fail!");
            }

            beans.Node root = importer.getTree();
            String out= outputPath.append("/").append(sheetName).append(files[i]).append(ending).toString();
            TCCalculator.getInstance().calcTree(root).writeToNewExcelFile(out,sheetName);

            pathData = new StringBuilder(pathToInputs);
            outputPath = new StringBuilder(pathToOutputs);
            System.out.println(files[i]+ " completed.");
        }
    }
}
