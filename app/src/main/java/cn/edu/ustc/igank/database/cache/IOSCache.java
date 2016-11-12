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
import cn.edu.ustc.igank.database.table.IOSTable;
import cn.edu.ustc.igank.model.IOSBean;
import cn.edu.ustc.igank.model.IOSBean;
import cn.edu.ustc.igank.support.CONSTANT;
import cn.edu.ustc.igank.support.NetWorkUtil;
import cn.edu.ustc.igank.support.Snack;
import cn.edu.ustc.igank.support.Utils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Lehman on 2016-07-03.
 */
public class IOSCache extends BaseCache<IOSBean> {
    private int currentPage = 1;

    public IOSCache(Handler handler) {
        super(handler);
    }

    @Override
    protected void putData() {

        db.execSQL(mHelper.DELETE_TABLE_DATA + IOSTable.NAME);
        // db.execSQL(table.CREATE_TABLE);
        for (int i = 0; i < mList.size(); i++) {
            IOSBean iosBean = mList.get(i);
            values.put(IOSTable.ID, iosBean.get_id()+"");
            values.put(IOSTable.CREATED_AT, iosBean.getCreatedAt());
            values.put(IOSTable.DESC, iosBean.getDesc());
            values.put(IOSTable.PUBLISHED_AT, iosBean.getPublishedAt());
            values.put(IOSTable.SOURCE, iosBean.getSource());
            values.put(IOSTable.TYPE, iosBean.getType());
            values.put(IOSTable.URL, iosBean.getUrl());
            values.put(IOSTable.USED, iosBean.isUsed()?1:0);
            values.put(IOSTable.WHO, iosBean.getWho());

            db.insert(IOSTable.NAME, null, values);
        }

    }

    @Override
    protected void putData(IOSBean iosBean) {

        values.put(IOSTable.ID, iosBean.get_id());
        values.put(IOSTable.CREATED_AT, iosBean.getCreatedAt());
        values.put(IOSTable.DESC, iosBean.getDesc());
        values.put(IOSTable.PUBLISHED_AT, iosBean.getPublishedAt());
        values.put(IOSTable.SOURCE, iosBean.getSource());
        values.put(IOSTable.TYPE, iosBean.getType());
        values.put(IOSTable.URL, iosBean.getUrl());
        values.put(IOSTable.USED, iosBean.isUsed());
        values.put(IOSTable.WHO, iosBean.getWho());

        db.insert(IOSTable.NAME, null, values);
    }

    @Override
    public void refresh() {
        AsyncHttpClient client = NetWorkUtil.getInstance();
        client.get(GankAPI.getIosAPI(CONSTANT.QUANTITY_OF_DATA, 1), new JsonHttpResponseHandler() {
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
                            mList.add(gson.fromJson(array.get(i).toString(), IOSBean.class));
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
        client.get(GankAPI.getIosAPI(CONSTANT.QUANTITY_OF_DATA, currentPage + 1), new JsonHttpResponseHandler() {
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
                            mList.add(gson.fromJson(array.get(i).toString(), IOSBean.class));
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

        String sql = "select * from " + IOSTable.NAME + " order by " + IOSTable.ID + " desc";

        Cursor cursor = query(sql);
        while (cursor.moveToNext()) {
            IOSBean iosBean = new IOSBean();
            iosBean.set_id(cursor.getString(IOSTable.ID_ID));
            iosBean.setCreatedAt(cursor.getString(IOSTable.ID_CREATED_AT));
            iosBean.setDesc(cursor.getString(IOSTable.ID_DESC));
            iosBean.setPublishedAt(cursor.getString(IOSTable.ID_PUBLISHED_AT));
            iosBean.setSource(cursor.getString(IOSTable.ID_SOURCE));
            iosBean.setType(cursor.getString(IOSTable.ID_TYPE));
            iosBean.setUrl(cursor.getString(IOSTable.ID_URL));
            iosBean.setUsed(cursor.getInt(IOSTable.ID_USED) == 1 ? true : false);
            iosBean.setWho(cursor.getString(IOSTable.ID_WHO));

            mList.add(iosBean);
        }

        // notify
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        cursor.close();
    }
}
