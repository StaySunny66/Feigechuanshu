package cn.shilight.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MessageListSql extends SQLiteOpenHelper {
    public MessageListSql(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "messageList.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("SQL","创建数据库");

        sqLiteDatabase.execSQL("CREATE TABLE messageList(uid TEXT PRIMARY KEY ,name TEXT ,lasttime INTEGER,content TEXT,tx TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop TABLE messageList");

        sqLiteDatabase.execSQL("CREATE TABLE messageList(uid TEXT PRIMARY KEY ,name TEXT ,lasttime INTEGER,content TEXT,tx TEXT)");


    }
}
