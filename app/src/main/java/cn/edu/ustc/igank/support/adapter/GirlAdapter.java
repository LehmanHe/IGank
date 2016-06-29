package cn.edu.ustc.igank.support.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.database.cache.ICache;
import cn.edu.ustc.igank.model.GirlBean;
import cn.edu.ustc.igank.support.NetWorkUtil;
import cn.edu.ustc.igank.support.Settings;
import cn.edu.ustc.igank.ui.girl.GirlActivity;


/**
 * Created by Lehman on 2016-06-25.
 */
public class GirlAdapter extends BaseListAdapter<GirlBean,GirlAdapter.MyViewHolder>{


    public GirlAdapter(Context context, ICache<GirlBean> cache) {
        super(context, cache);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_girl, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final GirlBean girlBean = (GirlBean) getItem(position);
        holder.title.setText(girlBean.getDesc());

//        if(Settings.noPicMode && NetWorkUtil.isWIFI == false){
//            //holder.image.setImageURI(null);
//        }else {
            holder.image.setImageURI(Uri.parse(girlBean.getUrl()));
//        }
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GirlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",girlBean.getUrl());
                bundle.putString("_id",girlBean.get_id());
                bundle.putString("desc",girlBean.getDesc());

                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private TextView title;
        private SimpleDraweeView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            title = (TextView) parentView.findViewById(R.id.item_girl_text);
            image = (SimpleDraweeView) parentView.findViewById(R.id.item_girl_image);
        }
    }
}
