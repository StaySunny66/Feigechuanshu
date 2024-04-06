package cn.shilight.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserSql extends SQLiteOpenHelper {


    public UserSql(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "user.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE user(uid TEXT PRIMARY KEY ,name TEXT ,words TEXT,tx TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE user");

        sqLiteDatabase.execSQL("CREATE TABLE user(uid TEXT PRIMARY KEY ,name TEXT ,words TEXT,tx TEXT)");


    }
}
