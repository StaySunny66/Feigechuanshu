package cn.shilight.myapplication.domain;

import android.graphics.Bitmap;

public class MessageView {

    private String content;
    private String fromUid;
    private String forUid;
    private long timeS;

    private int MessageType;

    public final static int MY_MESSAGE = 0;
    public final static int SHE_MESSAGE = 1;


    public MessageView(String content, String fromUid, String forUid, long timeS, int messageType) {
        this.content = content;
        this.fromUid = fromUid;
        this.forUid = forUid;
        this.timeS = timeS;
        MessageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getForUid() {
        return forUid;
    }

    public void setForUid(String forUid) {
        this.forUid = forUid;
    }

    public long getTimeS() {
        return timeS;
    }

    public void setTimeS(long timeS) {
        this.timeS = timeS;
    }

    public int getMessageType() {
        return MessageType;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }
}
