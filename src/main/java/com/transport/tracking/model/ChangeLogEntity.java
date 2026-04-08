package com.transport.tracking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "X1CLVSTRNS")
public class ChangeLogEntity {


      @Id
      @Column(name= "ROWID")
      private BigDecimal rowid;
      @Column(name="XNUMPC_0")
      private String xnumpc;
      @Column(name="XDOCNUM_0")
      private String docnum;
      @Column(name="XTYPE_0")
      private int doctype;
      @Column(name="XOFLG_0")
      private int xoflg;
      @Column(name="XOLDVALUE_0")
      private String oldValue;
      @Column(name="XNEWVALUE_0")
      private String latestValue;



}
