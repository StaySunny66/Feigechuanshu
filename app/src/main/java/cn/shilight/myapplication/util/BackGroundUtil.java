package cn.shilight.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Random;

import cn.shilight.myapplication.R;

public class BackGroundUtil {


    static final int[] backImg={
            R.color.wx_back,
            R.drawable.back0,
            R.drawable.back1,
            R.drawable.back2,
            R.drawable.back3,
            R.drawable.back4,
            R.drawable.back5,
            R.drawable.back6,
            R.drawable.back7,
            R.drawable.back8,
            R.drawable.back9,
            R.drawable.back10,
            R.drawable.back11,
    };


    // 设定 是否需要开启 摇一摇换 壁纸
    public static void setEnable(Context context,boolean enable){

        SharedPreferences sp = context.getSharedPreferences("Setting2.conf", Context.MODE_PRIVATE);
        SharedPreferences.Editor esp = sp.edit();
        esp.putBoolean("enable", enable);
        esp.apply();

    }


    public static boolean getEnable(Context context){

        SharedPreferences sp = context.getSharedPreferences("Setting2.conf", Context.MODE_PRIVATE);
        return sp.getBoolean("enable",false);
    }

    // 获取到 当前的壁纸
    public static int getConfig(Context context){

            SharedPreferences sp = context.getSharedPreferences("theme.conf", Context.MODE_PRIVATE);
            return backImg[sp.getInt("id",0)];

    }


    public static int changeConfig(Context context){

        if(!getEnable(context)) return getConfig(context);

        Random r = new Random(BasicUtil.getTime());
        int a = r.nextInt(12);

        SharedPreferences sp = context.getSharedPreferences("theme.conf", Context.MODE_PRIVATE);
        SharedPreferences.Editor esp = sp.edit();
        esp.putInt("id", a);
        esp.apply();
        Log.e("dddd", String.valueOf(a));
        Log.e("dddd", String.valueOf(backImg[a]));
        return backImg[a];
    }



}
