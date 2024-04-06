package cn.shilight.myapplication.domain;

public class MyData {
    private String uid;
    private String password;
    private String name;
    private String word;

    private int tx_id;

    public MyData() {
    }

    public MyData(String uid, String password, String name, String word,int tx_id) {
        this.uid = uid;
        this.password = password;
        this.name = name;
        this.word = word;
        this.tx_id = tx_id;
    }


    public int getTx_id() {
        return tx_id;
    }

    public void setTx_id(int tx_id) {
        this.tx_id = tx_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getQRString(){

        return "01"+"#"+uid+"#"+name+"#"+word+"#"+String.valueOf(tx_id);

    }
}
