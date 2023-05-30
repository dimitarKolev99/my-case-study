package com.example.app.dto.utils;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "section")
@Entity
public class Section {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "section_id")
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String sectionString;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "waggon_id")
    @Setter
    private Waggon waggon;
}
