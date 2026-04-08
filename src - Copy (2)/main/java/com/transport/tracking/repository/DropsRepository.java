package com.transport.tracking.repository;

import com.transport.tracking.model.Drops;
import com.transport.tracking.model.LoadVehStock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface DropsRepository extends CrudRepository<Drops, String> {

    public List<Drops> findBySite(String site);

    public List<Drops> findBySiteAndDocdate(String site, Date date);

    public List<Drops> findByDocdateAndSiteIn(Date date,List<String> sites);

    public List<Drops> findByDocdate(Date date);

    public Drops findByDocnum(String docnum);


}
