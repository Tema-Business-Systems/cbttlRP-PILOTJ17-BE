package com.transport.tracking.repository;

import com.transport.tracking.model.LoadVehStock;
import com.transport.tracking.model.Trip;
import com.transport.tracking.model.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {

    List<Vehicle> findByFcy(String fcy);

    public List<Vehicle> findAll();

    public Vehicle findByCodeyve(String codeyve);

    @Query("select c from Vehicle c where c.fcy IN (:sites)")
    List<Vehicle> findBySites(@Param("sites")List<String> sitelist);


    @Query("select c from Vehicle c where c.fcy IN (:sites) and (c.unavaildates IS NULL OR c.unavaildates NOT LIKE  '%'||:date||'%')")
    public List<Vehicle> findBySitesAndDate(@Param("sites")List<String> sitelist, @Param("date") String date);


}
