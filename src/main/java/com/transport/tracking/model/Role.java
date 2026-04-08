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
@Table(name = "XX10CROL")
public class Role {

      @Id
      @Column(name= "UPDTICK_0")
      private int updtick;
      @Column(name= "XROLID_0")
      private int xrolid;
      @Column(name= "XDESC_0")
      private String xdesc;
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

      /*@ManyToMany(mappedBy = "roles")
      private Set<User> users = new HashSet<>();*/
}
