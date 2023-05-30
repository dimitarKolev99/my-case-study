package com.example.app.dto.utils;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//@XmlRootElement
@Table(name = "train")
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public @Data class Train {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Transient
    private List<String> trainNumberXml = new ArrayList<>();

    @XmlElementWrapper(name = "trainNumbers")
    @XmlElement(name = "trainNumber")
    public List<String> getTrainNumberForXml() {
        return trainNumberXml;
    }

    public String trainNumberToSave;

    public String getTrainNumberDb() {
        return trainNumberToSave;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "train_waggon_id")
    private List<Waggon> waggonsList = new ArrayList<>();

    @XmlElementWrapper(name = "waggons")
    @XmlElement(name = "waggon")
    public List<Waggon> getMyWaggonsList() {
        return waggonsList;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

    @PrePersist
    public void setTheWaggonsTrain() {

        if (this.trainNumberXml != null && !this.trainNumberXml.isEmpty()) {
            this.trainNumberToSave = this.trainNumberXml.get(0);
        }

        if (this.waggonsList != null && !this.waggonsList.isEmpty()) {
            List<Waggon> waggonsCopy = new ArrayList<>(this.waggonsList);
            this.waggonsList = new ArrayList<>();

            waggonsCopy.forEach(waggon -> {
                waggon.setTrain(this);
                this.waggonsList.add(waggon);
            });
        }
    }

}
