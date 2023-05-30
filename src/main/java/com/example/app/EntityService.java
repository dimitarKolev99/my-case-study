package com.example.app;

import com.example.app.dto.utils.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntityService {

    @Autowired
    StationRepository stationRepository;

    @Transactional
    public void doHeavy(Station station) {
        stationRepository.save(station);
    }
}
