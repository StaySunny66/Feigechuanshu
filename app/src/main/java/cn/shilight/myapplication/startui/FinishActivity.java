package cn.shilight.myapplication.startui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.shilight.myapplication.MainActivity;
import cn.shilight.myapplication.R;
import cn.shilight.myapplication.domain.MyData;
import cn.shilight.myapplication.util.SettingUtil;
import cn.shilight.myapplication.util.TxUtil;

public class FinishActivity extends AppCompatActivity {
    Button button;
    TextView uuid,key,name;
    ImageView tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        getSupportActionBar().hide();


        MyData data = SettingUtil.getMydata(this);

        button = findViewById(R.id.finishbutton);
        uuid = findViewById(R.id.finishuuid);
        key = findViewById(R.id.finishkey);
        tx = findViewById(R.id.finishtx);
        name = findViewById(R.id.finishname);



        uuid.setText(data.getUid());
        key.setText(data.getWord());
        name.setText(data.getName());
        Log.i("eeeeee",String.valueOf(data.getTx_id()));


        tx.setImageBitmap(TxUtil.getTxByUser(this,data.getUid()));
        //tx.setImageResource(data.getTx_id());


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









        button.setOnClickListener(view -> {

            SettingUtil.setState(this,4);
            Intent go = new Intent();
            go.setClass(this, MainActivity.class);
            startActivity(go);


        });
    }
}