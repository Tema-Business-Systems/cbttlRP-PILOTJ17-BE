package com.transport.tracking.repository;

import com.transport.tracking.model.Trip;
import com.transport.tracking.model.VehTrail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VehTrailRepository extends CrudRepository<VehTrail, String> {

    public List<VehTrail> findAll();


    @Query(value="select TOP 1 * from PILOT.XX10CVEHTRAI c where c.VEHICLE_0 = :veh and c.FCY_0 = :fcy and (DATDEB_0 <= :dte  AND DATEND_0 >= :dte) ",nativeQuery = true)
    public VehTrail findTrailerbyVehicleSiteandDate(@Param("veh") String veh,@Param("fcy") String fcy, @Param("dte") String dte);


}
