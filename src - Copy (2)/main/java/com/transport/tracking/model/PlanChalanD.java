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
@Table(name = "XX10CPLANCHD")
public class PlanChalanD implements Serializable {

    @Column(name= "XNUMPC_0")
    private String vrCode;

    @Column(name= "XDLV_STATUS_0")
    private int status;

    @Column(name= "DEPARTTIME_0")
    private String adeptime;

    @Column(name= "SDHNUM_0")
    private String docnum;


    @Column(name= "ARRIVETIME_0")
    private String areturntime;

    @Column(name= "ADEPARTTIME_0")
    private String aadeptime;


    @Column(name= "AARRIVETIME_0")
    private String aareturntime;

    @Id
    @Column(name= "ROWID")
    private long rowid;

}
