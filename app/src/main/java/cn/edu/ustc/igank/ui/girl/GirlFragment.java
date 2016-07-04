package cn.edu.ustc.igank.ui.girl;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import cn.edu.ustc.igank.database.cache.GirlCache;
import cn.edu.ustc.igank.support.BaseListFragment;
import cn.edu.ustc.igank.support.adapter.GirlAdapter;

public class GirlFragment extends BaseListFragment {


    @Override
    protected void onCreateCache() {
        cache = new GirlCache(handler);
    }

    @Override
    protected RecyclerView.Adapter bindAdapter() {
        return new GirlAdapter(getContext(), cache);
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

    //设置recyclerView的LayoutManager
    @Override
    protected RecyclerView.LayoutManager setLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

}
