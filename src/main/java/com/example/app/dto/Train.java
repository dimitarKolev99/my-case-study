package com.example.app.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//@XmlRootElement
public @Data class Train {

    private TrainNumbers trainNumbers;

//    private List<Waggons> waggonsList;

    private Waggons waggonsList;

    @XmlElement(name = "trainNumbers")
    public TrainNumbers getTrainNumbers() {
        return trainNumbers;
    }

    @XmlElement(name = "waggons")
    public Waggons getWaggonsList() {
        return waggonsList;
    }

}
