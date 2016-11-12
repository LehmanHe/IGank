package cn.edu.ustc.igank.database.cache;

import android.database.Cursor;
import android.os.Handler;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.api.GankAPI;
import cn.edu.ustc.igank.database.table.AndroidTable;
import cn.edu.ustc.igank.model.AndroidBean;
import cn.edu.ustc.igank.support.CONSTANT;
import cn.edu.ustc.igank.support.NetWorkUtil;
import cn.edu.ustc.igank.support.Snack;
import cn.edu.ustc.igank.support.Utils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Lehman on 2016-07-03.
 */
public class AndroidCache extends BaseCache<AndroidBean>{
    private int currentPage = 1;

    public AndroidCache(Handler handler) {
        super(handler);
    }

    @Override
    protected void putData() {

        db.execSQL(mHelper.DELETE_TABLE_DATA + AndroidTable.NAME);
        // db.execSQL(table.CREATE_TABLE);
        for (int i = 0; i < mList.size(); i++) {
            AndroidBean androidBean = mList.get(i);
            values.put(AndroidTable.ID, androidBean.get_id()+"");
            values.put(AndroidTable.CREATED_AT, androidBean.getCreatedAt());
            values.put(AndroidTable.DESC, androidBean.getDesc());
            values.put(AndroidTable.PUBLISHED_AT, androidBean.getPublishedAt());
            values.put(AndroidTable.SOURCE, androidBean.getSource());
            values.put(AndroidTable.TYPE, androidBean.getType());
            values.put(AndroidTable.URL, androidBean.getUrl());
            values.put(AndroidTable.USED, androidBean.isUsed()?1:0);
            values.put(AndroidTable.WHO, androidBean.getWho());

            db.insert(AndroidTable.NAME, null, values);
        }

    }

    @Override
    protected void putData(AndroidBean androidBean) {

        values.put(AndroidTable.ID, androidBean.get_id());
        values.put(AndroidTable.CREATED_AT, androidBean.getCreatedAt());
        values.put(AndroidTable.DESC, androidBean.getDesc());
        values.put(AndroidTable.PUBLISHED_AT, androidBean.getPublishedAt());
        values.put(AndroidTable.SOURCE, androidBean.getSource());
        values.put(AndroidTable.TYPE, androidBean.getType());
        values.put(AndroidTable.URL, androidBean.getUrl());
        values.put(AndroidTable.USED, androidBean.isUsed());
        values.put(AndroidTable.WHO, androidBean.getWho());

        db.insert(AndroidTable.NAME, null, values);
    }

    @Override
    public void refresh() {
        AsyncHttpClient client = NetWorkUtil.getInstance();
        client.get(GankAPI.getAndroidAPI(CONSTANT.QUANTITY_OF_DATA, 1), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean error = response.getBoolean("error");
                    if (error) {
                        Snack.showShort(R.string.network_error + "");
                    } else {
                        JSONArray array = response.getJSONArray("results");
                        mList.clear();
                        Gson gson = new Gson();
                        for (int i = 0; i < array.length(); ++i) {
                            mList.add(gson.fromJson(array.get(i).toString(), AndroidBean.class));
                        }

                        // notify
                        mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
                    }
                    //Snack.showShort("2222222");
                    Utils.DLog("3333333333333333");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Snack.showShort(R.string.network_error + "");
                // notify
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                //Snack.showShort(R.string.network_error + "");
                // notify
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }
        });
    }

    @Override
    public void loadMore() {
        AsyncHttpClient client = NetWorkUtil.getInstance();
        client.get(GankAPI.getAndroidAPI(CONSTANT.QUANTITY_OF_DATA, currentPage + 1), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean error = response.getBoolean("error");
                    if (error) {
                        Snack.showShort(R.string.network_error + "");
                    } else {
                        ++currentPage;
                        JSONArray array = response.getJSONArray("results");
                        Gson gson = new Gson();
                        for (int i = 0; i < array.length(); ++i) {
                            mList.add(gson.fromJson(array.get(i).toString(), AndroidBean.class));
                        }

                        // notify
                        mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Snack.showShort(R.string.network_error + "");
            }
        });
    }

    @Override
    public synchronized void loadFromCache() {
        mList.clear();

        String sql = "select * from " + AndroidTable.NAME + " order by " + AndroidTable.ID + " desc";

        Cursor cursor = query(sql);
        while (cursor.moveToNext()) {
            AndroidBean androidBean = new AndroidBean();
            androidBean.set_id(cursor.getString(AndroidTable.ID_ID));
            androidBean.setCreatedAt(cursor.getString(AndroidTable.ID_CREATED_AT));
            androidBean.setDesc(cursor.getString(AndroidTable.ID_DESC));
            androidBean.setPublishedAt(cursor.getString(AndroidTable.ID_PUBLISHED_AT));
            androidBean.setSource(cursor.getString(AndroidTable.ID_SOURCE));
            androidBean.setType(cursor.getString(AndroidTable.ID_TYPE));
            androidBean.setUrl(cursor.getString(AndroidTable.ID_URL));
            androidBean.setUsed(cursor.getInt(AndroidTable.ID_USED) == 1 ? true : false);
            androidBean.setWho(cursor.getString(AndroidTable.ID_WHO));

            mList.add(androidBean);
        }

        // notify
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        cursor.close();
    }
}
