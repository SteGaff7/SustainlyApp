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
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link BarcodeDBHandler}.
     *
     * @param context of the app
     */

    public BarcodeDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL("INSERT INTO "+ ProductEntry.TABLE_PRODUCTS+  " VALUES('123','1','Sushi','Japan', 'Japan')");
        db.execSQL("INSERT INTO "+ ProductEntry.TABLE_PRODUCTS + " VALUES('456','1','Steak','Ireland', 'Ireland')");
        db.execSQL("INSERT INTO "+ ProductEntry.TABLE_PRODUCTS + " VALUES('789','1','Cheese','France', 'Spain')");
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ProductEntry.TABLE_PRODUCTS);
        db.execSQL(TABLE_CREATE);

    }
    private static final String TABLE_CREATE =
            "CREATE TABLE " + ProductEntry.TABLE_PRODUCTS + " (" +
                    ProductEntry.COLUMN_BARCODE + " TEXT PRIMARY KEY, " +
                    ProductEntry.COLUMN_STATUS + " TEXT, " +
                    ProductEntry.COLUMN_NAME + " TEXT, " +
                    ProductEntry.COLUMN_MANUF_LOCATION + " TEXT, " +
                    ProductEntry.COLUMN_INGREDIENTS + " TEXT " +
                    ")";
}