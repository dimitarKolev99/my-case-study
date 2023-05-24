package com.example.app.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public @Data class Waggons {

    @XmlElement(name = "waggon")
    private List<Waggon> waggons;

}
