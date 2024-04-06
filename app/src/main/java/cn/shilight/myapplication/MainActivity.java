package cn.shilight.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.shilight.myapplication.domain.NewFriend;
import cn.shilight.myapplication.myui.NewFriendActivity;
import cn.shilight.myapplication.myui.ScanActivity;
import cn.shilight.myapplication.service.MessageServiceT;
import cn.shilight.myapplication.ui.MeaasageFragment;
import cn.shilight.myapplication.ui.MyFragment;
import cn.shilight.myapplication.ui.PeopleFragment;
import cn.shilight.myapplication.util.FastBlurUtil;
import cn.shilight.myapplication.util.ScannerUtil;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private RadioGroup radioGroup;
    private TextView toolbarText;

    String xiaoxi;
    String lianxiren;

    Intent intent;


    private ImageView search,add;
    ConstraintLayout cs;
    ConstraintLayout ll;
    MessageReceived myReceiver;

    private ImageView addim,mainbackimg;


    int flag;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addim = findViewById(R.id.add_button);
        search = findViewById(R.id.search_button);
        mainbackimg = findViewById(R.id.mainbackimg);

        getSupportActionBar().hide();

        xiaoxi = "消息";
        lianxiren = "联系人";

        flag = 0;


        //conn = new MessageServiceConnection();
        //intent = new Intent(this, MessageService.class);
        // bindService(intent,conn,BIND_AUTO_CREATE);

        final Intent intent = new Intent(this, MessageServiceT.class);
        startService(intent);


        //messageBinder.starConnect();

        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.back5);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mBitmap,
                mBitmap.getWidth() / 10,
                mBitmap.getHeight() / 10,
                false);
        Bitmap blurBitmap = FastBlurUtil.doBlur(scaledBitmap, 30, true);
        //mainbackimg.setImageBitmap(blurBitmap);


        myReceiver = new MessageReceived();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction("cn.shilight.myapplication.connect");
        registerReceiver(myReceiver, itFilter);

        viewPager2 = findViewById(R.id.viewPager2);
        radioGroup = findViewById(R.id.Meun);
        toolbarText = findViewById(R.id.state);
        cs = findViewById(R.id.toolbar);
        ll = findViewById(R.id.statebar);

        search = findViewById(R.id.search_button);
        add = findViewById(R.id.add_button);

        List<Fragment> fview = new ArrayList<Fragment>();
        fview.add(new MeaasageFragment());
        fview.add(new PeopleFragment());
        fview.add(new MyFragment());


        viewPager2.setAdapter(new FragAdapter(this.getSupportFragmentManager(),fview));


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






        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.radioButton2:viewPager2.setCurrentItem(0);

                        toolbarText.setText(xiaoxi);
                        search.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        flag = 0;

                        break;

                    case R.id.radioButton3:viewPager2.setCurrentItem(1);

                        toolbarText.setText(lianxiren);
                        search.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        flag = 1;
                        break;

                    case R.id.radioButton4:viewPager2.setCurrentItem(2);

                        toolbarText.setText("");
                        search.setVisibility(View.GONE);
                        add.setVisibility(View.GONE);
                        flag = 2;
                        break;

                }
            }
        });


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                Log.i("AA", String.valueOf(position));

                switch (position){
                    case 0:

                        ((RadioButton)findViewById(R.id.radioButton2)).setChecked(true);

                        break;
                    case 1:
                        ((RadioButton)findViewById(R.id.radioButton3)).setChecked(true);

                        break;
                    case 2:
                        ((RadioButton)findViewById(R.id.radioButton4)).setChecked(true);

                        break;
                }


            }
        });


        addim.setOnClickListener(view -> {


            initPopWindow(view);
        });

        search.setOnClickListener(view -> {


            Intent go  = new Intent();
            go.setClass(this, SearchActivity.class);
            startActivity(go);


        });


    }



    private void initPopWindow(View v) {
        View view = LayoutInflater.from(this).inflate(R.layout.popwindwow, null, false);
        LinearLayout scan =view.findViewById(R.id.buttonscan);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);


        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 50, 0);

        //设置popupWindow里的按钮的事件
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requsetPermission();
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setBeepEnabled(true);
                /*设置启动我们自定义的扫描活动，若不设置，将启动默认活动*/
                intentIntegrator.setCaptureActivity(ScanActivity.class);
                intentIntegrator.initiateScan();

            }
        });

    }

    private void requsetPermission(){
        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA)!=     PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},1);

            }else {

            }
        }else {

        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
               // Toast.makeText(MainActivity.this, "取消扫描", Toast.LENGTH_LONG).show();
            } else {
                /// 获得了扫描内容


                NewFriend nf = ScannerUtil.parsh(result.getContents());

             //   Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();

                if(nf!=null){

                    Intent go  = new Intent();
                    go.setClass(this, NewFriendActivity.class);
                    go.putExtra("data",result.getContents());
                    startActivity(go);


                }else {

                    Toast.makeText(MainActivity.this,"不支持的二维码", Toast.LENGTH_LONG).show();


                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    class  MessageReceived extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String state = intent.getStringExtra("state");

            Log.e("MessageServerstate",state);
            if("err".equals(state)){

                xiaoxi = "消息(未连接)";
                lianxiren = "联系人(未连接)";

            }else {
                xiaoxi = "消息";
                lianxiren = "联系人";

            }
            if(flag==0){
                toolbarText.setText(xiaoxi);
            }else if(flag==1) {
                toolbarText.setText(lianxiren);
            }else {


            }




        }
    }




}