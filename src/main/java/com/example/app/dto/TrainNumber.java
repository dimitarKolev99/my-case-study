package com.example.app.dto;

import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

import java.util.List;

public @Data class TrainNumber {

    private String stationTrainNumber;

    @XmlElement(name = "trainNumber")
    public String getTrainNumber() {
        return stationTrainNumber;
    }

}
