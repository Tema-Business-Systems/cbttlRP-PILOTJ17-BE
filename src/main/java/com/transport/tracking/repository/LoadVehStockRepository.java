package com.transport.tracking.repository;

import com.transport.tracking.model.LoadVehStock;
import com.transport.tracking.model.Trip;
import com.transport.tracking.model.VehRoute;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoadVehStockRepository extends CrudRepository<LoadVehStock, String> {

    public List<LoadVehStock> findAll();

    public LoadVehStock findByXvrsel(String vrcode);


    public List<LoadVehStock> findByStofcyAndXxiptdatOrderByVcrnumAsc(String site, Date date);


    @Query("select c from LoadVehStock c where c.stofcy =  ?1 and MONTH(xxiptdat) = ?2 and YEAR(xxiptdat) = ?3")
    public List<LoadVehStock> findBySiteandDate(String chars, int mon, int year);

    @Query("select c from LoadVehStock c where c.stofcy =  ?1 and MONTH(xxiptdat) = ?2 and YEAR(xxiptdat) = ?3 and c.vcrnum like '%XCHG%' ORDER BY c.vcrnum DESC ")
    public List<LoadVehStock> findBySiteandDateOrderByVcrnumDesc(String chars, int mon, int year);


    @Query("select c from LoadVehStock c where c.vcrnum like 'LVS-%' ORDER BY c.vcrnum DESC")
    public List<LoadVehStock> findLatestLVSinFormat();

}
