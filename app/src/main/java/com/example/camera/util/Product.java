package com.example.camera.util;

/**
 * Creates Product objects
 */
public class Product {
    private String product_name_en;
    private String manufacturing_places;
    private String origins;
    private String category;


    public Product(String product_name_en, String manufacturing_places, String origins, String category) {
        this.product_name_en = product_name_en;
        this.manufacturing_places = manufacturing_places;
        this.origins = origins;
        this.category = "Unknown";
    }

    public String getProductName() {

        return product_name_en;
    }

    public void setProductName(String product_name_en) {

        this.product_name_en = product_name_en;
    }

    public String getManufacturingPlaces() {

        return manufacturing_places;
    }

    public void setManufacturingPlaces(String manufacturing_places) {

        this.manufacturing_places = manufacturing_places;
    }

    public String getOrigins() {

        return origins;
    }

    public void setOrigins(String origins) {

        this.origins = origins;
    }


    public String getCategory() {

        return category;
    }

    public void setCategory(String category) {

        this.category = category;
    }

}