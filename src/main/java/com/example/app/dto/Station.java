package com.example.app.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@XmlRootElement(name = "station")
public @Data class Station {

    private List<Tracks> stationTracks;

    private String stationShortCode;

    @XmlElement(name = "tracks")
    public List<Tracks> getStationTracks() {
        return stationTracks;
    }

    @XmlElement(name = "shortcode")
    public String getStationShortCode() {
        return stationShortCode;
    }

}

