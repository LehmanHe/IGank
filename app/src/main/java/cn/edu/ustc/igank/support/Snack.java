package cn.edu.ustc.igank.support;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

/**
 * Created by lehman.
 * Snackbar 統一管理類
 */
public class Snack {

    public static boolean isShow = true;
    private static Snackbar snackbar=null;


    public static void showShort(String message)
    {
        if (isShow) {
            if (snackbar==null){
                snackbar=Snackbar.make(null,message,Snackbar.LENGTH_SHORT);
            }
            else {
                snackbar.setText(message);
            }
            snackbar.show();
        }
    }


    public static void showLong(String message)
    {
        if (isShow) {
            if (snackbar==null){
                snackbar=Snackbar.make(null,message,Snackbar.LENGTH_LONG);
            }
            else {
                snackbar.setText(message);
            }
            snackbar.show();
        }
    }
    
}
