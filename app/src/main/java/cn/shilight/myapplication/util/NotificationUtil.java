package cn.shilight.myapplication.util;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import cn.shilight.myapplication.R;
import cn.shilight.myapplication.dao.FriendDao;
import cn.shilight.myapplication.domain.Friend;

public class NotificationUtil {


   public static boolean sendMessageNot(Context context, String from, String content){

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel("ShadyPi","消息通知",
                NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);
       Friend fr = new FriendDao(context).getUser(from);
       if(fr==null) return false;

        Notification note=new NotificationCompat.Builder(context,"ShadyPi")
                .setContentTitle(fr.getName())
                .setContentText(content)
                .setSmallIcon(R.drawable.icon_white)
                .build();





        manager.notify(1,note);
        return true;

    }


    public static void sendNewFriendNot(Context context, String name){

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel("friend","好友通知",
                NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);

        Notification note=new NotificationCompat.Builder(context,"friend")
                .setContentTitle(name)
                .setContentText("我已添加你为好友，快来聊天吧")
                .setSmallIcon(R.drawable.icon_white)
                .build();
        manager.notify(1,note);

    }






}
