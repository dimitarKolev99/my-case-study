package com.example.app;

import com.example.app.dto.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class AppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppApplication.class);

    @Autowired
    private EntityService entityService;

    @Autowired
    private IdentifierRepository identifierRepository;

    public String getFileName(String prefixToCompare) {
//        String prefixToCompare = "FF";

        File directory = new File("Wagenreihungsplan_RawData_201712112");

        String searchedFile = "";
        // Check if the specified path is a directory
        if (directory.isDirectory()) {
            // Get all files in the directory
            File[] files = directory.listFiles();

            // Iterate through the files
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Get the file name
                        String fileName = file.getName();

                        // Cut the string from the beginning to the first "_"
                        int underscoreIndex = fileName.indexOf('_');
                        if (underscoreIndex != -1) {
                            String prefix = fileName.substring(0, underscoreIndex);

                            // Compare with the given string
                            if (prefix.equals(prefixToCompare)) {
                                searchedFile = fileName;
                                System.out.println("File name: " + fileName);
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("Invalid directory path.");
        }
        return searchedFile;
    }

    public Station getStation(String searchedFile) throws JAXBException {
        File file = new File("Wagenreihungsplan_RawData_201712112/" + searchedFile);
        JAXBContext jaxbContext = JAXBContext.newInstance(Station.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (Station) jaxbUnmarshaller.unmarshal(file);
    }

    @Async
    public void doAsync(Station station) {
        entityService.doHeavy(station);
    }

    public List<WaggonIdentifier> getIdentfiersForStation(String stationShortCode, String trainNumber, String waggonNumber) {
        return identifierRepository.findAllByWaggon_Train_Station_StationShortCodeAndWaggon_Train_TrainNumberToSaveAndWaggon_WaggonNumber(stationShortCode,
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
}

