package cn.shilight.myapplication.util;

import android.util.Log;

import java.util.List;

import cn.shilight.myapplication.domain.NewFriend;

public class ScannerUtil {

    public static NewFriend parsh(String result){

        String[] sarr = result.split("#");
        Log.i("ScannerResult", String.valueOf(sarr.length));
        if (sarr.length == 5 ) {
            return  new NewFriend(sarr[1],sarr[2],sarr[3],Integer.parseInt(sarr[4]));

        }else{

            return  null;

        }










    }
}
