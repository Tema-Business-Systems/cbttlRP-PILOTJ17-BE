package com.transport.tracking.repository;

import com.transport.tracking.model.Trail;
import com.transport.tracking.model.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TrailRepository extends CrudRepository<Trail, BigDecimal> {

    public Trail findByTrailer(String trailer);

    public List<Trail> findByFcy(String site);

    public List<Trail> findByFcyIn(List<String> site);

    
    @Query("select c from Trail c where c.fcy IN (:sites) and (c.unavaildates IS NULL OR c.unavaildates NOT LIKE  '%'||:date||'%')")
    public List<Trail> findBySitesAndDate(@Param("sites")List<String> sitelist, @Param("date") String date);

}
