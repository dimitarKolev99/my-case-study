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
public class AppApplication implements ApplicationListener<ApplicationReadyEvent> {

	public static final Logger LOGGER = LoggerFactory.getLogger(AppApplication.class);

	@Autowired
	private AppService appService;

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Override
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

							appService.saveStation(station);
						}
					}
				}
			}
	}
}
