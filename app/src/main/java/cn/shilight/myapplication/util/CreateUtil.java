package cn.shilight.myapplication.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class CreateUtil {
    //String codestring：要生成二维码的字符串
    // int width：二维码图片的宽度
    // int height：二维码图片的高度
    public static Bitmap createQRCode(String codestring,int width,int height){
        try {
            //首先判断参数的合法性，要求字符串内容不能为空或图片长宽必须大于0
            if (TextUtils.isEmpty(codestring)||width<=0||height<=0){
                return null;
            }
            width++;
            height++;
            //设置二维码的相关参数，生成BitMatrix（位矩阵）对象
            Hashtable<EncodeHintType,String> hashtable=new Hashtable<>();
            hashtable.put(EncodeHintType.CHARACTER_SET,"utf-8");  //设置字符转码格式
            hashtable.put(EncodeHintType.ERROR_CORRECTION,"H");   //设置容错级别
            hashtable.put(EncodeHintType.MARGIN,"2"); //设置空白边距
            //encode需要抛出和处理异常
            BitMatrix bitMatrix=new QRCodeWriter().encode(codestring, BarcodeFormat.QR_CODE,width,height,hashtable);
            //再创建像素数组，并根据位矩阵为数组元素赋颜色值
            int[] pixel=new int[width*width];
            for (int h=0;h<height;h++){
                for (int w=0;w<width;w++){
                    if (bitMatrix.get(w,h)){
                        pixel[h*width+w]= Color.BLACK;  //设置黑色色块
                    }else{
                        pixel[h*width+w]=Color.WHITE;  //设置白色色块
                    }
                }
            }
            //创建bitmap对象
            //根据像素数组设置Bitmap每个像素点的颜色值，之后返回Bitmap对象
            Bitmap qrcodemap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
            qrcodemap.setPixels(pixel,0,width,0,0,width,height);
            return qrcodemap;
        }catch (WriterException e){
            return null;
        }
    }
}
