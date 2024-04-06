package cn.shilight.myapplication.domain;

import android.graphics.Bitmap;

public class Message {

    private int tx;
    private String name,content,time,uid;

    public Message(int tx, String name, String content, String time,String uid) {
        this.tx = tx;
        this.name = name;
        this.content = content;
        this.time = time;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getTx() {
        return tx;
    }

    public void setTx(int tx) {
        this.tx = tx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
