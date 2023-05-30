package com.example.app.dto.utils;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "waggon")
@Entity
public @Data class Waggon {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "waggon_id")
    private Long id;

    @XmlElement(name = "number")
    private String waggonNumber;

    @XmlElementWrapper(name = "sections")
    @XmlElement(name = "identifier")
    @Transient
    private List<String> identifiersStrings;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "section_identifier_id")
    private List<Section> identifierList;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "train_id")
    private Train train;

    @PrePersist
    public void setTheIdentifiers() {
        if (identifiersStrings != null && !identifiersStrings.isEmpty()) {
            identifierList = new ArrayList<>();
            identifiersStrings.forEach(s -> {
                Section section = new Section();
                section.setSectionString(s);
                section.setWaggon(this);

                identifierList.add(section);
            });
        }
    }
}



