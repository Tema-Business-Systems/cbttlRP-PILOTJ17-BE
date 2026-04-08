package com.transport.tracking.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
