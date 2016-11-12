package cn.edu.ustc.igank.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.database.cache.ICache;
import cn.edu.ustc.igank.database.table.AndroidTable;
import cn.edu.ustc.igank.model.AndroidBean;
import cn.edu.ustc.igank.ui.base.WebViewActivity;

/**
 * Created by lehman on 16/8/4.
 */
public class AndroidAdapter extends BaseListAdapter<AndroidBean,AndroidAdapter.MyViewHolder> {

    public AndroidAdapter(Context context, ICache<AndroidBean> cache) {
        super(context, cache);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_android, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AndroidBean androidBean=getItem(position);
        holder.title.setText(androidBean.getDesc());
        if (androidBean.getWho() == null|| androidBean.getWho() == ""){
            androidBean.setWho("佚名");
        }
        holder.author.setText(androidBean.getWho());
        holder.time.setText(androidBean.getCreatedAt());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:add the intent to start another Activity
                Intent intent =new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url",androidBean.getUrl());
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
