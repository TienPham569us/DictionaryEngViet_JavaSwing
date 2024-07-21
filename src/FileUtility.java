import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUtility {
    public static ArrayList<Record> loadXMLFileIntoArrayList(String filePath) {

        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // Parse the XML file
            File xmlFile = new File(filePath);
            Document document = documentBuilder.parse(xmlFile);

            // Get the root element
            Element rootElement = document.getDocumentElement();

            // Get all the 'record' elements
            NodeList recordList = rootElement.getElementsByTagName("record");

            ArrayList<Record> arrayListRecord = new ArrayList<>();
            // Iterate over the 'record' elements
            for (int i = 0; i < recordList.getLength(); i++) {
                Element recordElement = (Element) recordList.item(i);

                // Get the 'word' element
                Element wordElement = (Element) recordElement.getElementsByTagName("word").item(0);
                String word = wordElement.getTextContent();

                // Get the 'meaning' element
                Element meaningElement = (Element) recordElement.getElementsByTagName("meaning").item(0);
                String meaning = meaningElement.getTextContent();

                Record record = new Record(word, meaning);
                arrayListRecord.add(record);
            }

            return arrayListRecord;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static TreeMap<String,String> loadXMLFile(String filePath) {

        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // Parse the XML file
            File xmlFile = new File(filePath);
            Document document = documentBuilder.parse(xmlFile);

            // Get the root element
            Element rootElement = document.getDocumentElement();

            // Get all the 'record' elements
            NodeList recordList = rootElement.getElementsByTagName("record");

            TreeMap<String,String> treeMapRecord = new TreeMap<>();
            // Iterate over the 'record' elements
            for (int i = 0; i < recordList.getLength(); i++) {
                Element recordElement = (Element) recordList.item(i);

                // Get the 'word' element
                Element wordElement = (Element) recordElement.getElementsByTagName("word").item(0);
                String word = wordElement.getTextContent();

                // Get the 'meaning' element
                Element meaningElement = (Element) recordElement.getElementsByTagName("meaning").item(0);
                String meaning = meaningElement.getTextContent();

                treeMapRecord.put(word, meaning);
            }

            return treeMapRecord;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void saveXMLFileFromArrayList(String filePath, ArrayList<Record> arrayListRecord) {
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Create a new Document
            Document document = builder.newDocument();

            // Create the root element
            Element rootElement = document.createElement("dictionary");
            document.appendChild(rootElement);

            // Iterate over the record objects and create the XML structure
            for (Record record : arrayListRecord) {
                // Create the record element
                Element recordElement = document.createElement("record");
                rootElement.appendChild(recordElement);

                // Create the word element
                Element wordElement = document.createElement("word");
                wordElement.appendChild(document.createTextNode(record.getWord()));
                recordElement.appendChild(wordElement);

                // Create the meaning element
                Element meaningElement = document.createElement("meaning");
                meaningElement.appendChild(document.createTextNode(record.getMeaning()));
                recordElement.appendChild(meaningElement);
            }

            // Transform the Document to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Save the XML file
            File xmlFile = new File(filePath);
            transformer.transform(new DOMSource(document), new StreamResult(xmlFile));
            System.out.println("XML file saved successfully at "+ filePath);
        } catch (Exception e) {
            System.out.println("XML file saved fail");
            e.printStackTrace();
        }
    }
    public static void saveXMLFile(String filePath, TreeMap<String, String> treeMapRecord) {
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Create a new Document
            Document document = builder.newDocument();

            // Create the root element
            Element rootElement = document.createElement("dictionary");
            document.appendChild(rootElement);

            // Iterate over the record objects and create the XML structure
            for (Map.Entry<String, String> entry : treeMapRecord.entrySet()) {
                // Create the record element
                Element recordElement = document.createElement("record");
                rootElement.appendChild(recordElement);

                // Create the word element
                Element wordElement = document.createElement("word");
                wordElement.appendChild(document.createTextNode(entry.getKey()));
                recordElement.appendChild(wordElement);

                // Create the meaning element
                Element meaningElement = document.createElement("meaning");
                meaningElement.appendChild(document.createTextNode(entry.getValue()));
                recordElement.appendChild(meaningElement);
            }

            // Transform the Document to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Save the XML file
            File xmlFile = new File(filePath);
            transformer.transform(new DOMSource(document), new StreamResult(xmlFile));
            System.out.println("XML file saved successfully at "+ filePath);
        } catch (Exception e) {
            System.out.println("XML file saved fail");
            e.printStackTrace();
        }
    }

    public static String getRootProjectDirectory() {
        String rootDirectory = System.getProperty("user.dir");
        //System.out.println("Root project directory: " + rootDirectory);
        return rootDirectory;
    }
    public static void saveSearchToHistory(String searchValue, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(
                                filePath,
                                true),
                        StandardCharsets.UTF_8))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            writer.write(timestamp + " - " + searchValue);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int calculateSearchCountBetweenDates(String startDateStr, String endDateStr, String filePath) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            int count = 0;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filePath),
                            StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" - ");
                    if (parts.length == 2) {
                        String timestampStr = parts[0];
                        Date timestamp = dateFormat.parse(timestampStr);
                        if (timestamp.after(startDate) && timestamp.before(endDate)) {
                            count++;
                        }
                    }
                }
            }

            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static List<String> getSearchWordsBetweenDates(String startDateStr, String endDateStr, String filePath) {
        List<String> searchWords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filePath),
                        StandardCharsets.UTF_8))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = dateFormat.parse(startDateStr + " 00:00:00");
            Date endDate = dateFormat.parse(endDateStr + " 23:59:59");
            //System.out.println("Start: " + startDate+" - End: "+endDate);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    String timestampStr = parts[0];
                    String searchWord = parts[1];
                    Date timestamp = dateFormat.parse(timestampStr);
                    if (timestamp.after(startDate) && timestamp.before(endDate)) {
                        searchWords.add(searchWord);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return searchWords;
    }
}
