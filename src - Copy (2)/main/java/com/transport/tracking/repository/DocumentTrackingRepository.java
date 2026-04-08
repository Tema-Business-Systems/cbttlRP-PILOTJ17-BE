package com.transport.tracking.repository;

import com.transport.tracking.model.DocReportTrack;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTrackingRepository extends CrudRepository<DocReportTrack, Integer> {

    List<DocReportTrack> findBySite(String site);

    public List<DocReportTrack> findAll();

    List<DocReportTrack> findBySiteAndDocdate(String site, String date);

    @Query(value="select * from PILOT.XTMSDOCREPORTS c where c.SITE = ?1 and (c.DOCDATE between ?2 AND ?3)",nativeQuery = true)
    public List<DocReportTrack> getDocReportBySiteAndDateRange(String site, String sdate, String edate);



}
