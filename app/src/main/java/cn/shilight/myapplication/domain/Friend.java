package cn.shilight.myapplication.domain;

public class Friend {

    private String uid;
    private String name;
    private String words;
    private int tx_id;


    public Friend(String uid, String name, String words, int tx_id) {
        this.uid = uid;
        this.name = name;
        this.words = words;
        this.tx_id = tx_id;
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

    public String getWords() {
        return words;
    }

    public void setWords(String phone) {
        this.words = words;
    }

    public int getTx_id() {
        return tx_id;
    }

    public void setTx_id(int tx_id) {
        this.tx_id = tx_id;
    }
}
