package cn.shilight.myapplication.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.jinrishici.sdk.android.view.JinrishiciTextView;
import com.jinrishici.sdk.android.view.JinrishiciTextViewConfig;

import java.io.File;

import cn.shilight.myapplication.R;
import cn.shilight.myapplication.AboutActivity;
import cn.shilight.myapplication.dao.TxDao;
import cn.shilight.myapplication.domain.MyData;
import cn.shilight.myapplication.myui.QrCodeActivity;
import cn.shilight.myapplication.myui.ScanActivity;
import cn.shilight.myapplication.startui.SetTxActivity;
import cn.shilight.myapplication.startui.SettingUserActivity;
import cn.shilight.myapplication.util.FastBlurUtil;
import cn.shilight.myapplication.util.SettingUtil;


public class MyFragment extends Fragment {


    private ImageView imageView,qrbutton,myback,userdata;
    private TextView name,words;
    private ConstraintLayout scan,setting;
    private JinrishiciTextView jinrisiciTextView;

    MyData data;



    private int scaleRatio = 10;
    private int blurRadius = 30;



    /// 二维码 扫描功能 翻了翻 以前的 比赛作品
    /// 也不知道当初从哪里看到的  😂
    /// 2023.5.14 最后加两个功能 一个是 头像背景模糊  一个是 随机诗句 （网络 io handler ）
    //  2023.6.5  诗句功能偷了个懒 使用了 一言 的包





    public MyFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View root =  inflater.inflate(R.layout.fragment_my, container, false);
         data = SettingUtil.getMydata(getContext());

         imageView = root.findViewById(R.id.my_icon);
         qrbutton = root.findViewById(R.id.qrbutton);
         scan = root.findViewById(R.id.myScane);
         name = root.findViewById(R.id.myname);
         words = root.findViewById(R.id.mywords);
         setting = root.findViewById(R.id.mysetting);
         myback = root.findViewById(R.id.myback);
         jinrisiciTextView = root.findViewById(R.id.jinrisiciTextView);
         userdata = root.findViewById(R.id.userdata);


        AssetManager mgr = getContext().getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fronts/zi2.ttf");
        jinrisiciTextView.setConfig(new JinrishiciTextViewConfig().setShowErrorOnTextView(false).setShowLoadingText(false).setRefreshWhenClick(true));
        jinrisiciTextView.setTypeface(tf);


         name.setText(data.getName());
         words.setText(data.getWord());

         //// 从这里读取照片

        TxDao txDao = new TxDao(getActivity());
        Bitmap tx = txDao.getTxData(data.getUid());
        if(tx==null) {
            tx = BitmapFactory.decodeResource(getResources(), R.drawable.defaultmessage);
        }

        imageView.setImageBitmap(tx);

         /// 开始 头像背景图处理

        Bitmap mBitmap = tx;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mBitmap,
                mBitmap.getWidth() / scaleRatio,
                mBitmap.getHeight() / scaleRatio,
                false);
        Bitmap blurBitmap = FastBlurUtil.doBlur(scaledBitmap, blurRadius, true);
        myback.setImageBitmap(blurBitmap);



        setting.setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.setClass(getActivity(), AboutActivity.class);
            getContext().startActivity(intent);


        });

        imageView.setOnClickListener(view -> {

            // 修改头像
            Intent intent = new Intent();
            intent.putExtra("way",2);
            intent.setClass(getActivity(), SetTxActivity.class);
            getContext().startActivity(intent);

        });

        userdata.setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.putExtra("way",2);
            intent.setClass(getActivity(), SettingUserActivity.class);
            getContext().startActivity(intent);

        });




         scan.setOnClickListener(view -> {

             requsetPermission();
             IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
             intentIntegrator.setBeepEnabled(true);
             intentIntegrator.setCaptureActivity(ScanActivity.class);
             intentIntegrator.initiateScan();

         });





         qrbutton.setOnClickListener(view -> {
             Intent go = new Intent();
             go.setClass(getActivity(), QrCodeActivity.class);
             startActivity(go);
         });


         return root;
    }


    private void requsetPermission(){
        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.CAMERA)!=     PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.CAMERA},1);
            }else {
            }
        }else {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    Log.i("123","123121123");

                }else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    Toast.makeText(getActivity(),"请手动打开相机权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        TxDao txDao = new TxDao(getActivity());

        Bitmap tx = txDao.getTxData(data.getUid());

        if(tx==null) {
            tx = BitmapFactory.decodeResource(getResources(), R.drawable.defaultmessage);

        }


        imageView.setImageBitmap(tx);


        /// 开始 头像背景图处理

        Bitmap mBitmap = tx;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mBitmap,
                mBitmap.getWidth() / scaleRatio,
                mBitmap.getHeight() / scaleRatio,
                false);
        Bitmap blurBitmap = FastBlurUtil.doBlur(scaledBitmap, blurRadius, true);
        myback.setImageBitmap(blurBitmap);
        name.setText(data.getName());
        words.setText(data.getWord());

    }
}