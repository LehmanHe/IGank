package cn.edu.ustc.igank.support;

/**
 * Created by Administrator on 2016/1/26.
 */
import android.content.Context;
import android.net.ConnectivityManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import cn.edu.ustc.igank.IGankApplication;

/**
 * Created by Administrator on 2015/10/28.
 */
public class NetWorkUtil {

    public static boolean isWIFI = true;
    private static AsyncHttpClient client;

    public static AsyncHttpClient getInstance() {
        if (client == null) {
            client = new AsyncHttpClient();
        }
        return client;
    }


    public static boolean readNetworkState() {

        ConnectivityManager cm = (ConnectivityManager) IGankApplication.AppContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            isWIFI = (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
            return true;
        } else {

            return false;
        }
    }

}
