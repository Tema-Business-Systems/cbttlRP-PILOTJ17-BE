package com.transport.tracking.repository;

import com.transport.tracking.model.LoadMsg;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoadMsgRepository extends CrudRepository<LoadMsg, String> {

    public List<LoadMsg> findAll();

    public LoadMsg findByDocnum(String docnum);


}
