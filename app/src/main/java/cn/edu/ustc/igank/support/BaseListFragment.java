/*
 *  Copyright (C) 2015 MummyDing
 *
 *  This file is part of Leisure( <https://github.com/MummyDing/Leisure> )
 *
 *  Leisure is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *                             ｀
 *  Leisure is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Leisure.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.edu.ustc.igank.support;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.database.cache.ICache;


public abstract class BaseListFragment extends Fragment {

    protected View parentView;
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout refreshView;

    protected ImageView placeHolder;
    protected ProgressBar progressBar;

    protected RecyclerView.Adapter adapter;
    protected ICache cache;

    protected int mLayout = 0;
    protected int lastVisibleItem = 0;

    protected boolean withHeaderTab = false;
    protected boolean withRefreshView = true;
    protected boolean needCache = true;


    protected abstract void onCreateCache();

    protected abstract RecyclerView.Adapter bindAdapter();

    protected abstract void refreshData();

    protected abstract void loadMore();

    protected abstract void loadFromCache();

    protected abstract boolean hasData();

    protected abstract void getArgs();

    protected abstract RecyclerView.LayoutManager setLayoutManager();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setLayout();
        needCache = setCache();
        getArgs();
        parentView = inflater.inflate(R.layout.layout_common_list, container, false);
        withHeaderTab = setHeaderTab();
        withRefreshView = setRefreshView();

        progressBar = (ProgressBar) parentView.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recyclerView);
        placeHolder = (ImageView) parentView.findViewById(R.id.placeholder);

        onCreateCache();

        adapter = bindAdapter();


        mLayoutManager = setLayoutManager();

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(8));


        //滑动到底部是加载更多数据
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (lastVisibleItem + 1 >= adapter.getItemCount()) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(new int[2])[0];
                Utils.DLog(lastVisibleItem + "");
            }
        });


        refreshView = (SwipeRefreshLayout) parentView.findViewById(R.id.pull_to_refresh);

        if (withRefreshView) {
            refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshData();
                }
            });

            placeHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placeHolder.setVisibility(View.GONE);
                    refreshData();
                }
            });
        } else {
            refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshView.setRefreshing(false);
                }
            });
        }

        NetWorkUtil.readNetworkState();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadFromCache();
            }
        }).start();

        return parentView;
    }


    protected boolean setHeaderTab() {
        return true;
    }

    protected boolean setRefreshView() {
        return true;
    }

    protected boolean setCache() {
        return true;
    }

    protected void setLayout() {
        mLayout = R.layout.layout_common_list;
    }

    protected Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CONSTANT.ID_FAILURE:
                    if (isAdded()) {
                        Utils.DLog(getString(R.string.network_error));
                    }
                    break;
                case CONSTANT.ID_SUCCESS:
                    if (isAdded()) {
                        Utils.DLog(getString(R.string.refresh_success));
                    }
                    if (needCache) {
                        cache.cache();
                    }
                    break;
                case CONSTANT.ID_FROM_CACHE:
                    if (withRefreshView && hasData() == false) {
                        refreshData();
                        return false;
                    }
                    break;
            }
            if (withRefreshView) {
                refreshView.setRefreshing(false);
            }

            if (hasData()) {
                placeHolder.setVisibility(View.GONE);
            } else {
                placeHolder.setVisibility(View.VISIBLE);
            }

            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            return false;
        }
    });


}
