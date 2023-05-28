package com.example.app.dto;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Table(name = "identifier")
@Entity
public @Data class WaggonIdentifier {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "identifier_id")
    private Long id;

    private String stringIdentifier;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "waggon_id")
    private Waggon waggon;
}
