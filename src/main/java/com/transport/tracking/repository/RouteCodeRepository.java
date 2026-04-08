package com.transport.tracking.repository;

import com.transport.tracking.model.DvlBol;
import com.transport.tracking.model.RouteCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface RouteCodeRepository extends CrudRepository<RouteCode, String> {

    public List<RouteCode> findAll();

}
