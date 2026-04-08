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
      @Table(name = "XTMSPTHEADER")
public class Texclob {

      @Id
      @Column(name="CODE_0")
      private String code;
      @Column(name="TEXTE_0")
      private String text;

}
