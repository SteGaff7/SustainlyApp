package com.example.camera;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class BarcodeDBHandler extends SQLiteOpenHelper{


    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MANUF_LOCATION = "manufacturingLocation";
    public static final String COLUMN_INGREDIENTS= "ingredientsOrigin";



    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTS+ " (" +
                    COLUMN_BARCODE + " TEXT PRIMARY KEY, " +
                    COLUMN_STATUS  + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_MANUF_LOCATION + " TEXT, " +
                    COLUMN_INGREDIENTS+ " TEXT " +
                    ")";




    public BarcodeDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL("INSERT INTO "+ TABLE_PRODUCTS+  " VALUES('123','1','Sushi','Japan', 'Japan')");
        db.execSQL("INSERT INTO "+ TABLE_PRODUCTS + " VALUES('456','1','Steak','Ireland', 'Ireland')");
        db.execSQL("INSERT INTO "+ TABLE_PRODUCTS + " VALUES('789','1','Cheese','France', 'Spain')");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PRODUCTS);
        db.execSQL(TABLE_CREATE);

    }
}
