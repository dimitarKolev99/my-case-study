package com.example.app.dto.utils;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@XmlRootElement
public @Data class Track {
    private List<Train> stationTrains;

    @XmlElementWrapper(name = "trains")
    @XmlElement(name = "train")
    public List<Train> getStationTrains() {
        return stationTrains;
    }
}
