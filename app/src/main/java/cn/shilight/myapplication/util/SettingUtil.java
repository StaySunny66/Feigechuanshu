package cn.shilight.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import cn.shilight.myapplication.R;
import cn.shilight.myapplication.domain.MyData;
import cn.shilight.myapplication.domain.SettingConfig;

public class SettingUtil {


    public static void saveServerSetting(Context context, SettingConfig sf){

        SharedPreferences sp = context.getSharedPreferences("Setting.conf",Context.MODE_PRIVATE);
        SharedPreferences.Editor esp = sp.edit();
        esp.putString("server", sf.getServer());
        esp.putInt("port", sf.getPort());
        esp.putString("password", sf.getPassword());
        esp.apply();

    }


    public static SettingConfig getServerSetting(Context context){

        SharedPreferences sp = context.getSharedPreferences("Setting.conf",Context.MODE_PRIVATE);
        if(!"".equals(sp.getString("server",""))){
            SettingConfig sf = new SettingConfig();
            sf.setServer(sp.getString("server",""));
            sf.setPort(sp.getInt("port",0));
            sf.setPassword(sp.getString("password",""));
            return sf;
        }
        return null;
    }




    public static void saveMydata(Context context, MyData myData){

        SharedPreferences sp = context.getSharedPreferences("My.conf",Context.MODE_PRIVATE);
        SharedPreferences.Editor esp = sp.edit();
        esp.putString("name", myData.getName());
        esp.putString("password", myData.getPassword());
        esp.putString("uid", myData.getUid());
        esp.putString("word", myData.getWord());
        esp.putInt("tx",myData.getTx_id());
        esp.apply();

    }


    public static MyData getMydata(Context context){
        SharedPreferences sp = context.getSharedPreferences("My.conf",Context.MODE_PRIVATE);
            MyData sf = new MyData();
            sf.setPassword(sp.getString("password",""));
            sf.setName(sp.getString("name",""));
            sf.setUid(sp.getString("uid",""));
            sf.setWord(sp.getString("word",""));
            sf.setTx_id(sp.getInt("tx", R.drawable.defaultmessage));

        return sf;
    }

    public static void setState(Context context,int state){
        SharedPreferences sp = context.getSharedPreferences("state.conf",Context.MODE_PRIVATE);

        SharedPreferences.Editor esp = sp.edit();
        esp.putInt("state", state);
        esp.apply();
    }


    public static int getState(Context context){
        SharedPreferences sp = context.getSharedPreferences("state.conf",Context.MODE_PRIVATE);
        return sp.getInt("state",0);
    }

    public static String getAndroidID(Context context) {
        String androidID = Settings.System.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID
        );

        return androidID;
    }










}
