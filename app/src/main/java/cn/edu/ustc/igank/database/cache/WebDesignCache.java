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
import cn.edu.ustc.igank.database.table.WebDesignTable;
import cn.edu.ustc.igank.model.WebDesignBean;
import cn.edu.ustc.igank.model.WebDesignBean;
import cn.edu.ustc.igank.support.CONSTANT;
import cn.edu.ustc.igank.support.NetWorkUtil;
import cn.edu.ustc.igank.support.Snack;
import cn.edu.ustc.igank.support.Utils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Lehman on 2016-07-03.
 */
public class WebDesignCache extends BaseCache<WebDesignBean> {
    private int currentPage = 1;

    public WebDesignCache(Handler handler) {
        super(handler);
    }

    @Override
    protected void putData() {

        db.execSQL(mHelper.DELETE_TABLE_DATA + WebDesignTable.NAME);
        // db.execSQL(table.CREATE_TABLE);
        for (int i = 0; i < mList.size(); i++) {
            WebDesignBean webDesignBean = mList.get(i);
            values.put(WebDesignTable.ID, webDesignBean.get_id()+"");
            values.put(WebDesignTable.CREATED_AT, webDesignBean.getCreatedAt());
            values.put(WebDesignTable.DESC, webDesignBean.getDesc());
            values.put(WebDesignTable.PUBLISHED_AT, webDesignBean.getPublishedAt());
            values.put(WebDesignTable.SOURCE, webDesignBean.getSource());
            values.put(WebDesignTable.TYPE, webDesignBean.getType());
            values.put(WebDesignTable.URL, webDesignBean.getUrl());
            values.put(WebDesignTable.USED, webDesignBean.isUsed()?1:0);
            values.put(WebDesignTable.WHO, webDesignBean.getWho());

            db.insert(WebDesignTable.NAME, null, values);
        }

    }

    @Override
    protected void putData(WebDesignBean webDesignBean) {

        values.put(WebDesignTable.ID, webDesignBean.get_id());
        values.put(WebDesignTable.CREATED_AT, webDesignBean.getCreatedAt());
        values.put(WebDesignTable.DESC, webDesignBean.getDesc());
        values.put(WebDesignTable.PUBLISHED_AT, webDesignBean.getPublishedAt());
        values.put(WebDesignTable.SOURCE, webDesignBean.getSource());
        values.put(WebDesignTable.TYPE, webDesignBean.getType());
        values.put(WebDesignTable.URL, webDesignBean.getUrl());
        values.put(WebDesignTable.USED, webDesignBean.isUsed());
        values.put(WebDesignTable.WHO, webDesignBean.getWho());

        db.insert(WebDesignTable.NAME, null, values);
    }

    @Override
    public void refresh() {
        AsyncHttpClient client = NetWorkUtil.getInstance();
        client.get(GankAPI.getWebDesignAPI(CONSTANT.QUANTITY_OF_DATA, 1), new JsonHttpResponseHandler() {
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
                            mList.add(gson.fromJson(array.get(i).toString(), WebDesignBean.class));
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
        client.get(GankAPI.getWebDesignAPI(CONSTANT.QUANTITY_OF_DATA, currentPage + 1), new JsonHttpResponseHandler() {
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
                            mList.add(gson.fromJson(array.get(i).toString(), WebDesignBean.class));
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

        String sql = "select * from " + WebDesignTable.NAME + " order by " + WebDesignTable.ID + " desc";

        Cursor cursor = query(sql);
        while (cursor.moveToNext()) {
            WebDesignBean webDesignBean = new WebDesignBean();
            webDesignBean.set_id(cursor.getString(WebDesignTable.ID_ID));
            webDesignBean.setCreatedAt(cursor.getString(WebDesignTable.ID_CREATED_AT));
            webDesignBean.setDesc(cursor.getString(WebDesignTable.ID_DESC));
            webDesignBean.setPublishedAt(cursor.getString(WebDesignTable.ID_PUBLISHED_AT));
            webDesignBean.setSource(cursor.getString(WebDesignTable.ID_SOURCE));
            webDesignBean.setType(cursor.getString(WebDesignTable.ID_TYPE));
            webDesignBean.setUrl(cursor.getString(WebDesignTable.ID_URL));
            webDesignBean.setUsed(cursor.getInt(WebDesignTable.ID_USED) == 1 ? true : false);
            webDesignBean.setWho(cursor.getString(WebDesignTable.ID_WHO));

            mList.add(webDesignBean);
        }

        // notify
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        cursor.close();
    }
}
