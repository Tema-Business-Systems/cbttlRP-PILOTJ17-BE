package com.transport.tracking.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
      @Table(name = "XTMSDLVBOL")
public class DvlBol {

      @Id
      @Column(name="BOLNUM")
      private String bolnum;
      @Column(name="DOCDATE")
      private String docdate;
      @Column(name="DOCNUM")
      private String docnum;
      @Column(name="BLLINK")
      private String bllink;
      @Column(name="BLHEADER")
      private String blheader;
}
