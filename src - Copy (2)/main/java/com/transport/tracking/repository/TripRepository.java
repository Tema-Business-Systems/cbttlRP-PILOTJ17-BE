package com.transport.tracking.repository;

import com.transport.tracking.model.LoadVehStock;
import com.transport.tracking.model.Trip;
import com.transport.tracking.model.Vehicle;
import com.transport.tracking.response.ResultTripVO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface TripRepository extends CrudRepository<Trip, BigDecimal> {

    public List<Trip> findBySite(String site);

    public List<Trip> findByCode(String code);

    public List<Trip> findBySiteAndDocdate(String site, Date date);

    public List<Trip> findBySiteAndDocdateOrderByTripCodeAsc(String site, Date date);

    public List<Trip> findByCodeAndDocdateOrderByTripsAsc(String code, Date date);

    public List<Trip> findBySiteInAndDocdateOrderByTripCodeAsc(List<String> sites, Date date);

    public List<Trip> findBySiteAndDocdateOrderByVrseqAsc(String site, Date date);


    public List<Trip> findByDocdate(Date date);

    public Trip findByTripCode(String tripCode);


    @Query(value="select top 1 * from PILOT.XX10TRIPS c where c.CODE = :veh and c.DOCDATE <= :date ORDER BY c.DOCDATE DESC,c.TRIPS DESC ",nativeQuery = true)
   public Trip findLatestDepartSites(@Param("veh") String veh,@Param("date") Date date);

    @Query(value="select distinct c.CODE,c.DOCDATE,c.ROWID from PILOT.XX10TRIPS c where c.SITE = ?1 and (c.DOCDATE between ?2 AND ?3)",nativeQuery = true)
    public List<Trip> getCodeAndDocdateOnly(String veh,Date sdate,Date edate);


    @Query("select DISTINCT new com.transport.tracking.response.ResultTripVO(c.code,GETDATE()) from  Trip c where c.site = ?1 and (c.docdate between ?2 AND ?3)")
    public List<ResultTripVO> getcustomCodeAndDocdateOnly(String veh, Date sdate, Date edate);

    @Query(value="select c from  Trip c where c.code = ?1 and c.docdate = CAST(?2 as timestamp without time zone) order by trips",nativeQuery = true)
    public List<Trip> getTripsByCodeAndDocdate1(String veh,String docdate);

  @Query(value="select * from PILOT.XX10TRIPS c where c.SITE IN (?1) and (c.DOCDATE between ?2 AND ?3)",nativeQuery = true)
    public List<Trip> getTripswithRangeAndSite(List<String> site,Date sdate,Date edate);



    @Query(value="select * from PILOT.XX10TRIPS c where c.CODE = ?1 and c.DOCDATE = ?2 ORDER BY c.TRIPS",nativeQuery = true)
    public List<Trip> getTripsByCodeAndDocdate(String veh,Date date);



    @Query(value="select * from PILOT.XX10TRIPS c where c.TRIPCODE LIKE 'VR-%' ORDER BY c.TRIPCODE",nativeQuery = true)
    public List<Trip> getVRTripcode();

    @Query(value = "SELECT COUNT(*) FROM PILOT.XX10TRIPS WHERE CODE = :code AND CAST(DOCDATE AS DATE) = CAST(:docDate AS DATE)", nativeQuery = true)
    int countByCodeAndDocdate(@Param("code") String code, @Param("docDate") Date docDate);













}
