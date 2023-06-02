package com.example.app;

import com.example.app.dto.response.Response;
import com.example.app.dto.utils.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.*;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;

@Service
public class AppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppApplication.class);

    @Autowired
    private SectionsRepository sectionsRepository;

    @Autowired
    private StationRepository stationRepository;

    public String getFileName(String prefixToCompare) {

        File directory = new File("Wagenreihungsplan_RawData_201712112");

        String searchedFile = "";
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();

                        int underscoreIndex = fileName.indexOf('_');
                        if (underscoreIndex != -1) {
                            String prefix = fileName.substring(0, underscoreIndex);

                            if (prefix.equals(prefixToCompare)) {
                                searchedFile = fileName;
                            }
                        }
                    }
                }
            }
        }
        return searchedFile;
    }

    public Station getStation(String searchedFile) throws JAXBException {
        File file = new File("Wagenreihungsplan_RawData_201712112/" + searchedFile);
        JAXBContext jaxbContext = JAXBContext.newInstance(Station.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (Station) jaxbUnmarshaller.unmarshal(file);
    }

    public Set<String> getIdentifiersXPath(String stationShortCode, String trainNumber, String waggonNumber) throws XPathExpressionException {

        Set<String> res = new HashSet<>();

        String searchedFile = getFileName(stationShortCode);

        InputSource xml = new InputSource("Wagenreihungsplan_RawData_201712112/" + searchedFile);

        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "(//train[trainNumbers/trainNumber = '" + trainNumber + "'])[1]/waggons/waggon[number = '" + waggonNumber + "']/sections/identifier/text()";

//        String expression = "/station[shortcode='" + stationShortCode + "']/tracks/track/trains/train[trainNumbers/trainNumber='" + trainNumber + "'][1]" +
//                "/waggons/waggon[number='" + waggonNumber + "']/sections/identifier/text()";

        XPathExpression xPathExpression = xpath.compile(expression);

        NodeList nodes = (NodeList) xPathExpression.evaluate(xml, XPathConstants.NODESET);

        if (nodes.getLength() == 0) {
            LOGGER.debug("NODES LENGTH: is ZERO");
        }

        for (int i = 0; i < nodes.getLength(); i++) {

            Node singleNode = nodes.item(i);
            singleNode.getParentNode().removeChild(singleNode);

            res.add(nodes.item(i).getTextContent());

            LOGGER.debug("NODE: " + nodes.item(i).getTextContent());
        }

        return res;
    }

    public Set<String> getIdentifiersStax(String stationShortCode, String trainNumber, String waggonNumber) throws IOException {

        String searchedFile = getFileName(stationShortCode);

        File file = new File("Wagenreihungsplan_RawData_201712112/" + searchedFile);
        FileInputStream fis = new FileInputStream(file);

        Set<String> sections = new HashSet<>();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();

            XMLEventReader reader = factory.createXMLEventReader(fis);

            boolean isMatchingTrain = false;
            boolean isMatchingWaggon = false;
            boolean found = false;
            String currentTrainNumber = null;
            String currentWaggonNumber = null;

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    String elementName = startElement.getName().getLocalPart();

                    if (elementName.equals("trainNumber")) {
                        event = reader.nextEvent();
                        if (event.isCharacters()) {
                            String trainNumberValue = event.asCharacters().getData();
                            if (trainNumberValue.equals(trainNumber)) {
                                currentTrainNumber = trainNumberValue;
                                isMatchingTrain = true;
                            }
                        }
                    } else if (found) {
                        break;
                    } else if (isMatchingTrain && elementName.equals("waggon")) {
                        sections.clear();
                        while (reader.hasNext()) {
                            event = reader.nextEvent();

                            if (event.isStartElement()) {
                                StartElement innerStartElement = event.asStartElement();
                                String innerElementName = innerStartElement.getName().getLocalPart();

                                if (innerElementName.equals("sections")) {
                                    while (reader.hasNext()) {
                                        event = reader.nextEvent();
                                        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("identifier")) {
                                            event = reader.nextEvent();
                                            if (event.isCharacters()) {
                                                String section = event.asCharacters().getData();
                                                sections.add(section);
                                            }
                                        } else if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("sections")) {
                                            break;
                                        }
                                    }

                                } else if (innerElementName.equals("number")) {
                                    event = reader.nextEvent();
                                    if (event.isCharacters()) {
                                        currentWaggonNumber = event.asCharacters().getData();
                                        if (currentWaggonNumber.equals(waggonNumber)) {
                                            isMatchingWaggon = true;
                                            found = true;
                                            break;
                                        } else {
                                            sections.clear();
                                        }
                                    }
                                }
                            } else if (isMatchingWaggon && event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("waggon")) {
                                break;
                            } else if (event.isEndElement()) {
                                EndElement endElement = event.asEndElement();
                                String endElementName = endElement.getName().getLocalPart();
                                if (endElementName.equals("waggon")) {
                                    if (isMatchingWaggon) {
                                        break;
                                    } else {
                                        currentWaggonNumber = null;
                                    }
                                }
                            }
                        }
                    }

                } else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    String elementName = endElement.getName().getLocalPart();

                    if (elementName.equals("train")) {
                        if (isMatchingTrain && currentTrainNumber.equals(trainNumber)) {
                            for (String section : sections) {
                                System.out.println("Current Section: " + section);
                            }
                        }

                        isMatchingTrain = false;
                        isMatchingWaggon = false;
                        currentTrainNumber = null;
                        currentWaggonNumber = null;
                        sections.clear();
                    }
                }
            }

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }

