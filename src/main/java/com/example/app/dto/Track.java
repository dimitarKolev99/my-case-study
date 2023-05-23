package com.example.app.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@XmlRootElement
public @Data class Track {

    private List<Trains> stationTrains;

    @XmlElement(name = "trains")
    public List<Trains> getStationTrains() {
        return stationTrains;
    }
}
