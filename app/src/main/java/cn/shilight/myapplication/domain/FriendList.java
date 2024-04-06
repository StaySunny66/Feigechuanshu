package cn.shilight.myapplication.domain;

public class FriendList {
    private String uid;
    private String name;
    private int tx;

    public FriendList(String uid, String name, int tx) {
        this.uid = uid;
        this.name = name;
        this.tx = tx;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTx() {
        return tx;
    }

    public void setTx(int tx) {
        this.tx = tx;
    }
}
