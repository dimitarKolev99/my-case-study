package com.example.app;

import com.example.app.dto.utils.Station;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;

@SpringBootApplication
@EnableAsync
public class AppApplication implements ApplicationListener<ApplicationReadyEvent> {

	public static final Logger LOGGER = LoggerFactory.getLogger(AppApplication.class);

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private AppService appService;

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

//	@PostConstruct
//	public void run() {
//		try {
//			LOGGER.debug("IN POSTCONSTRUCT");
//
//			String searchedFile = appService.getFileName("FF");
//			Station station = appService.getStation(searchedFile);
//
//			appService.doAsync(station);


//			String prefixToCompare = "FF";
//
//			File directory = new File("Wagenreihungsplan_RawData_201712112");
//
//			String searchedFile = "";
//			// Check if the specified path is a directory
//			if (directory.isDirectory()) {
//				// Get all files in the directory
//				File[] files = directory.listFiles();
//
//				// Iterate through the files
//				if (files != null) {
//					for (File file : files) {
//						if (file.isFile()) {
//							// Get the file name
//							String fileName = file.getName();
//
//							// Cut the string from the beginning to the first "_"
//							int underscoreIndex = fileName.indexOf('_');
//							if (underscoreIndex != -1) {
//								String prefix = fileName.substring(0, underscoreIndex);
//
//								// Compare with the given string
//								if (prefix.equals(prefixToCompare)) {
//									searchedFile = fileName;
//									System.out.println("File name: " + fileName);
//								}
//							}
//						}
//					}
//				}
//			} else {
//				System.out.println("Invalid directory path.");
//			}
//
//
//			File file = new File("Wagenreihungsplan_RawData_201712112/" + searchedFile);
//
//			InputSource xml = new InputSource("Wagenreihungsplan_RawData_201712112/" + searchedFile);
//
//			// Create an XPath expression to select the titles of books in the "web" category
//			XPath xpath = XPathFactory.newInstance().newXPath();
//			String expression = "//train[trainNumbers/trainNumber = '2310']/waggons/waggon[number = '10']/sections/identifier/text()";
//			XPathExpression xPathExpression = xpath.compile(expression);
//
//			// Evaluate the XPath expression and print the results
//			NodeList nodes = (NodeList) xPathExpression.evaluate(xml, XPathConstants.NODESET);
//
//			if (nodes.getLength() == 0) {
//				LOGGER.debug("NODES LENGTH: is ZERO");
//			}
//
//			for (int i = 0; i < nodes.getLength(); i++) {
//				System.out.println("NODE: " + nodes.item(i).getTextContent());
//			}
//			JAXBContext jaxbContext = JAXBContext.newInstance(Station.class);
//			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//			Station station = (Station) jaxbUnmarshaller.unmarshal(file);
//
//			stationRepository.save(station);



//			waggons
//			LOGGER.debug("TRACKS: {}", station.getMyTracks());

//			List<Track> tracks = station.getMyTracks();
//			List<Train> list = new ArrayList<>();
//			tracks.forEach(track -> list.addAll(track.getStationTrains()));
//
//			Stream<Train> filteredStream = list.stream().filter(train -> Objects.equals(train.getTrainNumbers().getTrainNumber(), "2310"));
//
////			filteredStream.forEach(t -> LOGGER.debug("HERE: {}", t));
//
//			List<Train> filteredList = filteredStream.toList();
//
//			List<Waggon> waggons = new ArrayList<>();
//			filteredList.forEach(train -> waggons.addAll(train.getWaggonsList()));
//
//			Stream<Waggon> filteredWaggonsSteam = waggons.stream().filter(waggon -> Objects.equals(waggon.getWaggonNumber(), "10"));
//			List<Waggon> filteredWaggonsList = filteredWaggonsSteam.toList();
//
//
//			filteredWaggonsList.forEach(waggon -> LOGGER.debug("RETURNED: {}", waggon.getSections().getIdentifiers()));
//			LOGGER.debug("list: {}", list.toString());

//		} catch (JAXBException e) {
//			throw new RuntimeException(e);
//		}
//	}

	@Override
	@Async
	public void onApplicationEvent(ApplicationReadyEvent event) {

		File directory = new File("Wagenreihungsplan_RawData_201712112");

			if (directory.isDirectory()) {
				File[] files = directory.listFiles();

				if (files != null) {
					for (File file : files) {
						if (file.isFile()) {
							String fileName = file.getName();

							Station station = null;
							try {
								station = appService.getStation(fileName);
							} catch (JAXBException e) {
								throw new RuntimeException(e);
							}

							appService.doAsync(station);
						}
					}
				}
			}
	}
}
