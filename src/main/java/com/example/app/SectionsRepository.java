package com.example.app;

import com.example.app.dto.utils.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionsRepository extends JpaRepository<Section, Long> {

//    @Query("SELECT i FROM WaggonIdentifier i WHERE i.id IN (SELECT DISTINCT w.id FROM Waggon w JOIN w.train t JOIN t.station s WHERE s.stationShortCode = :stationShortCode AND t.trainNumberToSave = :trainNumber AND w.waggonNumber = :waggonNumber)")
//    List<WaggonIdentifier> findAllByIdentifierAttributes(@Param("stationShortCode") String stationShortCode, @Param("trainNumber") String trainNumber, @Param("waggonNumber") String waggonNumber);

//    @Query("SELECT i FROM Identifier i JOIN i.waggons w JOIN w.train t JOIN t.station s WHERE s.stationShortCode = :stationShortCode AND t.trainNumber = :trainNumber AND w.waggonNumber = :waggonNumber")
//    List<WaggonIdentifier> findAllByIdentifierAttributes(@Param("stationShortCode") String stationShortCode, @Param("trainNumber") String trainNumber, @Param("waggonNumber") String waggonNumber);


    List<Section> findAllByWaggon_Train_Station_StationShortCodeAndWaggon_Train_TrainNumberToSaveAndWaggon_WaggonNumber(
            String stationShortCode, String trainNumber, String waggonNumber);

}
