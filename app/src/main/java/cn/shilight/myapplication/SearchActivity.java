package cn.shilight.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import cn.shilight.myapplication.adapter.PeopleAdapter;
import cn.shilight.myapplication.adapter.RecyclerTouchListener;
import cn.shilight.myapplication.dao.FriendDao;
import cn.shilight.myapplication.domain.Friend;
import cn.shilight.myapplication.domain.FriendList;

public class SearchActivity extends AppCompatActivity {
   EditText searcheditTextText;
   List<FriendList> friendList ;
   RecyclerView recyclerView;
   TextView back;
   FriendDao friendDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        back = findViewById(R.id.searback);
        searcheditTextText = findViewById(R.id.searcheditTextText);
        recyclerView = findViewById(R.id.searchrec);
        friendDao = new FriendDao(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));




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





        back.setOnClickListener(view -> {
            finish();


        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                Intent go = new Intent();
                go.putExtra("uid", friendDao.getAllUserList().get(position).getUid());
                go.setClass(SearchActivity.this, FriendMessage.class);
                startActivity(go);

            }

            @Override
            public void onLongClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position, MotionEvent e) {

            }
        }));



        searcheditTextText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(searcheditTextText.getText().toString().length()>0){

                    friendList = friendDao.getUsersByName(searcheditTextText.getText().toString());
                    recyclerView.setAdapter(new PeopleAdapter(friendList,SearchActivity.this));
                }else {

                    friendList = friendDao.getUsersByName("ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
                    recyclerView.setAdapter(new PeopleAdapter(friendList,SearchActivity.this));

                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}