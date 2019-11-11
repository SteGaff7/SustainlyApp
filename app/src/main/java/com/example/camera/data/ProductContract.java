package com.example.camera.data;

import android.provider.BaseColumns;


public final class ProductContract {
    // To prevent someone from accidentally instantiating the contract class.
    private ProductContract() {
    }

    /**
     * Inner class that defines constant values for the barcode table.
     * Each entry in the table represents a single pet.
     */
    public static final class ProductEntry implements BaseColumns {

        /** Name of database table for barcodes */
        public static final String TABLE_PRODUCTS = "products";
        /**
         * Unique ID number for the barcode.
         *
         * Type: STRING
         */
        public static final String COLUMN_BARCODE = "barcode";
        /**
         * Unique ID number for the barcode.
         *
         * Type: STRING
         */
        public static final String COLUMN_STATUS = "status";
        /**
         * Name of the product.
         *
         * Type: STRING
         */
        public static final String COLUMN_NAME = "name";
        /**
         * Location of Product Manufactoring Company
         *
         * Type: STRING
         */
        public static final String COLUMN_MANUF_LOCATION = "manufacturingLocation";
        /**
         * Location of Product Ingredients
         *
         * Type: STRING
         */
        public static final String COLUMN_INGREDIENTS = "ingredientsOrigin";

    }
}