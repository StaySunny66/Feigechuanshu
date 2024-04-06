package cn.shilight.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TxDataSql extends SQLiteOpenHelper {


    public TxDataSql(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "tx.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE txdata(uid TEXT PRIMARY KEY ,txdata BLOB)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE txdata");

        sqLiteDatabase.execSQL("CREATE TABLE txdata(uid TEXT PRIMARY KEY ,txdata BLOB)");


    }
}
