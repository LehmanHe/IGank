package cn.edu.ustc.igank.database.cache;

import java.util.List;

/**
 * Created by Lehman on 2016-06-25.
 */
public interface ICache<T>{
    void execSQL(String sql);
    List<T> getmList();
    boolean hasData();
    void refresh();
    void loadMore();
    void loadFromCache();
    void cache();
}