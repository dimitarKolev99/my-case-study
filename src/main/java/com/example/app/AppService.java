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

import javax.xml.xpath.*;
import java.io.File;
import java.util.*;
import java.util.stream.Stream;

@Service
public class AppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppApplication.class);

    @Autowired
    private EntityService entityService;

    @Autowired
    private SectionsRepository sectionsRepository;

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

    public void doAsync(Station station) {
        entityService.doHeavy(station);
    }


    public List<Section> getSectionsFromRepository(String stationShortCode, String trainNumber, String waggonNumber) {
        return sectionsRepository.findAllByWaggon_Train_Station_StationShortCodeAndWaggon_Train_TrainNumberToSaveAndWaggon_WaggonNumber(stationShortCode,
                trainNumber, waggonNumber);

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

//        filteredWaggonsList.forEach(waggon -> LOGGER.debug("RETURNED: {}", sections.addAll(waggon.getSections().getIdentifiers())));
//			LOGGER.debug("list: {}", list.toString());


        return sections;
    }

    public ResponseEntity<Response> getSections(String ril100, String trainNumber, String number) throws XPathExpressionException {

        List<Section> waggonSections = getSectionsFromRepository(ril100, trainNumber, number);

        if (waggonSections.isEmpty()) {
            Set<String> identifiers = getIdentifiersXPath(ril100, trainNumber, number);

            Response response = new Response();
            response.setSections(identifiers);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Set<String> sections = new HashSet<>();

        waggonSections.forEach(identifier -> {
            LOGGER.debug("IDENTIFIER: {}", identifier.getSectionString());
            sections.add(identifier.getSectionString());
        });

        Response response = new Response();
        response.setSections(sections);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

