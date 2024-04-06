package cn.shilight.myapplication.domain;

public class NewFriend {

    String UUID ;
    String name;
    String words;

    int tx;

    public NewFriend(String UUID, String name, String words, int tx) {
        this.UUID = UUID;
        this.name = name;
        this.words = words;
        this.tx = tx;
    }

    public int getTx() {
        return tx;
    }

    public void setTx(int tx) {
        this.tx = tx;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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

    public void setWords(String words) {
        this.words = words;
    }
}
