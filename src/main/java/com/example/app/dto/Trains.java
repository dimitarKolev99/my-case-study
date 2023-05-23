package com.example.app.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@XmlRootElement
public @Data class Trains {

    private List<Train> train;

    @XmlElement(name = "train")
    public List<Train> getTrain() {
        return train;
    }
}
