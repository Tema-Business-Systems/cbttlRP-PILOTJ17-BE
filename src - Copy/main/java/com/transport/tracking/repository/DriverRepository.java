package com.transport.tracking.repository;

import com.transport.tracking.model.Driver;
import com.transport.tracking.model.Trail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface DriverRepository extends CrudRepository<Driver, BigDecimal> {

    public Driver findByDriverid(String driverId);

    public List<Driver> findByFcy(String site);

   public List<Driver> findByFcyIn(List<String> sites);

    public List<Driver> findAll();

    @Query("select c from Driver c where c.fcy IN (:sites) and (c.unavaildates IS NULL OR c.unavaildates NOT LIKE  '%'||:date||'%')")
    public List<Driver> findBySitesAndDate(@Param("sites")List<String> sitelist, @Param("date") String date);

}
