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

import cn.shilight.myapplication.data.TxDataSql;
import cn.shilight.myapplication.data.UserSql;
import cn.shilight.myapplication.domain.Friend;

public class TxDao {
    private Context context;

    public TxDao(Context context) {
        this.context = context;
    }


   public boolean upDataTx(String uid, Bitmap bitmap){

       SQLiteDatabase sq = new TxDataSql(context,null,null,1).getReadableDatabase();




        /// 先检查头像是否存在

       if(getTxData(uid)!=null){


           String sql = "UPDATE txdata SET txdata  = ? WHERE uid = ?";
           SQLiteStatement sqLiteStatement = sq.compileStatement(sql);
           ByteArrayOutputStream os = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

           sqLiteStatement.clearBindings();
           sqLiteStatement.bindString(2, uid);
           sqLiteStatement.bindBlob(1, os.toByteArray());
           sqLiteStatement.executeUpdateDelete();

           sq.close();
           return true;
       }


       String sql = "INSERT INTO txdata(uid,txdata) values(?,?)";
       SQLiteStatement sqLiteStatement = sq.compileStatement(sql);
       ByteArrayOutputStream os = new ByteArrayOutputStream();
       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

       sqLiteStatement.clearBindings();
       sqLiteStatement.bindString(1, uid);
       sqLiteStatement.bindBlob(2, os.toByteArray());
       sqLiteStatement.executeInsert();

       sq.close();
       return true;

    }
    @SuppressLint("Range")
    public Bitmap getTxData(String uid){


        SQLiteDatabase sq = new TxDataSql(context,null,null,1).getReadableDatabase();

        Cursor cr = sq.rawQuery("SELECT * FROM txdata WHERE uid = ?",new String[]{uid});
        Log.i("SQL", String.valueOf(cr.getColumnIndex("txdata")));


        //Log.i("SQL", String.valueOf(cr.getBlob(cr.getColumnIndex("txdata"))));
        if(cr.moveToFirst()){
            // Log.i("SQL","读取数据");
            return BitmapFactory.decodeByteArray(cr.getBlob(cr.getColumnIndex("txdata")),0,cr.getBlob(cr.getColumnIndex("txdata")).length);
        }
        sq.close();
        return null;



    }


}
