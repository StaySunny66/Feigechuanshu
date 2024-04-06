package cn.shilight.myapplication.startui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import cn.shilight.myapplication.MainActivity;
import cn.shilight.myapplication.R;
import cn.shilight.myapplication.util.SettingUtil;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        getSupportActionBar().hide();

        int state = SettingUtil.getState(this);

        Intent go;
        switch (state){
            case 0:
            case 1:
                go = new Intent();
                go.setClass(this,SettingUserActivity.class);
                startActivity(go);
                finish();

                break;
            case 2:

                go = new Intent();
                go.setClass(this,SetTxActivity.class);
                startActivity(go);
                finish();

                break;
            case 3:

                go = new Intent();
                go.setClass(this,FinishActivity.class);
                startActivity(go);
                finish();

                break;

            case 4:

                go = new Intent();
                go.setClass(this, MainActivity.class);
                startActivity(go);
                finish();

                break;


        }

        // TO DO


        // 检测服务是否启动  服务不正常 则重新启动服务

        // 是否为第一次运行  第一次 进入配置界面  开始配置


    }
}