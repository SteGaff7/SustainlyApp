package com.example.camera.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.camera.data.ProductContract.ProductEntry;


public class BarcodeDBHandler extends SQLiteOpenHelper{

    public static final String LOGTAG = "EMP_MNGMNT_SYS";

    /** Name of the database file */
    private static final String DATABASE_NAME = "products.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 2;

    /**
     * Constructs a new instance of {@link BarcodeDBHandler}.
     *
     * @param context of the app
     */

    public BarcodeDBHandler(Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    /**
     * Called to create a database locally.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ProductEntry.TABLE_PRODUCTS + " (" +
                ProductEntry.COLUMN_BARCODE + " TEXT PRIMARY KEY, " +
                ProductEntry.COLUMN_STATUS + " TEXT, " +
                ProductEntry.COLUMN_NAME + " TEXT, " +
                ProductEntry.COLUMN_MANUF_LOCATION + " TEXT, " +
                ProductEntry.COLUMN_INGREDIENTS + " TEXT, " +
                ProductEntry.COLUMN_CATEGORIES + " TEXT " +
                ")");

        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('4311501606704','1','Steak Burger','France', 'France, Germany', 'Meat')");
        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('00767781','1','Chiplote chili chicken jerky','United Kingdom', 'France, United Kingdom', 'Meat')");

        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('3276557087833','1','Pain au mais','France', 'France', 'Bread')");
        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('5099630250997','1','Brown soda bread','Ireland', 'Ireland, United Kingdom', 'Bread')");

        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('3590941000926','1','Carrots','France', 'France', 'Vegetable')");


        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('0035826086433','1','Granny Smith Apple','United States', 'United States', 'Fruit')");

        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('8715700422718','1','Tomato Ketchup - Heinz','United Kingdom', 'United Kingdom, France', 'Condiments')");

        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('5390248520015','1','Keoghs Salt & Vinegar Crisps','Ireland', 'Ireland, United Kingdom', 'Snacks')");


        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('5011007003005','1','Jameson Irish Whiskey','Ireland', 'Ireland', 'Beverages')");
        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('4053400211411','1','Irish Red Ale - Kilkenny','Ireland', 'Ireland', 'Beverages')");

        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('8076800195057','1','Spaghetti - Barilla','Italy', 'Italy', 'Pasta')");

        db.execSQL("INSERT INTO " + ProductEntry.TABLE_PRODUCTS + " VALUES('4001724819905','1','Ristorante: Vegetarian Pizza','Germany', 'Italy, Germany', 'Pizza')");
    }

    /**
     * Updates the local database when schema is changes.
     *
     * @param db instance
     * @param oldVersion previous version of database version
     * @param newVersion latest version of database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ProductEntry.TABLE_PRODUCTS);
        onCreate(db);

    }
}
