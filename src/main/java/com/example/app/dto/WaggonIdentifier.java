package com.example.app.dto;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Table(name = "identifier")
@Entity
public class WaggonIdentifier {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "identifier_id")
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String stringIdentifier;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "waggon_id")
    @Setter
    private Waggon waggon;
}
