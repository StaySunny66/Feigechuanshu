package cn.shilight.myapplication.domain;

public class SettingConfig {

    String server;
    int port;

    String password;





    public SettingConfig(String server, int port,String password) {
        this.server = server;
        this.port = port;
        this.password = password;
    }

    public SettingConfig() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
