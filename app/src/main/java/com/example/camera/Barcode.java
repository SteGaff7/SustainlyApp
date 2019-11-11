package com.example.camera;

import android.content.Intent;

public class Barcode {
    private Product product;
    private String code;
    private Integer status;
    private Boolean inDB;


    public Barcode(Product product, Integer status, String code){
        this.product = product;
        this.status = status;
        this.inDB = false;
        this.code = code;
    }

    public Barcode() {
        this.inDB = false;
    }

    public Product getProduct(){

        return product;
    }

    public void setProduct(Product product){

        this.product= product;
    }

    public Integer getStatus(){

        return status;
    }

    public void setStatus(Integer status){

        this.status = status;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public Boolean getInDB(){

        return inDB;
    }

    public void setInDB(Boolean inDB) {

        this.inDB = inDB;
    }
}
