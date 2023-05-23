package com.example.app.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@XmlRootElement
public @Data class TrainNumbers {

    private String trainNumber;

    @XmlElement(name = "trainNumber")
    public String getTrainNumber() {
        return trainNumber;
    }
}
