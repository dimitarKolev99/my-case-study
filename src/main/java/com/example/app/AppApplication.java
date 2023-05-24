package com.example.app;

import com.example.app.dto.Station;
import com.example.app.dto.Waggons;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class AppApplication {

	public static final Logger LOGGER = LoggerFactory.getLogger(AppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@PostConstruct
	public void run() {
		try {
			LOGGER.debug("IN POSTCONSTRUCT");

			File file = new File("FF_2017-12-01_10-47-17.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Station.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Station station = (Station) jaxbUnmarshaller.unmarshal(file);


			LOGGER.debug("Station: {}", station.getStationShortCode());
			Waggons waggons = station.getStationTracks().get(0).getStationTracks().get(0)
					.getStationTrains().get(0).getTrain().get(0).getWaggonsList();
//			waggons
			LOGGER.debug("Station Tracks: {}", station.getStationTracks().get(0).getStationTracks().get(0)
					.getStationTrains().get(0).getTrain().get(0).getWaggonsList());

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
