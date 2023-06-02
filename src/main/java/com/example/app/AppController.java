package com.example.app;

import com.example.app.dto.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.*;

@RestController
public class AppController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Autowired
    AppService appService;

    @GetMapping("/station/{ril100}/train/{trainNumber}/waggon/{number}")
    public Response getSections(@PathVariable("ril100") String ril100, @PathVariable("trainNumber") String trainNumber,
                                               @PathVariable("number") String number) throws Exception {
        return appService.getSections(ril100, trainNumber, number);
//        return appService.getIdentifiersStax(ril100, trainNumber, number);
    }
}
