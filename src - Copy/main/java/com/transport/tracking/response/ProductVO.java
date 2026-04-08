package com.transport.tracking.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVO {

    private String productCode;
    private String productName;
    private String productCateg;
    private String quantity;
    private String uom;
    private String docLineNum;
    private String weight;
    private String weu;
    private String volume;
    private String vou;

}
