package cn.shilight.myapplication.ui;

import static androidx.core.content.ContextCompat.registerReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.LinkedList;
import java.util.List;

import cn.shilight.myapplication.FriendMessage;
import cn.shilight.myapplication.MainActivity;
import cn.shilight.myapplication.MessageActivity;
import cn.shilight.myapplication.R;
import cn.shilight.myapplication.adapter.MessageListAdapter;
import cn.shilight.myapplication.adapter.MessageViewAdapter;
import cn.shilight.myapplication.adapter.RecyclerTouchListener;
import cn.shilight.myapplication.dao.FriendDao;
import cn.shilight.myapplication.dao.MessageAllDao;
import cn.shilight.myapplication.domain.Friend;
import cn.shilight.myapplication.domain.Message;
import cn.shilight.myapplication.dao.MessageListDao;
import cn.shilight.myapplication.domain.MessageView;
import cn.shilight.myapplication.message.MessagObtian;
import cn.shilight.myapplication.myui.ScanActivity;
import cn.shilight.myapplication.util.BasicUtil;

public class MeaasageFragment extends Fragment {


    private RecyclerView recyclerView;
    MessageListDao messageListDao1;
    List<Message> messageList;

    MessageReceived myReceiver;


    RecyclerTouchListener listener;
    public MeaasageFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meaasage, container, false);
        recyclerView = root.findViewById(R.id.messagerv);
        messageList = new LinkedList<Message>();


        listener = new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent go = new Intent();
                go.putExtra("uid", messageListDao1.getMessageList().get(position).getUid());
                go.setClass(getContext(),MessageActivity.class);


                startActivity(go);

            }

            @Override
            public void onLongClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position,MotionEvent event) {
                initPopWindow(view,position,event);



            }
        });




        messageListDao1 = new MessageListDao(getContext());
        recyclerView.setAdapter(new MessageListAdapter(messageListDao1.getMessageList(),getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnItemTouchListener(listener);

        myReceiver = new MessageReceived();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction("cn.shilight.myapplication.Received");
        getContext().registerReceiver(myReceiver, itFilter);



        return root;

    }





    @Override
    public void onResume() {
        super.onResume();


        messageListDao1 = new MessageListDao(getContext());
        recyclerView.setAdapter(new MessageListAdapter(messageListDao1.getMessageList(),getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    class  handler extends Handler {

        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);


        }
    }


    private void initPopWindow(View v,int position,MotionEvent event) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pop_message, null, false);
        LinearLayout clean =view.findViewById(R.id.buttonscan);
        LinearLayout del =view.findViewById(R.id.buttondel);

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
        popWindow.showAsDropDown(v, (int) event.getX()/2+50, -20);
       // popWindow.showAtLocation(v,1, (int) event.getX(), (int) event.getY());

        //设置popupWindow里的按钮的事件
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageListDao1.delect(position);
                recyclerView.setAdapter(new MessageListAdapter(messageListDao1.getMessageList(),getContext()));
                popWindow.dismiss();

            }
        });

        del.setOnClickListener(view1 -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("确认要删除吗");
            builder.setMessage("这只会删除聊天数据\n请您三思");
            Friend fr = new FriendDao(getContext()).getUser(messageListDao1.getMessageList().get(position).getUid());

            builder.setIcon(R.drawable.icon_green);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new MessageAllDao(getContext()).deleteMessage(fr.getUid());
                    new MessageListDao(getContext()).delectByFromUid(fr.getUid());
                    recyclerView.setAdapter(new MessageListAdapter(messageListDao1.getMessageList(),getContext()));
                    popWindow.dismiss();

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        });

    }




    class  MessageReceived extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            messageListDao1 = new MessageListDao(getContext());
            recyclerView.setAdapter(new MessageListAdapter(messageListDao1.getMessageList(),getContext()));



        }
    }



}