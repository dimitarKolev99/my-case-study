package com.example.app.dto;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@XmlRootElement
@Table(name = "track")
@Entity
public @Data class Track {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

//    private List<Trains> stationTrains;
//
//    @XmlElement(name = "trains")
//    public List<Trains> getStationTrains() {
//        return stationTrains;
//    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "track_train_id")
    private List<Train> stationTrains;

    @XmlElementWrapper(name = "trains")
    @XmlElement(name = "train")
    public List<Train> getStationTrains() {
        return stationTrains;
    }
}
