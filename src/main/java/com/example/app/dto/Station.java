package com.example.app.dto;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "station")
@Table(name = "station")
@Entity
public @Data class Station {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "station_track_id")
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

    @Transient
    private List<Train> holderList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "station_train_id")
    private List<Train> trainList;

    @PrePersist
    public void setTheTrains() {

        if (myTracks != null && !myTracks.isEmpty()) {
            this.holderList = new ArrayList<>();
            this.trainList = new ArrayList<>();
            this.myTracks.forEach(track -> {
                List<Train> trainList1 = track.getStationTrains();
                this.holderList.addAll(trainList1);
            });

            this.holderList.forEach(train -> {
                train.setStation(this);
                this.trainList.add(train);
            });
        }
    }


    //    private List<Tracks> stationTracks;

//    @XmlElement(name = "tracks")
//    public List<Tracks> getStationTracks() {
//        return stationTracks;
//    }
}

