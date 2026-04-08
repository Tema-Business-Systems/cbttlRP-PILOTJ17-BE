package com.transport.tracking.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


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
