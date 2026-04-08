package com.transport.tracking.repository;

import com.transport.tracking.model.PlanChalanD;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanChalanDRepository extends CrudRepository<PlanChalanD, String> {

    public List<PlanChalanD> findAll();

    public PlanChalanD findByVrCodeAndDocnum(String vrcode, String docnum);


}
