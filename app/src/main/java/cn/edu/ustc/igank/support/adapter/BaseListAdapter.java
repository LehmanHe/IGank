package cn.edu.ustc.igank.support.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;


import java.util.List;

import cn.edu.ustc.igank.database.cache.ICache;
import cn.edu.ustc.igank.support.NetWorkUtil;

public abstract class BaseListAdapter<M,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    protected List<M> mItems;
    protected Context mContext;
    protected ICache<M> mCache;

    public BaseListAdapter(Context context, ICache<M> cache) {
        mContext = context;
        mCache = cache;
        mItems = cache.getmList();

        NetWorkUtil.readNetworkState();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    protected M getItem(int position){
        return mItems.get(position);
    }


}
