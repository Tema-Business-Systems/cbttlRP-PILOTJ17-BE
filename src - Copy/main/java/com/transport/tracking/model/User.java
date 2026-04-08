package com.transport.tracking.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "XX10CUSERS")
public class User {

      @Id
      @Column(name= "UPDTICK_0")
      private int updtick;
      @Column(name= "XAUS_0")
      private String xlogin;
      @Column(name= "XPWSD_0")
      private String xpswd;
      @Column(name= "XAUSNA_0")
      private String xusrname;
      @Column(name= "XACT_0")
      private int xact;
      @Column(name= "CREDATTIM_0")
      private Date credattim;
      @Column(name= "UPDDATTIM_0")
      private Date upddattim;
      @Column(name= "AUUID_0")
      private byte[] auuid;
      @Column(name= "CREUSR_0")
      private String creusr;
      @Column(name= "UPDUSR_0")
      private String updusr;
      @Column(name= "ROWID")
      private BigDecimal rowid;

      public User() {
      }

      /*@ManyToMany(cascade = { CascadeType.ALL })
      @JoinTable(
              name = "XX10CUSRROL",
              joinColumns = { @JoinColumn(name = "XUSER_0") },
              inverseJoinColumns = { @JoinColumn(name = "XROLE_0") }
      )
      private Set<Role> roles = new HashSet<>();*/
}
