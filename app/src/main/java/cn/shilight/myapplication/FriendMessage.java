package cn.shilight.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.shilight.myapplication.dao.FriendDao;
import cn.shilight.myapplication.dao.MessageAllDao;
import cn.shilight.myapplication.dao.MessageListDao;
import cn.shilight.myapplication.domain.Friend;
import cn.shilight.myapplication.util.TxUtil;

public class FriendMessage extends AppCompatActivity {

    ConstraintLayout tomessage,del,clean;
    ImageView back,nume,tx;

    TextView name,words;
    Friend fr;


    FriendDao friendDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_message);
        getSupportActionBar().hide();

        tomessage = findViewById(R.id.friendtomess);
        del = findViewById(R.id.delmessage);
        back = findViewById(R.id.friendback);
        clean= findViewById(R.id.cleanmess);

        name =findViewById(R.id.friendname);
        tx = findViewById(R.id.friendtx);
        words = findViewById(R.id.friendwords);

        friendDao = new FriendDao(this);

        getWindow().setStatusBarColor(getResources().getColor(R.color.wx_back,null));
        int visibility = getWindow().getDecorView().getSystemUiVisibility();

        visibility = visibility| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        getWindow().getDecorView().setSystemUiVisibility(visibility);




        Intent intent  = getIntent();
        String friendId = intent.getStringExtra("uid");

        fr = friendDao.getUser(friendId);
        if(fr!=null){

            name.setText(fr.getName());
            tx.setImageBitmap(TxUtil.getTxByUser(this,fr.getUid()));
            TxUtil.reNewTxByUid(fr.getUid(),this); // 更新头像用
            words.setText(fr.getWords());
        }






        tomessage.setOnClickListener(view -> {

            Intent go = new Intent();

            go.putExtra("uid", friendId);
            go.setClass(this,MessageActivity.class);
            startActivity(go);

        });

        del.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("确认要删除吗");
            builder.setMessage("聊天记录和好友将一并删除\n请您三思");

            builder.setIcon(R.drawable.icon_green);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    new FriendDao(FriendMessage.this).deleteFriend(fr.getUid());
                    new MessageAllDao(FriendMessage.this).deleteMessage(fr.getUid());
                    new MessageListDao(FriendMessage.this).delectByFromUid(fr.getUid());
                    finish();

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        });
        clean.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("确认要删除吗");
            builder.setMessage("这只会删除聊天数据\n请您三思");

            builder.setIcon(R.drawable.icon_green);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new MessageAllDao(FriendMessage.this).deleteMessage(fr.getUid());
                    new MessageListDao(FriendMessage.this).delectByFromUid(fr.getUid());
                    finish();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();



        });




        back.setOnClickListener(view -> {


            finish();


        });










    }
}