package cn.edu.ustc.igank.ui.android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.database.cache.AndroidCache;
import cn.edu.ustc.igank.support.BaseListFragment;
import cn.edu.ustc.igank.support.adapter.AndroidAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AndroidFragment extends BaseListFragment {


    public AndroidFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onCreateCache() {
        cache=new AndroidCache(handler);
    }

    @Override
    protected RecyclerView.Adapter bindAdapter() {
        return new AndroidAdapter(getContext(),cache);
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
