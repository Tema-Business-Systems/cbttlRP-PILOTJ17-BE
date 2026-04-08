package com.transport.tracking.repository;

import com.transport.tracking.model.Driver;
import com.transport.tracking.model.Facility;
import com.transport.tracking.model.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FacilityRepository extends CrudRepository<Facility, String>  {

    List<Facility> findByOrderByFcynamAsc();

    List<Facility> findByFcyNumberOrderByFcynamAsc(int fcyNumber);

    public Facility findByFcy(String fcy);

    @Query("select c from Facility c where c.fcy IN (:fcy)")
    Facility findBysite(@Param("fcy") String site);


}
