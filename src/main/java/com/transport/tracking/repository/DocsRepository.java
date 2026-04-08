package com.transport.tracking.repository;

import com.transport.tracking.model.Docs;
import com.transport.tracking.model.Trip;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface DocsRepository extends CrudRepository<Docs, String> {

    public List<Docs> findBySite(String site);

    public List<Docs> findBySiteAndDocdate(String site, Date date);

    public List<Docs> findByDocdateAndSiteIn(Date date,List<String> sites);

    public List<Docs> findByDocdate(Date date);

    public Docs findByDocnum(String docnum);


    @Query(value="select * from PILOT.XSCHDOCS c where c.SITE IN (?1) and (c.DOCDATE between ?2 AND ?3) ORDER BY c.DOCDATE ,c.TRIPNO , c.SEQ",nativeQuery = true)
    public List<Docs> getDocswithRangeAndSite(List<String> site, Date sdate, Date edate);

    @Query(value="select * from PILOT.XSCHDOCS c where  c.DOCDATE between ?1 AND ?2 ORDER BY c.DOCDATE,c.TRIPNO , c.SEQ",nativeQuery = true)
    public List<Docs> getDocswithRange(Date sdate, Date edate);

    @Query(value="select d.*, x.PILOTUCTCODE, x.PILOTUCTNAME,x.PILOTUCTCATEG, x.QUANTITY, x.UOM, x.DOCLINENO \n" +
            " from PILOT.XSCHDOCS d left join PILOT.XSCHDOCSD x on d.DOCNUM = x.DOCNUM d.SITE IN (?1) AND (d.DOCDATE BETWEEN  ?2 AND ?3) ORDER BY d.TRIPNO, d.SEQ  ASC",nativeQuery = true)
    public List<Docs> getDocswithRangeAndSite2(List<String> site, Date sdate, Date edate);


}
