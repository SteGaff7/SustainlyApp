package com.example.camera;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.lang.Object;




public class ProductOperations {
    public static final String LOGTAG = "EMP_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            BarcodeDBHandler.COLUMN_BARCODE,
            BarcodeDBHandler.COLUMN_STATUS,
            BarcodeDBHandler.COLUMN_NAME,
            BarcodeDBHandler.COLUMN_MANUF_LOCATION,
            BarcodeDBHandler.COLUMN_INGREDIENTS

    };

    public ProductOperations(Context context) {
        dbhandler = new BarcodeDBHandler(context);
    }

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        database = dbhandler.getWritableDatabase();


    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }

    public static Barcode addBarcode(Barcode barcode) {
        ContentValues values = new ContentValues();
        values.put(BarcodeDBHandler.COLUMN_BARCODE, barcode.getCode());
        values.put(BarcodeDBHandler.COLUMN_STATUS, barcode.getStatus());
        values.put(BarcodeDBHandler.COLUMN_NAME, barcode.getProduct().getProductName());
        values.put(BarcodeDBHandler.COLUMN_MANUF_LOCATION, barcode.getProduct().getManufacturingPlaces());
        values.put(BarcodeDBHandler.COLUMN_INGREDIENTS, barcode.getProduct().getOrigins());
        return barcode;

    }

    // Getting single Barcode
    public Barcode getBarcode(String message) {
        database=dbhandler.getWritableDatabase();
        String[] whereArgs = new String[]{
                message
        };
        Cursor cursor = database.query(BarcodeDBHandler.TABLE_PRODUCTS, allColumns, "barcode" + "=?", whereArgs, null, null, null, null);
        Log.d("cursor", DatabaseUtils.dumpCursorToString(cursor));

        Product product = new Product();
        Barcode returnedBarcode = new Barcode();

        //needed an extra check
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();

            returnedBarcode.setCode(cursor.getString(0));
            returnedBarcode.setStatus(cursor.getInt(1));
            returnedBarcode.setInDB(true);

            product.setProductName(cursor.getString(2));
            product.setManufacturingPlaces(cursor.getString(3));
            product.setOrigins(cursor.getString(4));

            returnedBarcode.setProduct(product);

        }
//        else{
//
//            returnedBarcode.setCode("null");
//            //returnedBarcode.setStatus("null");
//
//            product.setProductName("null");
//            product.setManufacturingPlaces("null");
//            product.setOrigins("null");
//
//        }

        //returnedBarcode.setProduct(product);
        database.close();
        return returnedBarcode;
    }


// close cursor



    public int count() {

        SQLiteDatabase db = dbhandler.getWritableDatabase();

        String sql = "SELECT * FROM products";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }
}
