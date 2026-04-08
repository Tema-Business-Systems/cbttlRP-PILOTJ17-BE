package com.transport.tracking.repository;

import com.transport.tracking.model.VehRouteDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VehRouteDetailRepository extends CrudRepository<VehRouteDetail, String> {

    public List<VehRouteDetail> findAll();

    public List<VehRouteDetail> findByXnumpc(String vrcode);
}
