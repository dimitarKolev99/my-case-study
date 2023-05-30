package com.example.app.dto.utils;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlRootElement(name = "station")
@Table(name = "station")
@Entity
public @Data class Station {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Transient
    private List<Track> myTracks;

    private String stationShortCode;

    @XmlElementWrapper(name = "tracks")
    @XmlElement(name = "track")
    public List<Track> getMyTracks() {
        return myTracks;
    }

    @XmlElement(name = "shortcode")
    public String getStationShortCode() {
        return stationShortCode;
    }


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "station_train_id")
    private List<Train> trainList;

    @PrePersist
    public void setTheTrains() {

        if (myTracks != null && !myTracks.isEmpty()) {

            List<Train> holderList = new ArrayList<>();
            this.trainList = new ArrayList<>();

            Set<String> trainNumbers = new HashSet<>();

            this.myTracks.forEach(track -> {
                List<Train> trainList1 = track.getStationTrains();
                holderList.addAll(trainList1);
            });

            holderList.forEach(train -> {
                if (!train.getTrainNumberXml().isEmpty()
                && !trainNumbers.contains(train.getTrainNumberXml().get(0))) {
                    train.setStation(this);
                    this.trainList.add(train);
                    trainNumbers.add(train.getTrainNumberXml().get(0));
                }
            });

        }
    }

}

