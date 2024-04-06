package cn.shilight.myapplication.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import cn.shilight.myapplication.data.MessageAllData;
import cn.shilight.myapplication.domain.MessageView;

public class MessageAllDao {
    //// (fromuid TEXT,forUid TEXT,content TEXT ,MessageType INTEGER,content TEXT,time TEXT)
    private Context context;

    public MessageAllDao(Context context) {
        this.context = context;
    }
    @SuppressLint("Range")


        public List<MessageView> getAllMessage(){
        List<MessageView> messageViewList = new LinkedList<MessageView>();

        SQLiteDatabase sq = new MessageAllData(context,null,null,1).getReadableDatabase();

        Cursor cr = sq.rawQuery("SELECT * FROM messageAll",null);

        Log.i("SQL", String.valueOf(cr.getColumnCount()));

        while(cr.moveToNext()){

            messageViewList.add(new MessageView(cr.getString(cr.getColumnIndex("content")),
                    cr.getString(cr.getColumnIndex("fromuid")),
                    cr.getString(cr.getColumnIndex("forUid")),
                    cr.getInt(cr.getColumnIndex("time")),
                    cr.getInt(cr.getColumnIndex("MessageType"))
            ));
        }
        sq.close();
        return messageViewList;

    }


    /// 主要的方法 实现 获取聊天框的内容
    @SuppressLint("Range")
    public List<MessageView> getMessagesByFromUid(String fromUid){

            List<MessageView> messageViewList = new LinkedList<MessageView>();

            SQLiteDatabase sq = new MessageAllData(context,null,null,1).getReadableDatabase();

            // 我们两个的对话框 显示消息 应该是 来自他的消息 和 我发送给他的

            Cursor cr = sq.rawQuery("SELECT * FROM messageAll where fromUid = ? ORDER BY time ",new String[]{fromUid});

            Log.i("SQL", String.valueOf(cr.getColumnCount()));

            while(cr.moveToNext()){

                messageViewList.add(new MessageView(cr.getString(cr.getColumnIndex("content")),
                        cr.getString(cr.getColumnIndex("fromuid")),
                        cr.getString(cr.getColumnIndex("forUid")),
                        cr.getInt(cr.getColumnIndex("time")),
                        cr.getInt(cr.getColumnIndex("MessageType"))
                ));
            }
            sq.close();
            return messageViewList;

    }


    public boolean saveMessage(MessageView messageView){


        SQLiteDatabase sq = new MessageAllData(context,null,null,1).getReadableDatabase();
        String sql = "INSERT INTO messageAll(fromuid ,forUid ,content  ,MessageType ,content ,time ) values(?,?,?,?,?,?)";
        SQLiteStatement sqLiteStatement = sq.compileStatement(sql);

        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1, messageView.getFromUid());
        sqLiteStatement.bindString(2, messageView.getForUid());
        sqLiteStatement.bindString(3, messageView.getContent());
        sqLiteStatement.bindLong(4, messageView.getMessageType());
        sqLiteStatement.bindLong(5, messageView.getTimeS());

        sqLiteStatement.executeInsert();
        sq.close();

        return true;


    }

    @SuppressLint("Range")
    public boolean deleteMessage(String fromUid){

        SQLiteDatabase sq = new MessageAllData(context,null,null,1).getReadableDatabase();
        sq.execSQL(" DELETE FROM  messageAll where fromUid = ?",new String[]{fromUid});
        sq.close();

        return true;
    }






}
