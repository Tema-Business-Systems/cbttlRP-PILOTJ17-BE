package com.transport.tracking.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "XX10CPLANCHA")
public class PlanChalanA implements Serializable {

    @Column(name= "XNUMPC_0")
    private String vrCode;

    @Column(name= "XVALID_0")
    private int valid;

    @Column(name= "AHEUDEP_0")
    private String aadeptime;


    @Column(name= "AHEUARR_0")
    private String aareturntime;


    @Column(name= "HEUDEP_0")
    private String adeptime;

    @Column(name= "HEUARR_0")
    private String areturntime;

    @Id
    @Column(name= "ROWID")
    private long rowid;

}
