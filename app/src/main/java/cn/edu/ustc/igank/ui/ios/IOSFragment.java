package cn.edu.ustc.igank.ui.ios;


import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import cn.edu.ustc.igank.database.cache.IOSCache;
import cn.edu.ustc.igank.support.BaseListFragment;
import cn.edu.ustc.igank.support.adapter.AndroidAdapter;
import cn.edu.ustc.igank.support.adapter.IOSAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class IOSFragment extends BaseListFragment {


    public IOSFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onCreateCache() {
        cache=new IOSCache(handler);
    }

    @Override
    protected RecyclerView.Adapter bindAdapter() {
        return new IOSAdapter(getContext(),cache);
    }

    @Override
    protected void refreshData() {
        cache.refresh();
    }

    @Override
    protected void loadMore() {
        cache.loadMore();
    }

    @Override
    protected void loadFromCache() {
        cache.loadFromCache();
    }

    @Override
    protected boolean hasData() {
        return cache.hasData();
    }

    @Override
    protected void getArgs() {

    }

    @Override
    protected RecyclerView.LayoutManager setLayoutManager() {
        return new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    }

}
