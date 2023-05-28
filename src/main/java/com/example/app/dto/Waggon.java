package com.example.app.dto;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.hibernate.boot.model.naming.Identifier;

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
    private List<WaggonIdentifier> identifierList;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name = "waggon_identifier",
//        joinColumns = @JoinColumn(name = "waggon_id"),
//        inverseJoinColumns = @JoinColumn(name = "identifier_id"))
//    @XmlElementWrapper(name = "sections")
////    @XmlElement(name = "identifier")
//    private List<WaggonIdentifier> waggonIdentifiers = new ArrayList<>();

//    @XmlElementWrapper(name = "sections")
//    @XmlElement(name = "identifier")
//    public List<WaggonIdentifier> getIdentifiers() {
//        return waggonIdentifiers;
//    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "train_id")
    private Train train;

    @PrePersist
    public void setTheIdentifiers() {
        if (identifiersStrings != null && !identifiersStrings.isEmpty()) {
            identifierList = new ArrayList<>();
            identifiersStrings.forEach(s -> {
                WaggonIdentifier waggonIdentifier = new WaggonIdentifier();
                waggonIdentifier.setStringIdentifier(s);
                waggonIdentifier.setWaggon(this);

                identifierList.add(waggonIdentifier);
            });
        }
//        this.setIdentifierList(identifiersHelper.findIdentifiers(this.identifiers));
    }
}

        //    @XmlElement(name = "sections")
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id")
//    private Sections sections;

//    @OneToMany()
//    @JoinColumn(name = "waggon_identifier_id")
//    private List<Identifier> waggonIdentifiers;
//
//    @XmlElementWrapper(name = "sections")
//    @XmlElement(name = "identifier")
//    public List<Identifier> getIdentifiers() {
//        return waggonIdentifiers;
//    }


