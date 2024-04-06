package cn.shilight.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.shilight.myapplication.adapter.MessageViewAdapter;
import cn.shilight.myapplication.dao.FriendDao;
import cn.shilight.myapplication.dao.MessageAllDao;
import cn.shilight.myapplication.domain.Friend;
import cn.shilight.myapplication.domain.MessageView;
import cn.shilight.myapplication.domain.MyData;
import cn.shilight.myapplication.message.MessagObtian;
import cn.shilight.myapplication.service.MessageServiceT;
import cn.shilight.myapplication.util.BackGroundUtil;
import cn.shilight.myapplication.util.BasicUtil;
import cn.shilight.myapplication.util.SensorManagerHelper;
import cn.shilight.myapplication.util.SettingUtil;
import cn.shilight.myapplication.util.TxUtil;

public class MessageActivity extends AppCompatActivity {

    // 可能不用
    testHander testHander = new testHander();

    /// 服务通信
    private MessageServiceT.MessageBinder messageBinder;
    MessageServiceConnection conn;

    List<MessageView> messageViewList ;

    Friend fr;

    private RecyclerView recyclerView;
    private TextView tittle;
    private ImageView  back;

    private EditText message_content;
    MyData data;
    Intent t;

    private FriendDao friendDao;
    private ImageView backImg;

    MessageReceived myReceiver;

    private Button send_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        data = SettingUtil.getMydata(this);




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messageshow);
        getSupportActionBar().hide();

        conn = new MessageServiceConnection();
        friendDao = new FriendDao(this);
        backImg = findViewById(R.id.backImg);

        backImg.setImageResource(BackGroundUtil.getConfig(this));


        Intent intent = new Intent(this, MessageServiceT.class);
        bindService(intent,conn,BIND_AUTO_CREATE);
        t =  this.getIntent();


        fr = friendDao.getUser(t.getStringExtra("uid"));



        SensorManagerHelper sensorHelper = new SensorManagerHelper(this);
        sensorHelper.setOnShakeListener(() -> {

            Log.i("eeee","检测到摇动");
            backImg.setImageResource(BackGroundUtil.changeConfig(this));

        });




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
        tittle = findViewById(R.id.title_name);

        send_button = findViewById(R.id.send_button);
        message_content = findViewById(R.id.send_message_content);



        tittle.setText(fr.getName());
        recyclerView = findViewById(R.id.message_container);
        back = findViewById(R.id.MessageViewBack);

        TxUtil.reNewTxByUid(fr.getUid(),this); // 更新头像用
        TxUtil.reNewTxByUid(SettingUtil.getMydata(this).getUid(),this); // 更新头像用



        MessageAllDao messageAllDao = new MessageAllDao(this);
        messageViewList = messageAllDao.getMessagesByFromUid(t.getStringExtra("uid"));

        myReceiver = new MessageReceived();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction("cn.shilight.myapplication.Received");
        registerReceiver(myReceiver, itFilter);

        send_button.setOnClickListener(view -> {

            if(message_content.getText().length()<=0){
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            }else{


                messageBinder.sendMessage(new MessagObtian(t.getStringExtra("uid"), SettingUtil.getMydata(this).getUid(),0)
                        .setContent(message_content.getText().toString()));

                messageViewList.add(new MessageView(message_content.getText().toString(),fr.getUid(),data.getUid(), BasicUtil.getTime(),MessageView.MY_MESSAGE));
                recyclerView.setAdapter(new MessageViewAdapter(messageViewList,this));
                message_content.setText("");
                recyclerView.scrollToPosition(messageViewList.size()-1);



            }

        });

        recyclerView.setAdapter(new MessageViewAdapter(messageViewList,this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.scrollToPosition(messageViewList.size()-1);


        back.setOnClickListener(V->{
            finish();
        });


    }


    class testHander extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = new Intent(this, MessageServiceT.class);
        startService(intent);

    }

    class  MessageReceived extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String fromUid = intent.getStringExtra("form");
            String content = intent.getStringExtra("content");


                if(fr.getUid().equals(fromUid)){

                    messageViewList.add(new MessageView(content,fr.getUid(),data.getUid(), BasicUtil.getTime(),MessageView.SHE_MESSAGE));
                    recyclerView.setAdapter(new MessageViewAdapter(messageViewList,MessageActivity.this));

                    recyclerView.scrollToPosition(messageViewList.size()-1);

                }


        }
    }


}


