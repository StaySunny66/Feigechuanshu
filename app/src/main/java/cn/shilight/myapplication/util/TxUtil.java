package cn.shilight.myapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.IOException;

import cn.shilight.myapplication.R;
import cn.shilight.myapplication.dao.TxDao;
import cn.shilight.myapplication.domain.NewFriend;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class TxUtil {



    public static Bitmap getTxByUser(Context context,String uid){
        TxDao txDao = new TxDao(context);
        Bitmap bitmap = txDao.getTxData(uid);
        if(bitmap==null){

            //如果头像不存在的话 先返回 默认头像
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultmessage);
            // 启动 头像更新函数  (异步 下次再显示)
            reNewTxByUid(uid,context);
            return  bitmap;
        }else {
            return bitmap;
        }
    }


    public static void reNewTxByUid(String uid, Context mContext){

        Log.i("upload","upload uid: "+uid);
        //下载路径，如果路径无效了，可换成你的下载路径
        final String url = "https://chatfile-1309446989.cos-website.ap-beijing.myqcloud.com/imgfiles/txresuorce/"+uid+".jpg";
        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                Log.i("DOWNLOAD","download failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Sink sink = null;
                BufferedSink bufferedSink = null;
                String filename = url.substring(url.lastIndexOf("/") + 1);
                //这是里的mContext是我提前获取了android的context
                if(response.body()!=null){
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                   // Log.i("ok",String.valueOf(bitmap.getHeight()));
                    if(bitmap!=null){
                        TxDao txDao = new TxDao(mContext);
                        txDao.upDataTx(uid,bitmap);
                    }


                }

            }
    });












    }

}
