package com.example.app.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@XmlRootElement
public @Data class Train {

    private TrainNumbers trainNumbers;

    @XmlElement(name = "trainNumbers")
    public TrainNumbers getTrainNumbers() {
        return trainNumbers;
    }

}
