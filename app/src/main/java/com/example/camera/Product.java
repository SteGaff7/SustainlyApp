package com.example.camera;

public class Product {
    private String product_name_en;
    private String manufacturing_places;
    private String origins;



    public Product(String product_name_en, String manufacturing_places, String origins){
        this.product_name_en=product_name_en;
        this.manufacturing_places=manufacturing_places;
        this.origins=origins;

    }
    public Product() {

    }

    public String getProductName(){

        return product_name_en;
    }

    public String getManufacturingPlaces(){

        return manufacturing_places;
    }

    public String getOrigins(){

        return origins;
    }



}