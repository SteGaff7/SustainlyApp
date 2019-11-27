package com.example.camera.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.camera.Barcode;
import com.example.camera.Product;
import com.example.camera.data.ProductContract.ProductEntry;
import com.example.camera.data.BarcodeDBHandler;

import java.util.ArrayList;
import java.util.List;


public class ProductOperations {
    public static final String LOGTAG = "EMP_MNGMNT_SYS";

    // Database helper that will provide us access to the database
    private BarcodeDBHandler dbhandler;

    // String representation of database columns for queries
    private static final String[] allColumns = {
            ProductEntry.COLUMN_BARCODE,
            ProductEntry.COLUMN_STATUS,
            ProductEntry.COLUMN_NAME,
            ProductEntry.COLUMN_MANUF_LOCATION,
            ProductEntry.COLUMN_INGREDIENTS,
            ProductEntry.COLUMN_CATEGORIES

    };


    public ProductOperations(Context context) {

        dbhandler = new BarcodeDBHandler(context);
    }

    /**
     * Adds a barcode and related information to the database
     *
     * @param barcode of product
     */
    public void addBarcode(Barcode barcode) {

        SQLiteDatabase database=dbhandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_BARCODE, barcode.getCode());
        values.put(ProductEntry.COLUMN_STATUS, barcode.getStatus());
        values.put(ProductEntry.COLUMN_NAME, barcode.getProduct().getProductName());
        values.put(ProductEntry.COLUMN_MANUF_LOCATION, barcode.getProduct().getManufacturingPlaces());
        values.put(ProductEntry.COLUMN_INGREDIENTS, barcode.getProduct().getOrigins());
        values.put(ProductEntry.COLUMN_CATEGORIES, barcode.getProduct().getCategory());

        database.insert(ProductEntry.TABLE_PRODUCTS, null, values);

    }

    /**
     * Queries the database products table to retrieve a barcode
     *
     * @param message containing column and query information
     * @return {@link Barcode} from database
     */
    public Barcode getBarcode(String message) {
        // Create and/or open a database to read from it
        SQLiteDatabase database=dbhandler.getReadableDatabase();
        // The values for the WHERE clause
        String[] whereArgs = new String[]{
                message
        };
        // Perform a query on the table
        Cursor cursor = database.query(
                ProductEntry.TABLE_PRODUCTS,
                allColumns,
                "barcode" + "=?",
                whereArgs,
                null,
                null,
                null,
                null);

        //Objects to be returned
        Product product = new Product();
        Barcode returnedBarcode = new Barcode();

        //if item is in the database already
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();

            returnedBarcode.setCode(cursor.getString(0));
            returnedBarcode.setStatus(cursor.getInt(1));
            returnedBarcode.setInDB(true);

            product.setProductName(cursor.getString(2));
            product.setManufacturingPlaces(cursor.getString(3));
            product.setOrigins(cursor.getString(4));
            product.setCategory(cursor.getString(5));

            returnedBarcode.setProduct(product);
            cursor.close();

        }
        database.close();
        return returnedBarcode;
    }

    // Getting single Barcode
    public ArrayList<Product> filterCategory(String message) {
        // Create and/or open a database to read from it
        SQLiteDatabase database=dbhandler.getReadableDatabase();
        String whereClause = "category = ?";
        String[] whereArgs = new String[]{
                message};
        // Perform a query on the table
        Cursor cursor = database.query(
                ProductEntry.TABLE_PRODUCTS,
                allColumns,
                whereClause,
                whereArgs,
                null,
                null,
                null,
                null);

        //Objects to be returned
        ArrayList<Product> products = new ArrayList<>(cursor.getCount());
        //if item is in the database already
        if (cursor != null && cursor.getCount()>0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Product product = new Product();
                product.setProductName(cursor.getString(2));
                product.setManufacturingPlaces(cursor.getString(3));
                product.setOrigins(cursor.getString(4));
                product.setCategory(cursor.getString(5));
                products.add(product);
            }
            cursor.close();
            database.close();
        }

        return products;
    }
}
