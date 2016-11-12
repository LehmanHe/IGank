package cn.edu.ustc.igank.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.database.cache.ICache;
import cn.edu.ustc.igank.model.AndroidBean;
import cn.edu.ustc.igank.model.ExtendBean;
import cn.edu.ustc.igank.ui.base.WebViewActivity;

/**
 * Created by lehman on 2016/10/26.
 */

public class ExtendAdapter extends BaseListAdapter<ExtendBean,ExtendAdapter.MyViewHolder>{

    public ExtendAdapter(Context context, ICache<ExtendBean> cache) {
        super(context, cache);
    }

    @Override
    public ExtendAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_android, parent, false);
        ExtendAdapter.MyViewHolder vh = new ExtendAdapter.MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ExtendAdapter.MyViewHolder holder, int position) {
        final ExtendBean extendBean=getItem(position);
        holder.title.setText(extendBean.getDesc());
        if (extendBean.getWho() == null|| extendBean.getWho() == ""){
            extendBean.setWho("佚名");
        }
        holder.author.setText(extendBean.getWho());
        holder.time.setText(extendBean.getCreatedAt());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:add the intent to start another Activity
                Intent intent =new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url",extendBean.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private TextView title;
        private TextView author;
        private TextView time;
        public MyViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            title = (TextView) parentView.findViewById(R.id.item_android_title);
            author= (TextView) parentView.findViewById(R.id.item_android_author);
            time= (TextView) parentView.findViewById(R.id.item_android_time);
        }
    }
}
