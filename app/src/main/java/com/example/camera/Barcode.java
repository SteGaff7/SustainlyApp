package com.example.camera;

import android.content.Intent;

public class Barcode {
    private Product product;
    private Integer status;


    public Barcode(Product product, Integer status){
        this.product=product;
        this.status = status;

    }
    public Barcode() {

    }
    public Product getProduct(){
        return product;
    }

    public Integer getStatus(){
        return status;
    }


}
