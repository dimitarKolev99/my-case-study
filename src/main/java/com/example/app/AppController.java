package com.example.app;

import com.example.app.dto.WaggonIdentifier;
import com.example.app.dto.Response;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.*;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AppController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Autowired
    AppService appService;

    @GetMapping("/station/{ril100}/train/{trainNumber}/waggon/{number}")
    public ResponseEntity<Response> getSections(@PathVariable("ril100") String ril100, @PathVariable("trainNumber") String trainNumber,
                                               @PathVariable("number") String number) throws JAXBException, XPathExpressionException {

        List<WaggonIdentifier> waggonIdentifiers = appService.getIdentfiersForStation(ril100, trainNumber, number);

        List<String> identifiers;
        if (waggonIdentifiers.isEmpty()) {
            identifiers = appService.getIdentifiersXPath(ril100, trainNumber, number);

            Response response = new Response();
            response.setSections(identifiers);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        List<String> res = new ArrayList<>();
        waggonIdentifiers.forEach(identifier -> {
            LOGGER.debug("IDENTIFIER: {}", identifier.getStringIdentifier());
            res.add(identifier.getStringIdentifier());
        });

        Response response = new Response();
        response.setSections(res);

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
