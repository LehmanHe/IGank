package cn.edu.ustc.igank;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by lehman on 2016-06-24.
 */
public class IGankApplication extends Application {
    public static Context AppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        Fresco.initialize(AppContext);
    }
}
