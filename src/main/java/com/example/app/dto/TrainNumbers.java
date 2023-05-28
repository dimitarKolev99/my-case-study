package com.example.app.dto;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@XmlRootElement
@Table(name = "train_number")
@Entity
public @Data class TrainNumbers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String trainNumber;

    @XmlElement(name = "trainNumber")
    public String getTrainNumber() {
        return trainNumber;
    }
}
