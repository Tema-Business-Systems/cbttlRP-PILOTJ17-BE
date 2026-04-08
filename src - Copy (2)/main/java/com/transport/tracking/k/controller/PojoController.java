package com.transport.tracking.k.controller;

import com.transport.tracking.k.service.CreatePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PojoController {

    @Autowired
    private CreatePojo createPojo;

    @GetMapping("/pojo/{tableName}/{className}")
    public void createTable(@PathVariable (name = "tableName") String tableName,
                            @PathVariable (name = "className") String className) {
        //createPojo.generatePojo(tableName, className);
    }
}
