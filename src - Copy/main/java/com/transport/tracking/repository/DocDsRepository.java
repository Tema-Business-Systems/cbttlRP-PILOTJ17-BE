package com.transport.tracking.repository;

import com.transport.tracking.model.DocDs;
import com.transport.tracking.model.Docs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface DocDsRepository extends CrudRepository<DocDs, String> {

    public List<DocDs> findBySite(String site);

    public List<DocDs> findByDocnum(String docnum);

    @Query(value="select * from PILOT.XSCHDOCSD c where c.DOCNUM = ?1 ",nativeQuery = true)
    public List<DocDs> getprodsbyDocnum(String docnum);




}
