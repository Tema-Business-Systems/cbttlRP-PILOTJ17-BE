package com.transport.tracking.repository;

import com.transport.tracking.model.VehDriver;
import com.transport.tracking.model.VehTrail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehDriverRepository extends CrudRepository<VehDriver, String> {

    public List<VehDriver> findAll();


    @Query(value="select TOP 1 * from PILOT.XX10CALLOC c where c.XVEHICLE_0 = :veh and (XSTRTDAT_0 <= :dte  AND XENDDAT_0 >= :dte) ",nativeQuery = true)
    public VehDriver findDriverbyVehicleandDate(@Param("veh") String veh, @Param("dte") String dte);


}
