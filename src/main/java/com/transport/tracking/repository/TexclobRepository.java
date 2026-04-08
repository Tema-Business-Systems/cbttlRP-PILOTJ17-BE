package com.transport.tracking.repository;

import com.transport.tracking.model.Texclob;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TexclobRepository extends CrudRepository<Texclob, String> {

    public List<Texclob> findAll();


    @Query("select c from Texclob c where c.code like 'SDH%' ORDER BY c.code DESC ")
    public List<Texclob> findByCodeToDelivery();

    @Query("select c from Texclob c where c.code like 'PRH%' ORDER BY c.code DESC ")
    public List<Texclob> findByCodeToPick();

    @Query("select c from Texclob c where c.code like 'XX10C%' ORDER BY c.code DESC ")
    public List<Texclob> findByCodeToReceipt();

    @Query("select c from Texclob c where c.code like 'BOL~%' ORDER BY c.code DESC ")
    public List<Texclob> findByCodeToBol();

}
