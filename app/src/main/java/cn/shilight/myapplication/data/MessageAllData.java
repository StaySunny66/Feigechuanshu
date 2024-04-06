package cn.shilight.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MessageAllData extends SQLiteOpenHelper {
    public MessageAllData(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "messageAll.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("SQL","创建数据库");

        sqLiteDatabase.execSQL("CREATE TABLE messageAll(fromuid TEXT,forUid TEXT,content TEXT ,MessageType INTEGER,time INTEGER)");




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop TABLE messageAll");

        sqLiteDatabase.execSQL("CREATE TABLE messageAll(fromuid TEXT,forUid TEXT,content TEXT ,MessageType INTEGER,time INTEGER)");


    }
}
