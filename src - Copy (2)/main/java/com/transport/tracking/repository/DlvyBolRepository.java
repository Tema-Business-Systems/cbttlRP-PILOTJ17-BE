package com.transport.tracking.repository;

import com.transport.tracking.model.DvlBol;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DlvyBolRepository extends CrudRepository<DvlBol, String> {

    public List<DvlBol> findAll();

    public DvlBol findByBolnum(String bolnum);

    public DvlBol findByDocnum(String docnum);



}
