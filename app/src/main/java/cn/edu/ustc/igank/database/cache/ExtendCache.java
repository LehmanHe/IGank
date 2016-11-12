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
import cn.edu.ustc.igank.database.table.ExtendTable;
import cn.edu.ustc.igank.model.ExtendBean;
import cn.edu.ustc.igank.support.CONSTANT;
import cn.edu.ustc.igank.support.NetWorkUtil;
import cn.edu.ustc.igank.support.Snack;
import cn.edu.ustc.igank.support.Utils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Lehman on 2016-07-03.
 */
public class ExtendCache extends BaseCache<ExtendBean> {
    private int currentPage = 1;

    public ExtendCache(Handler handler) {
        super(handler);
    }

    @Override
    protected void putData() {

        db.execSQL(mHelper.DELETE_TABLE_DATA + ExtendTable.NAME);
        // db.execSQL(table.CREATE_TABLE);
        for (int i = 0; i < mList.size(); i++) {
            ExtendBean extendBean = mList.get(i);
            values.put(ExtendTable.ID, extendBean.get_id()+"");
            values.put(ExtendTable.CREATED_AT, extendBean.getCreatedAt());
            values.put(ExtendTable.DESC, extendBean.getDesc());
            values.put(ExtendTable.PUBLISHED_AT, extendBean.getPublishedAt());
            values.put(ExtendTable.SOURCE, extendBean.getSource());
            values.put(ExtendTable.TYPE, extendBean.getType());
            values.put(ExtendTable.URL, extendBean.getUrl());
            values.put(ExtendTable.USED, extendBean.isUsed()?1:0);
            values.put(ExtendTable.WHO, extendBean.getWho());

            db.insert(ExtendTable.NAME, null, values);
        }

    }

    @Override
    protected void putData(ExtendBean extendBean) {

        values.put(ExtendTable.ID, extendBean.get_id());
        values.put(ExtendTable.CREATED_AT, extendBean.getCreatedAt());
        values.put(ExtendTable.DESC, extendBean.getDesc());
        values.put(ExtendTable.PUBLISHED_AT, extendBean.getPublishedAt());
        values.put(ExtendTable.SOURCE, extendBean.getSource());
        values.put(ExtendTable.TYPE, extendBean.getType());
        values.put(ExtendTable.URL, extendBean.getUrl());
        values.put(ExtendTable.USED, extendBean.isUsed());
        values.put(ExtendTable.WHO, extendBean.getWho());

        db.insert(ExtendTable.NAME, null, values);
    }

    @Override
    public void refresh() {
        AsyncHttpClient client = NetWorkUtil.getInstance();
        client.get(GankAPI.getExtendAPI(CONSTANT.QUANTITY_OF_DATA, 1), new JsonHttpResponseHandler() {
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
                            mList.add(gson.fromJson(array.get(i).toString(), ExtendBean.class));
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
                Snack.showShort(R.string.network_error + "");
            }
        });
    }

    @Override
    public void loadMore() {
        AsyncHttpClient client = NetWorkUtil.getInstance();
        client.get(GankAPI.getExtendAPI(CONSTANT.QUANTITY_OF_DATA, currentPage + 1), new JsonHttpResponseHandler() {
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
                            mList.add(gson.fromJson(array.get(i).toString(), ExtendBean.class));
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

        String sql = "select * from " + ExtendTable.NAME + " order by " + ExtendTable.ID + " desc";

        Cursor cursor = query(sql);
        while (cursor.moveToNext()) {
            ExtendBean extendBean = new ExtendBean();
            extendBean.set_id(cursor.getString(ExtendTable.ID_ID));
            extendBean.setCreatedAt(cursor.getString(ExtendTable.ID_CREATED_AT));
            extendBean.setDesc(cursor.getString(ExtendTable.ID_DESC));
            extendBean.setPublishedAt(cursor.getString(ExtendTable.ID_PUBLISHED_AT));
            extendBean.setSource(cursor.getString(ExtendTable.ID_SOURCE));
            extendBean.setType(cursor.getString(ExtendTable.ID_TYPE));
            extendBean.setUrl(cursor.getString(ExtendTable.ID_URL));
            extendBean.setUsed(cursor.getInt(ExtendTable.ID_USED) == 1 ? true : false);
            extendBean.setWho(cursor.getString(ExtendTable.ID_WHO));

            mList.add(extendBean);
        }

        // notify
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        cursor.close();
    }
}
