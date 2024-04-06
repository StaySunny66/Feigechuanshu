package cn.shilight.myapplication.startui;

import static android.os.Environment.DIRECTORY_PICTURES;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.soundcloud.android.crop.Crop;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import cn.shilight.myapplication.R;
import cn.shilight.myapplication.adapter.HeadAdapter;
import cn.shilight.myapplication.dao.TxDao;
import cn.shilight.myapplication.domain.MyData;
import cn.shilight.myapplication.util.BasicUtil;
import cn.shilight.myapplication.util.SettingUtil;
import cn.shilight.myapplication.util.TxUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class SetTxActivity extends AppCompatActivity {
    RecyclerView txSel;

    private ImageView txImageSel;
    private Button upLoadButtonl,dqtx;

    private ProgressBar progressBar;
    Intent II;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_tx);
        getSupportActionBar().hide();

        /// 组件绑定
        txImageSel = findViewById(R.id.txImageSel);
        upLoadButtonl = findViewById(R.id.uploadbutton);
        progressBar = findViewById(R.id.progressBar);
        dqtx = findViewById(R.id.dqtx);

        progressBar.setVisibility(View.GONE);



        /// 设置头像activity


        /// 点击头像设置图库回调


        ActivityResultLauncher<Intent> imgSelectLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //此处是跳转的result回调方法

                if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {

                    txImageSel.setImageURI(result.getData().getData());

                    Uri sourceUri =  result.getData().getData();
                    Uri destinationUri = Uri.fromFile(new File(getFilesDir().getAbsolutePath(), "ddd.jpg"));


                    UCrop.of(sourceUri, destinationUri)
                            .withAspectRatio(1, 1)
                            .withMaxResultSize(400, 400)
                            .start(SetTxActivity.this);


                }

            }
        });


        /// 点击事件的监听
        txImageSel.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imgSelectLauncher.launch(intent);

        });

        txImageSel.setImageBitmap(TxUtil.getTxByUser(this,SettingUtil.getMydata(this).getUid()));



        dqtx.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imgSelectLauncher.launch(intent);

        });


        upLoadButtonl.setOnClickListener(view -> {

            progressBar.setVisibility(View.VISIBLE);
            upload(SettingUtil.getMydata(this).getUid());




        });



        II = getIntent();
        MyData data = SettingUtil.getMydata(this);



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
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            txImageSel.setImageURI(resultUri);
            // 上传文件






        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }





    void upload(String uid){

        File file = new File(getFilesDir().getAbsolutePath()+"/ddd.jpg");

        if(!file.exists()){
            Toast.makeText(this,"请先选择一个头像",Toast.LENGTH_LONG).show();
            return;
        }

        TxDao txDao = new TxDao(SetTxActivity.this);
        MyData da = SettingUtil.getMydata(SetTxActivity.this);

        txDao.upDataTx(da.getUid(),BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/ddd.jpg"));

        Log.i("upload",file.getName());




        // 请求体
//这里是设置上传的类型为image,/*是不限制图片格式
        RequestBody image=RequestBody.create(MediaType.parse("multipart/form-data"),file);
//请求体
        RequestBody requestBody=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//这个很重要，如果是上传图片的同时还需要其他参数，就需要ALTERNATIVE这个属性
                .addFormDataPart("uid",uid)
                .addFormDataPart("uploadFile","uploadFile",image)//第一个参数是服务端接收的标记，第二个是路径，第三个是请求体信息
                .build();
        Request request=new Request.Builder()
                .url("https://chatserver.shilight.cn/upload")//服务端地址，可以使用本地Tomcat，需要保证手机和电脑在同一局域网下，使用servlet接收数据
                .post(requestBody)//post方式提交，这是必须的
                .build();


        OkHttpClient mOkHttpClient = new OkHttpClient();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.i("upload", "onFailure: ");
                e.printStackTrace();


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                // 上传完毕

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SetTxActivity.this,"上传完成",Toast.LENGTH_LONG).show();

                        MyData data = SettingUtil.getMydata(SetTxActivity.this);

                        TxUtil.reNewTxByUid(data.getUid(),SetTxActivity.this);



                        if(II.getIntExtra("way",0)==0){
                            SettingUtil.setState(SetTxActivity.this,3);
                            Intent go = new Intent();
                            go.setClass(SetTxActivity.this,FinishActivity.class);
                            startActivity(go);
                            finish();
                        } else {
                            finish();
                        }




                    }
                });

            }
        });









    }










}