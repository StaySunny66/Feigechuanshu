package cn.shilight.myapplication.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import cn.shilight.myapplication.data.MessageAllData;
import cn.shilight.myapplication.data.MessageListSql;
import cn.shilight.myapplication.data.UserSql;
import cn.shilight.myapplication.domain.Friend;
import cn.shilight.myapplication.domain.FriendList;

public class FriendDao {

    private Context context;

    public FriendDao(Context context) {
        this.context = context;
    }
    @SuppressLint("Range")


    public List<Friend> getAllUserMessage(){
        List<Friend> friendList = new LinkedList<Friend>();

        SQLiteDatabase sq = new UserSql(context,null,null,1).getReadableDatabase();
        Cursor cr = sq.rawQuery("SELECT * FROM user",null);
        Log.i("SQL", String.valueOf(cr.getColumnCount()));

        while(cr.moveToNext()){

            friendList.add(new Friend(cr.getString(cr.getColumnIndex("uid")),
                    cr.getString(cr.getColumnIndex("name")),
                    cr.getString(cr.getColumnIndex("words")),
                    Integer.parseInt(cr.getString(cr.getColumnIndex("tx")))
            ));
        }
        sq.close();
        return friendList;

    }

    @SuppressLint("Range")
    public List<FriendList> getAllUserList(){

        List<FriendList> friendList = new LinkedList<FriendList>();

        SQLiteDatabase sq = new UserSql(context,null,null,1).getReadableDatabase();


        Cursor cr = sq.rawQuery("SELECT * FROM user",null);

        Log.i("SQL", String.valueOf(cr.getColumnCount()));

        while(cr.moveToNext()){
            // Log.i("SQL","读取数据");

            friendList.add(new FriendList(cr.getString(cr.getColumnIndex("uid")),
                    cr.getString(cr.getColumnIndex("name")),
                    Integer.parseInt(cr.getString(cr.getColumnIndex("tx")))
            ));
        }
        sq.close();
        return friendList;

    }


    public boolean addUser(Friend friend){

        if(getUser(friend.getUid())!=null) return false;

        SQLiteDatabase sq = new UserSql(context,null,null,1).getReadableDatabase();
        String sql = "INSERT INTO user(uid,name,words,tx) values(?,?,?,?)";
        SQLiteStatement sqLiteStatement = sq.compileStatement(sql);

        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1, friend.getUid());
        sqLiteStatement.bindString(2, friend.getName());
        sqLiteStatement.bindString(3, friend.getWords());
        sqLiteStatement.bindString(4, String.valueOf(friend.getTx_id()));
        sqLiteStatement.executeInsert();

        sq.close();

        return true;

    }




    @SuppressLint("Range")
    public Friend getUser(String uid){

        SQLiteDatabase sq = new UserSql(context,null,null,1).getReadableDatabase();


        Cursor cr = sq.rawQuery("SELECT * FROM user WHERE uid = ?",new String[]{uid});
        Log.i("SQL", String.valueOf(cr.getColumnCount()));
        if(cr.moveToFirst()){
            // Log.i("SQL","读取数据");

           return new Friend(cr.getString(cr.getColumnIndex("uid")),
                    cr.getString(cr.getColumnIndex("name")),
                    cr.getString(cr.getColumnIndex("words")),
                    Integer.parseInt(cr.getString(cr.getColumnIndex("tx")))
           );
        }
        sq.close();
        return null;
    }


    @SuppressLint("Range")
    public List<FriendList> getUsersByName(String name){
        List<FriendList> friendList = new LinkedList<FriendList>();

        SQLiteDatabase sq = new UserSql(context,null,null,1).getReadableDatabase();

        Cursor cr = sq.rawQuery("SELECT * FROM user WHERE name LIKE ?",new String[]{"%"+name+"%"});

        Log.i("SQL", String.valueOf(cr.getColumnCount()));

        while(cr.moveToNext()){
            friendList.add(new FriendList(cr.getString(cr.getColumnIndex("uid")),
                    cr.getString(cr.getColumnIndex("name")),
                    Integer.parseInt(cr.getString(cr.getColumnIndex("tx")))
            ));
        }
        sq.close();

        return friendList;
    }
    @SuppressLint("Range")
    public boolean deleteFriend(String uid){

        SQLiteDatabase sq = new UserSql(context,null,null,1).getReadableDatabase();
        sq.execSQL(" DELETE FROM  user where uid = ?",new String[]{uid});
        sq.close();

        return true;
    }



}
