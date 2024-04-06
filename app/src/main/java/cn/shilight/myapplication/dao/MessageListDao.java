package cn.shilight.myapplication.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import cn.shilight.myapplication.data.MessageListSql;
import cn.shilight.myapplication.domain.Friend;
import cn.shilight.myapplication.domain.Message;
import cn.shilight.myapplication.message.MessagObtian;

public class MessageListDao {

    static List<Message> messageList;

    private  Context context;



    public MessageListDao(Context context){
        /// Load form sql
        messageList = new LinkedList<Message>();
        this.context = context;

    }

    @SuppressLint("Range")
    public List<Message> getMessageList(){

        messageList.clear();
        SQLiteDatabase sq = new MessageListSql(context,null,null,1).getReadableDatabase();


        Cursor cr = sq.rawQuery("SELECT * FROM messageList",null);

        Log.i("SQL", String.valueOf(cr.getColumnCount()));

        while(cr.moveToNext()){
           // Log.i("SQL","读取数据");
            messageList.add(new Message(Integer.parseInt(cr.getString(cr.getColumnIndex("tx"))), cr.getString(cr.getColumnIndex("name")), cr.getString(cr.getColumnIndex("content")),
                    cr.getString(cr.getColumnIndex("lasttime")) ,cr.getString(cr.getColumnIndex("uid"))));
        }
        sq.close();
        return messageList;
    }


    public boolean addMessageListUser(MessagObtian Message){

        SQLiteDatabase sq = new MessageListSql(context,null,null,1).getWritableDatabase();

        // messageList 中存放的应该是 id 为 发送者的 uid
        Cursor cr = sq.rawQuery("select * from messageList where uid = ?",new String[]{String.valueOf(Message.getFromUid())});
        if(cr.moveToFirst()){
            // 已经存在的数据
            // 把数据更新
            String sql = "UPDATE messageList SET lasttime = ? , content = ?  WHERE uid = ?";
            SQLiteStatement sqLiteStatement = sq.compileStatement(sql);

            Friend  myfriend = new FriendDao(context).getUser(Message.getFromUid());
            if(myfriend == null) {
                Log.i("MessageInsert","接收到的消息好友不存在于数据库放弃存储");
                return false;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm"); //设置时间格式
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区

            formatter.format(new Date());

            sqLiteStatement.clearBindings();
            sqLiteStatement.bindString(1,formatter.format(new Date()));
            sqLiteStatement.bindString(2,Message.getContent());
            sqLiteStatement.bindString(3,Message.getFromUid());
            sqLiteStatement.executeInsert();

            sq.close();

            Log.i("SQL","已经存在的用户");

        }else {
            //不存在的数据  添加进去
            Log.i("SQL","不存存在的用户");
            // 把数据插入
            String sql = "INSERT INTO messageList(uid,name,lasttime,content,tx) values(?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = sq.compileStatement(sql);

            Friend  myfriend = new FriendDao(context).getUser(Message.getFromUid());
            if(myfriend == null) {
                Log.i("MassageInsert","接收到的消息好友不存在于数据库放弃存储");
                return false;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm"); //设置时间格式
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区

            formatter.format(new Date());

            sqLiteStatement.clearBindings();
            sqLiteStatement.bindString(1,Message.getFromUid());
            sqLiteStatement.bindString(2,myfriend.getName());
            sqLiteStatement.bindString(3,formatter.format(new Date()));
            sqLiteStatement.bindString(4,Message.getContent());
            sqLiteStatement.bindString(5,String.valueOf(myfriend.getTx_id()));

            sqLiteStatement.executeInsert();

            sq.close();


        }

        sq.close();

        return true;



    }

    public boolean delect(int poestion){

        if(messageList.get(poestion)==null) return false;
        String uid = messageList.get(poestion).getUid();
        SQLiteDatabase sq = new MessageListSql(context,null,null,1).getReadableDatabase();
        sq.execSQL(" DELETE FROM  messageList where uid = ?",new String[]{uid});
        sq.close();
        messageList.remove(poestion);

        return true;

    }

    public boolean delectByFromUid(String formUid){

        SQLiteDatabase sq = new MessageListSql(context,null,null,1).getReadableDatabase();
        sq.execSQL(" DELETE FROM  messageList where uid = ?",new String[]{formUid});
        sq.close();
        return true;

    }



    public boolean dropAll(){

        SQLiteDatabase sq = new MessageListSql(context,null,null,1).getReadableDatabase();

        sq.execSQL(" DELETE FROM  messageList");

        sq.close();

        return true;

    }





}
