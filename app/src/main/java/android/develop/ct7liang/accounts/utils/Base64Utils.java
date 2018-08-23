package android.develop.ct7liang.accounts.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/10/12.
 *  Base64处理工具类
 *   需要权限:
 *  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
 *  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 */
public class Base64Utils {

    private static final String SALT = "悼良会之永诀兮,哀一逝而异乡";
    private static final String SALT_BASE64 = "5oK86Imv5Lya5LmL5rC46K+A5YWuLOWTgOS4gOmAneiAjOW8guS5oQ==\n";
    private static final String SALT_BASE64_RECOVER = "\n==Qo5Sug8WOjAienAmOg4SOgTWOLuWY5A+K64Cr5LmL5ayL5vmI68Ko5";

    /**
     * File转base64字符串
     * @param file File
     * @return base64字符串
     */
    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64;
    }

    /**
     * base64字符串转File
     * @param base64 base64字符串
     * @param file 输出文件
     * @return File
     */
    public static File base64ToFile(String base64, File file) {
        FileOutputStream out = null;
        try {
            // 解码，然后将字节转换为文件
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);// 将字符串转换为byte数组
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(file);
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread); // 文件写操作
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (out!= null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * bitmap转为base64字符串
     * @param bitmap Bitmap
     * @return base64字符串
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64字符串转为bitmap
     * @param base64Data base64字符串
     * @return Bitmap
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



    /**
     * 加密数据规则:
     *     1.先对原数据进行Base64加密
     *     2.如果长度为1: 在数据前加入字符串
     *     3.如果长度大于1: 将第一个字符放入最后一位, 并在最后一位之前加入字符串
     */
    public static String StringToBase64(String str){
        String s1 = string2base64(str);
        if (s1.length() == 1){
            return SALT_BASE64_RECOVER + s1;
        }else{
            return s1.substring(1) + SALT_BASE64_RECOVER + s1.charAt(0);
        }
    }

    /**
     * 字符串进行Base64编码
     * @param str
     */
    public static String string2base64(String str){
        String encodedString = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        return encodedString;
    }

    /**
     * 解密数据规则:
     *     1.如果长度为1+字符串长度: 取最后一位,然后用Base64解密即可
     *     2.如果长度大于1+字符串长度: 将最后一位放入第一位, 再删除最后的字符串
     */
    public static String Base64ToString(String string){
        int length = string.length();
        if (length == 1+SALT_BASE64_RECOVER.length()){
            return Base64Utils.base642string(string.charAt(length-1)+"");
        }else{
            String s = string.charAt(length-1) + string.substring(0, length-1);
            return Base64Utils.base642string(s.replace(SALT_BASE64_RECOVER, ""));
        }
    }

    /**
     * 字符串进行Base64解码
     * @param encodedString
     * @return
     */
    public static String base642string(String encodedString){
        String decodedString =new String(Base64.decode(encodedString,Base64.DEFAULT));
        return decodedString;
    }

}