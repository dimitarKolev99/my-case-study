package com.example.app;

import com.example.app.dto.EIdentifier;
import com.example.app.dto.WaggonIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdentifierRepository extends JpaRepository<WaggonIdentifier, Long> {

//    @Query("SELECT i FROM WaggonIdentifier i WHERE i.id IN (SELECT DISTINCT w.id FROM Waggon w JOIN w.train t JOIN t.station s WHERE s.stationShortCode = :stationShortCode AND t.trainNumberToSave = :trainNumber AND w.waggonNumber = :waggonNumber)")
//    List<WaggonIdentifier> findAllByIdentifierAttributes(@Param("stationShortCode") String stationShortCode, @Param("trainNumber") String trainNumber, @Param("waggonNumber") String waggonNumber);

//    @Query("SELECT i FROM Identifier i JOIN i.waggons w JOIN w.train t JOIN t.station s WHERE s.stationShortCode = :stationShortCode AND t.trainNumber = :trainNumber AND w.waggonNumber = :waggonNumber")
//    List<WaggonIdentifier> findAllByIdentifierAttributes(@Param("stationShortCode") String stationShortCode, @Param("trainNumber") String trainNumber, @Param("waggonNumber") String waggonNumber);


    List<WaggonIdentifier> findAllByWaggon_Train_Station_StationShortCodeAndWaggon_Train_TrainNumberToSaveAndWaggon_WaggonNumber(
            String stationShortCode, String trainNumber, String waggonNumber);

    WaggonIdentifier findByStringIdentifier(String stringIdentifier);

}
