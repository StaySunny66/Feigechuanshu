package cn.shilight.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import cn.shilight.myapplication.dao.FriendDao;
import cn.shilight.myapplication.dao.MessageAllDao;
import cn.shilight.myapplication.dao.MessageListDao;
import cn.shilight.myapplication.domain.Friend;
import cn.shilight.myapplication.domain.MessageType;
import cn.shilight.myapplication.domain.MessageView;
import cn.shilight.myapplication.domain.MyData;
import cn.shilight.myapplication.domain.NewFriend;
import cn.shilight.myapplication.message.MessagObtian;
import cn.shilight.myapplication.util.BasicUtil;
import cn.shilight.myapplication.util.NotificationUtil;
import cn.shilight.myapplication.util.ScannerUtil;
import cn.shilight.myapplication.util.SettingUtil;

public class MessageServiceT extends Service {

    LinkedList<MessagObtian> messagesQueue;

    int inflag = 0;
    int outflag = 0;

    ObjectOutputStream out = null;
    ObjectInputStream in = null;

    MessageInThread MIT;
    MessageOutThread MOT;

    int conFlag = 0;
    int count = 0;
    int flags = 0;


    // 服务器地址
    public String serverName;
    // 服务器端口
    int port ;
    Socket client = null;



    public class MessageBinder extends Binder {

        MessageBinder(){

        }

        /// 向服务器发送 消息包  所有的消息都从该方法入队 等待发送
        public void sendMessage(MessagObtian message){
            messagesQueue.offer(message);
        }
    }




    public MessageServiceT() {

        messagesQueue = new LinkedList<MessagObtian>();
        this.serverName = "124.222.3.251";
        this.port =122;
        Log.i("MessageServiceT","server: "+serverName+" port: "+String.valueOf(port));

    }


    //Service被创建时调用
    @Override
    public void onCreate() {

        Log.i("MessageServerT","创建 onCreate");

        super.onCreate();
    }


    //Service被启动时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("MessageServerT","启动");

        if(conFlag==0){
            (new MessageConnectThread()).start();
            conFlag =1;
        }

        if(inflag==0){
            MIT =  new MessageInThread();
            MIT.start();

            inflag = 1;
        }
        if(outflag==0){
            MOT =  new MessageOutThread();
            MOT.start();

            outflag = 1;
        }

