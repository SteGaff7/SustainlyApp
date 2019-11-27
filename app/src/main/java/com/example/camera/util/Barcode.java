package com.example.camera.util;

/**
 * Creates Barcode objects
 */
public class Barcode {


    private Product product;
    //Barcode number
    private String code;
    //represents whether the API can find the barcode
    private Integer status;
    //represents whether the barcode is in the local database
    private Boolean inDB;


    public Barcode() {
        this.inDB = false;
    }

    /**
     * Getter for Product represented by a specific barcode
     *
     * @return {@link Product}
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Setter for Product represented by a specific barcode
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Getter for status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Setter for status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Getter for code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter for code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter for inDB
     */
    public Boolean getInDB() {
        return inDB;
    }

    /**
     * Setter for inDB
     */
    public void setInDB(Boolean inDB) {
        this.inDB = inDB;
    }
}
