package cn.shilight.myapplication.startui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.shilight.myapplication.R;
import cn.shilight.myapplication.domain.MyData;
import cn.shilight.myapplication.util.SettingUtil;


public class SettingUserActivity extends AppCompatActivity {
    Button next;
    EditText uid;
    EditText name;
    EditText key;

    Intent II;
    MyData D;
    String uuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        getSupportActionBar().hide();

        next = findViewById(R.id.usernext);
        uid = findViewById(R.id.settinguuid);
        name = findViewById(R.id.settingname);
        key = findViewById(R.id.settingkey);

        String uuid = SettingUtil.getAndroidID(this);

        D = SettingUtil.getMydata(this);

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




        uid.setText(uuid);

        II = getIntent();

        next.setOnClickListener(view -> {



            if(!("".equals(name.getText().toString())&&"".equals(key.getText().toString()))){

                D.setWord(key.getText().toString());
                D.setName( name.getText().toString());
                D.setUid(uuid);

                SettingUtil.saveMydata(this,D);



                if(II.getIntExtra("way",0)==0){
                    SettingUtil.setState(this,2);
                    Intent go = new Intent();
                    go.setClass(this,SetTxActivity.class);
                    startActivity(go);
                    finish();
                }

                finish();


            }else {

                Toast.makeText(this,"请检查键入内容",Toast.LENGTH_LONG).show();


            }




        });
    }
}