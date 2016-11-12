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
import cn.edu.ustc.igank.model.IOSBean;
import cn.edu.ustc.igank.ui.base.WebViewActivity;

/**
 * Created by lehman on 2016/10/26.
 */

public class IOSAdapter extends BaseListAdapter<IOSBean,IOSAdapter.MyViewHolder> {

    public IOSAdapter(Context context, ICache<IOSBean> cache) {
        super(context, cache);
    }
    @Override
    public IOSAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_android, parent, false);
        IOSAdapter.MyViewHolder vh = new IOSAdapter.MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(IOSAdapter.MyViewHolder holder, int position) {
        final IOSBean iosBean=getItem(position);
        holder.title.setText(iosBean.getDesc());
        if (iosBean.getWho() == null|| iosBean.getWho() == ""){
            iosBean.setWho("佚名");
        }
        holder.author.setText(iosBean.getWho());
        holder.time.setText(iosBean.getCreatedAt());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:add the intent to start another Activity
                Intent intent =new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url",iosBean.getUrl());
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
