package com.transport.tracking.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "XTMSROUTECODE")
public class RouteCode implements Serializable {

    @Id
    @Column(name= "LANNUM_0")
    private String routeNo;
    @Column(name= "LANMES_0")
    private String routeDesc;
    @Column(name= "XSTYLE_0")
    private String bgColor;


}
