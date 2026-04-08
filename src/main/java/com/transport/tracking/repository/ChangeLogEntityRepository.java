package com.transport.tracking.repository;

import com.transport.tracking.model.ChangeLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeLogEntityRepository extends CrudRepository<ChangeLogEntity, String> {

    public List<ChangeLogEntity> findAll();

    public List<ChangeLogEntity> findByXnumpc(String vrcode);

    List<ChangeLogEntity> findByXoflg(Integer xoflg);
}
