package com.transport.tracking.repository;

import com.transport.tracking.model.Equipment;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface EquipmentRepository extends CrudRepository<Equipment, BigDecimal> {

    public List<Equipment> findByXfcy(String site);

    public List<Equipment> findByXfcyIn(List<String> site);
}
