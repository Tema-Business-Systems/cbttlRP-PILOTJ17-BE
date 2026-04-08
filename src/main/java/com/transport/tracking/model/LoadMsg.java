package com.transport.tracking.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@Entity
      @Table(name = "XTMSLOADERINST")
public class LoadMsg {

      @Id
      @Column(name="DOCNUM")
      private String docnum;
      @Column(name="DOCDATE")
      private String docdate;
      @Column(name="DOCTYPE")
      private String doctyp;
      @Column(name="BPCORD")
      private String bpcord;
      @Column(name="LOADTXT")
      private String loadtxt;
}
