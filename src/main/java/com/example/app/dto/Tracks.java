package com.example.app.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;


import java.util.List;

@XmlRootElement
public @Data class Tracks {

    private List<Track> stationTracks;

    @XmlElement(name = "track")
    public List<Track> getStationTracks() {
        return stationTracks;
    }

}