        return START_STICKY;

    }


    /// 被销毁是使用
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MessageServerT","销毁");
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MessageBinder();
    }


    // 保证client连接  掉线重连

    class MessageConnectThread extends  Thread{
        public MessageConnectThread() {
            super();
        }

        @Override
        public synchronized void start() {
            super.start();
        }

        @Override
        public void run() {
            super.run();

            while (true){
                // 没有连接的话  连接

                // 客户端开始 flag == 0

                if(flags==0){

                    Log.e("MessageServerT","套接字尝试连接");
                    sendBroadcast(new Intent("cn.shilight.myapplication.connect").putExtra("state","err"));
                    try {

                        client = new Socket(serverName, port);
                        if(client.isConnected()){
                             // 获得输入输出流
                             out = new ObjectOutputStream(client.getOutputStream());
                             in = new ObjectInputStream(client.getInputStream());
                            MyData myData = SettingUtil.getMydata(MessageServiceT.this);
                            out.writeObject(new MessagObtian("12345554ggtergfd",myData.getUid(),99).setContent("online"));
                            out.flush();
                            Log.e("MessageServerT","套接字连接完成");

                            ///// 启动 发送和接收线程
                            flags = 1;

                        }
                    } catch (IOException e) {

                        Log.e("MessageServerT","套接连接可能失败,1秒后 再次连接");
                        count=0;
                        sendBroadcast(new Intent("cn.shilight.myapplication.connect").putExtra("state","err"));
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }
            }



        }
    }



    /// 接收消息线程
    class MessageInThread extends Thread{

        public MessageInThread() {
            super();
        }

        @Override
        public synchronized void start() {
            super.start();
            Log.d("MessageServer","接收线程启动");
        }

        @Override
        public void run() {
            super.run();
            while (true){

                if(flags!=0){
                    try {
                        Log.i("MessageServeT","可以接收消息 "+in.available());
                        sendBroadcast(new Intent("cn.shilight.myapplication.connect").putExtra("state","ok"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {

                        MessagObtian messagObtian = (MessagObtian) in.readObject();
                        Log.i("MessageServeT","接收到消息 来自："+messagObtian.getFromUid()+" 内容："+messagObtian.getContent());
                        FriendDao friendDao = new FriendDao(MessageServiceT.this);

                        switch (messagObtian.getMessageType()){

                            case MessageType.BASIC_MESSAGE:

                                NotificationUtil.sendMessageNot(MessageServiceT.this,messagObtian.getFromUid(),messagObtian.getContent());
                                MessageListDao messageListDao = new MessageListDao(MessageServiceT.this);
                                messageListDao.addMessageListUser(messagObtian);
                                Friend friend = friendDao.getUser(messagObtian.getFromUid());
                                Intent i = new Intent("cn.shilight.myapplication.Received");
                                i.putExtra("form",messagObtian.getFromUid());
                                i.putExtra("content",messagObtian.getContent());


                                if(friend!=null){
                                    // TODO: 2023/5/12  完成消息的存储
                                    MessageAllDao messageAllDao = new MessageAllDao(MessageServiceT.this);
                                    messageAllDao.saveMessage(new MessageView(messagObtian.getContent(),messagObtian.getFromUid(),messagObtian.getForUid(), BasicUtil.getTime(),MessageView.SHE_MESSAGE));
                                }

                                sendBroadcast(i);

                                break;

                            case MessageType.NEW_FRIEND_MESSAGE:
                                NewFriend nf = ScannerUtil.parsh(messagObtian.getContent());
                                friendDao.addUser(new Friend(nf.getUUID(),nf.getName(),nf.getWords(), nf.getTx()));
                                NotificationUtil.sendNewFriendNot(MessageServiceT.this,nf.getName());
                                break;

                            default:break;
                        }

                    } catch (IOException e) {
                        Log.e("MessageServeT","RecMessageError 接受消息发生IO错误");
                       // client = null;
                        flags = 0;

                    } catch (ClassNotFoundException e) {
                        Log.e("MessageServeT","SendMessageClassNotFoundExceptionError");
                    }

                }
            }

        }
    }

    // 发送消息线程
    class MessageOutThread extends Thread{

        public MessageOutThread() {

            super();

        }

        public MessageOutThread(Socket client) {
        }

        @Override
        public synchronized void start() {
            super.start();
            Log.d("MessageServer","发送线程启动");

        }

        @Override
        public void run() {
            super.run();
            MessagObtian messagObtian = null;
            while (true){

                if(flags!=0){
                    try {
                            if(count==0){
                                Log.e("MessageServeT","发送消息头");
                                MyData myData = SettingUtil.getMydata(MessageServiceT.this);
                                out.writeObject(new MessagObtian("##########################",myData.getUid(),99).setContent("online"));
                                out.flush();
                                count++;
                            }
                            Thread.sleep(50);

                            if (messagesQueue.size()>0){
                                Log.e("MessageServeT","目前消息队列长度 "+String.valueOf(messagesQueue.size()));

                                messagObtian = messagesQueue.poll();

                                if(messagObtian!=null){

                                    Log.e("MessageServeT","开始发送消息");

                                    out.writeObject(messagObtian);
                                    out.flush();

                                    if(messagObtian.getMessageType()==MessageType.BASIC_MESSAGE){

                                        Log.i("MessageServeT","已经发送消息 到："+messagObtian.getFromUid()+" 内容："+messagObtian.getContent());

                                        MessageListDao messageListDao = new MessageListDao(MessageServiceT.this);
                                        messageListDao.addMessageListUser(new MessagObtian(messagObtian.getFromUid(),messagObtian.getForUid(),messagObtian.getMessageType()).setContent(messagObtian.getContent()));

                                        MessageAllDao messageAllDao = new MessageAllDao(MessageServiceT.this);
                                        messageAllDao.saveMessage(new MessageView(messagObtian.getContent(),messagObtian.getForUid(),messagObtian.getFromUid(),BasicUtil.getTime(),MessageView.MY_MESSAGE));

                                    }

                                }else {

                                    Log.e("MessageServeT","消息对象不知道为什么为空 ");

                                }

                            }

                    } catch (IOException e) {
                        Log.e("MessageServeT","发送消息IO 错误");

                        if(messagObtian!=null){
                            messagesQueue.offer(messagObtian);
                            messagObtian = null;
                        }
                        flags =0;
                        count = 0;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}