//        Response response = new Response();
//        response.setSections(sections);

        return sections;
    }

    public List<Section> getSectionsFromRepository(String stationShortCode, String trainNumber, String waggonNumber) {
        return sectionsRepository.findAllByWaggon_Train_Station_StationShortCodeAndWaggon_Train_TrainNumberToSaveAndWaggon_WaggonNumber(stationShortCode,
                trainNumber, waggonNumber);

    }

    public void saveStation(Station station) {
        stationRepository.save(station);
    }

    public List<String> parseFileAndGetSections(String searchedFile) throws JAXBException {
        File file = new File("Wagenreihungsplan_RawData_201712112/" + searchedFile);
        JAXBContext jaxbContext = JAXBContext.newInstance(Station.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Station station = (Station) jaxbUnmarshaller.unmarshal(file);


//        LOGGER.debug("Station: {}", station.getStationShortCode());


//			waggons
//			LOGGER.debug("TRACKS: {}", station.getMyTracks());

        List<Track> tracks = station.getMyTracks();
        List<Train> list = new ArrayList<>();
        tracks.forEach(track -> list.addAll(track.getStationTrains()));

        Stream<Train> filteredStream = list.stream().filter(train -> Objects.equals(train.getTrainNumberDb(), "2310"));

//			filteredStream.forEach(t -> LOGGER.debug("HERE: {}", t));

        List<Train> filteredList = filteredStream.toList();

        List<Waggon> waggons = new ArrayList<>();
        filteredList.forEach(train -> waggons.addAll(train.getMyWaggonsList()));

        Stream<Waggon> filteredWaggonsSteam = waggons.stream().filter(waggon -> Objects.equals(waggon.getWaggonNumber(), "10"));
        List<Waggon> filteredWaggonsList = filteredWaggonsSteam.toList();


        List<String> sections = new ArrayList<>();

        return sections;
    }

    public Response getSections(String ril100, String trainNumber, String number) throws IOException {

        Response response = new Response();

        List<Section> waggonSections = getSectionsFromRepository(ril100, trainNumber, number);

        if (waggonSections.isEmpty()) {
            Set<String> identifiers = getIdentifiersStax(ril100, trainNumber, number);
            response.setSections(identifiers);

            return response;
        }

        Set<String> sectionSet = new HashSet<>();

        waggonSections.forEach(identifier -> {
            sectionSet.add(identifier.getSectionString());
        });

        response.setSections(sectionSet);
        return response;
    }
}

