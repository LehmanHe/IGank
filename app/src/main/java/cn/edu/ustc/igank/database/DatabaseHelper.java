package cn.edu.ustc.igank.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.edu.ustc.igank.database.table.GirlTable;

/**
 * Created by Lehman on 2016-06-25.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private  static final String DB_NAME= "IGank0";
    private static  DatabaseHelper instance = null;
    private static final int DB_VERSION = 1;
    public static final String DELETE_TABLE_DATA = "delete from ";
    public static final String DROP_TABLE = "drop table if exists ";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized DatabaseHelper instance(Context context){
        if(instance == null){
            instance = new DatabaseHelper(context,DB_NAME,null,DB_VERSION);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(GirlTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
