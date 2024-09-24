import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileXml = "data.xml";
        List<Employee> list = parseXML(fileXml);
        listToJson(list);
    }

    private static List<Employee> parseXML(String fileXml) {
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileXml));

            Node root = document.getDocumentElement();
            read(root, list);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public static void read(Node node, List<Employee> list) {
        NodeList listChild = node.getChildNodes();
        for (int i = 0; i < listChild.getLength(); i++) {
            Node nodeChild = listChild.item(i);

            if (Node.ELEMENT_NODE == nodeChild.getNodeType()) {

                Element element = (Element) nodeChild;
                list.add(changeElement(element));
            }
        }
    }

    //Метод что-бы искать теги в узле и создавать объект Employee
    private static Employee changeElement(Element element) {

        long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());

        String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();

        String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();

        String country = element.getElementsByTagName("country").item(0).getTextContent();

        int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());

        return new Employee(id, firstName, lastName, country, age);
    }

    private static String listToJson(List<Employee> list) {
        String formatJson = "";
        try (FileWriter writer = new FileWriter("jsonData.json")) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Type listType = new TypeToken<List<Employee>>() {
            }.getType();
            formatJson = gson.toJson(list, listType);
            writer.write(formatJson);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return formatJson;
    }

}
