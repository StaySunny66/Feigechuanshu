package cn.shilight.myapplication.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import cn.shilight.myapplication.FriendMessage;
import cn.shilight.myapplication.MainActivity;
import cn.shilight.myapplication.MessageActivity;
import cn.shilight.myapplication.R;
import cn.shilight.myapplication.adapter.MessageListAdapter;
import cn.shilight.myapplication.adapter.PeopleAdapter;
import cn.shilight.myapplication.adapter.RecyclerTouchListener;
import cn.shilight.myapplication.dao.FriendDao;
import cn.shilight.myapplication.domain.Friend;
import cn.shilight.myapplication.domain.FriendList;


public class PeopleFragment extends Fragment {

    FriendDao friendDao;
    private RecyclerView recyclerView;

    RecyclerTouchListener listener;

    public PeopleFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_people, container, false);
        recyclerView = root.findViewById(R.id.peoplev);





        List<FriendList> friendLists = new LinkedList<FriendList>();


        friendDao = new FriendDao(getContext());



        listener = new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i("cc","ddd");

                //Toast.makeText(getContext(),String.valueOf(position),Toast.LENGTH_LONG).show();
                Intent go = new Intent();
                go.putExtra("uid", friendDao.getAllUserList().get(position).getUid());
                go.setClass(getContext(), FriendMessage.class);

                startActivity(go);

            }

            @Override
            public void onLongClick(View view, int position) {


            }

            @Override
            public void onLongClick(View view, int position, MotionEvent e) {

            }
        });




        friendDao.addUser(new Friend("123444","我的电脑(TEST)","未来可期",R.drawable.defaultmessage));

        recyclerView.setAdapter(new PeopleAdapter(friendDao.getAllUserList(),getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnItemTouchListener(listener);

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("PeoloeFragment","onResume");

        friendDao = new FriendDao(getContext());
        friendDao.addUser(new Friend("123444","我的电脑(TEST)","未来可期",R.drawable.defaultmessage));
        recyclerView.setAdapter(new PeopleAdapter(friendDao.getAllUserList(),getContext()));




    }
}