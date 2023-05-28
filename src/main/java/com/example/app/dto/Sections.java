//package com.example.app.dto;
//
//import com.example.app.AppApplication;
//import jakarta.annotation.PostConstruct;
//import jakarta.persistence.*;
//import jakarta.xml.bind.annotation.XmlAccessType;
//import jakarta.xml.bind.annotation.XmlAccessorType;
//import jakarta.xml.bind.annotation.XmlElement;
//import jakarta.xml.bind.annotation.XmlRootElement;
//import lombok.Data;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@XmlAccessorType(XmlAccessType.FIELD)
//@Table(name = "section")
//@Entity
//public @Data class Sections {
//
//    public static final Logger LOGGER = LoggerFactory.getLogger(AppApplication.class);
//
//    @Id
//    @GeneratedValue(strategy= GenerationType.AUTO)
//    private Long id;
//
//    @XmlElement(name = "identifier")
//    @Transient
//    private List<String> identifiers;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "section_identifier_id")
//    private List<Identifier> identifierList;
//
//    @PrePersist
//    public void setTheIdentifiers() {
//
////        if (identifiers != null && !identifiers.isEmpty()) {
////            this.identifierList = new ArrayList<>();
////            this.identifiers.forEach(s -> {
////                Identifier identifier = new Identifier();
////                identifier.setWaggonIdentifier(s);
////                this.identifierList.add(identifier);
////            });
////        }
//
//    }
//
//}
