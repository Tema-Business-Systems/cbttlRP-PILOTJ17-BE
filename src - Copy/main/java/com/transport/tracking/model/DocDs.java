package com.transport.tracking.model;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "XSCHDOCSD")
public class DocDs {

      @Id
      @Column(name= "ROWID")
      private String rowid;
      @Column(name= "SITE")
      private String site;
      @Column(name= "DOCNUM")
      private String docnum;
      @Column(name= "DOCDATE")
      private String docdate;
      @Column(name= "DOCTYPE")
      private String doctype;
      @Column(name= "PRODUCTCODE")
      private String prodcode;
      @Column(name= "PRODUCTNAME")
      private String prodname;
      @Column(name= "PRODUCTCATEG")
      private String prodcateg;
      @Column(name= "QUANTITY")
      private String qty;
      @Column(name= "UOM")
      private String uom;
      @Column(name= "DOCLINENO")
      private String doclineno;
      @Column(name= "WEIGHT")
      private String weight;
      @Column(name= "WEU")
      private String weu;
      @Column(name= "VOLUME")
      private String volume;
      @Column(name= "VOU")
      private String vou;


}
