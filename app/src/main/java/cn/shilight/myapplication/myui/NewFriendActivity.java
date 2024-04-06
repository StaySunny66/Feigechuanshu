package cn.shilight.myapplication.myui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cn.shilight.myapplication.MessageActivity;
import cn.shilight.myapplication.R;
import cn.shilight.myapplication.dao.FriendDao;
import cn.shilight.myapplication.dao.TxDao;
import cn.shilight.myapplication.domain.Friend;
import cn.shilight.myapplication.domain.MessageType;
import cn.shilight.myapplication.domain.MyData;
import cn.shilight.myapplication.domain.NewFriend;
import cn.shilight.myapplication.message.MessagObtian;
import cn.shilight.myapplication.service.MessageServiceT;
import cn.shilight.myapplication.startui.FinishActivity;
import cn.shilight.myapplication.startui.SetTxActivity;
import cn.shilight.myapplication.util.ScannerUtil;
import cn.shilight.myapplication.util.SettingUtil;
import cn.shilight.myapplication.util.TxUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Sink;

public class NewFriendActivity extends AppCompatActivity {

    Button button;
    TextView uuid, words, name;
    ProgressBar load_bar;
    CardView cardView;
    ImageView tx;

    NewFriend nf;


    private MessageServiceT.MessageBinder messageBinder;
    MessageServiceConnection conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);




        getSupportActionBar().hide();

        button = findViewById(R.id.newbutton);
        uuid = findViewById(R.id.newuuid);
        words = findViewById(R.id.newwords);
        tx = findViewById(R.id.newtx);
        name = findViewById(R.id.newname);
        load_bar = findViewById(R.id.load_bar);
        cardView = findViewById(R.id.cardView4);

        Intent intent = getIntent();
        String st = intent.getExtras().getString("data");

        nf = ScannerUtil.parsh(st);


        conn = new MessageServiceConnection();

        Intent sss = new Intent(this, MessageServiceT.class);
        bindService(sss, conn, BIND_AUTO_CREATE);

        load_bar.setVisibility(View.VISIBLE);
        tx.setVisibility(View.INVISIBLE);
        cardView.setVisibility(View.INVISIBLE);


        /// 来一个异步获取信息


        uuid.setText(nf.getUUID());
        words.setText(nf.getWords());

        reNewTxByUid(nf.getUUID(),this);


        name.setText(nf.getName());

        MyData data = SettingUtil.getMydata(this);


        button.setOnClickListener(view -> {

            FriendDao friendDao = new FriendDao(this);
            friendDao.addUser(new Friend(nf.getUUID(), nf.getName(), nf.getWords(), nf.getTx()));

            // TODO: 2023/5/11    通过服务器向 对方发送 我的数据包

            /// 把我的消息发过去
            messageBinder.sendMessage(new MessagObtian(nf.getUUID(), data.getUid(), MessageType.NEW_FRIEND_MESSAGE).setContent(data.getQRString()));

            Toast.makeText(this, "添加好友消息已发送", Toast.LENGTH_SHORT).show();

            finish();
        });


    }


    class MessageServiceConnection implements ServiceConnection {


        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            messageBinder = (MessageServiceT.MessageBinder) iBinder;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }


    }

    public void reNewTxByUid(String uid, Context mContext){

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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tx.setImageBitmap(bitmap);

                                load_bar.setVisibility(View.INVISIBLE);
                                tx.setVisibility(View.VISIBLE);
                                cardView.setVisibility(View.VISIBLE);

                                /// 保存头像  (异步)
                                TxUtil.reNewTxByUid(uid,NewFriendActivity.this);

                            }
                        });



                    }


                }

            }
        });



    }


}