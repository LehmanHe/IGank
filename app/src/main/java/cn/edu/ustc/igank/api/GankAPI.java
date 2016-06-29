package cn.edu.ustc.igank.api;

/**
 * Created by Lehman on 2016-06-25.
 */
public class GankAPI {
    private static String girlAPI="http://gank.io/api/data/福利/";
    private static String androidAPI="http://gank.io/api/data/Android/";
    private static String iosAPI="http://gank.io/api/data/iOS/";
    private static String webDesignAPI="http://gank.io/api/data/前端/";
    private static String extendAPI="http://gank.io/api/data/拓展资源/";

    public static String getGirlAPI(int quantity,int pages) {
        return girlAPI+quantity+"/"+pages;
    }

    public static String getAndroidAPI(int quantity,int pages) {
        return androidAPI+quantity+"/"+pages;
    }

    public static String getIosAPI(int quantity,int pages) {
        return iosAPI+quantity+"/"+pages;
    }

    public static String getWebDesignAPI(int quantity,int pages) {
        return webDesignAPI+quantity+"/"+pages;
    }

    public static String getExtendAPI(int quantity,int pages) {
        return extendAPI+quantity+"/"+pages;
    }
}
