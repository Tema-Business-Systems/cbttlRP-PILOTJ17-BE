package com.transport.tracking.repository;

import com.transport.tracking.model.Drops;
import com.transport.tracking.model.PickUP;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface PickupRepository extends CrudRepository<PickUP, String>  {

    public List<PickUP> findBySite(String site);

    public List<PickUP> findBySiteAndDocdate(String site, Date date);

    public List<PickUP> findByDocdateAndSiteIn(Date date,List<String> sites);

    public List<PickUP> findByDocdate(Date date);


    public PickUP findByDocnum(String docnum);
}
