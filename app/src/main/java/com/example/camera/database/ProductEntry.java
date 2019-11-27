package com.example.camera.database;

import android.provider.BaseColumns;

/**
 * A utility class to hold database strings
 */
final class ProductEntry implements BaseColumns {

        // Name of database table for barcodes
        static final String TABLE_PRODUCTS = "products";

        // Unique ID number for the barcode.
        static final String COLUMN_BARCODE = "barcode";

        // Indicates whether an item is in the database
        static final String COLUMN_STATUS = "status";

        // Name of the product.
        static final String COLUMN_NAME = "name";

        //Location of Product Manufacturing Company
        static final String COLUMN_MANUF_LOCATION = "manufacturingLocation";

        // Location of Product's Ingredients
        static final String COLUMN_INGREDIENTS = "ingredientsOrigin";

        //Category of the product e.g. beverage, snack
        static final String COLUMN_CATEGORIES = "category";
}
