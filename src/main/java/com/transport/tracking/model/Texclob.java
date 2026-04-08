package com.transport.tracking.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;


@Getter
@Setter
@Entity
      @Table(name = "XTMSPTHEADER")
public class Texclob {

      @Id
      @Column(name="CODE_0")
      private String code;
      @Column(name="TEXTE_0")
      private String text;

}
