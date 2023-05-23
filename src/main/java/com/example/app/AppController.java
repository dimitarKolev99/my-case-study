package com.example.app;

import com.example.app.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@RestController
public class AppController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/station/{ril100}/train/{trainNumber}/waggon/{number}")
    public ResponseEntity<Response> getSections(@PathVariable("ril100") String ril100, @PathVariable("trainNumber") String trainNumber,
                                               @PathVariable("number") String number) {

        String searchValue = "";

        try {
            File xmlFile = new File("FF_2017-12-01_10-47-17.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            searchValue = trainNumber;
            String searchPropertyName = "trainNumber";
            String searchPropertyValue = "2310";

            searchXML(doc.getDocumentElement(), searchValue, searchPropertyName, searchPropertyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response response = new Response();
        response.setValue(searchValue);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static void searchXML(Node node, String searchValue, String searchPropertyName, String searchPropertyValue) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String elementValue = element.getTextContent();

            NamedNodeMap attributes = element.getAttributes();
//            if (elementValue.equals(searchValue) && hasAttribute(attributes, searchPropertyName, searchPropertyValue)) {
//                System.out.println("Match found: " + elementValue);
//                // Additional logic for handling the match
//            }
            if (elementValue.equals(searchValue)) {

                System.out.println("Match found: " + elementValue + element.getTagName());

                // Additional logic for handling the match
            }
        }

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            searchXML(child, searchValue, searchPropertyName, searchPropertyValue); // Recursive call to search nested nodes
        }
    }

    private static boolean hasAttribute(NamedNodeMap attributes, String propertyName, String propertyValue) {
        if (attributes != null) {
            Node propertyNode = attributes.getNamedItem(propertyName);
            if (propertyNode != null && propertyNode.getNodeValue().equals(propertyValue)) {
                return true;
            }
        }
        return false;
    }
}
