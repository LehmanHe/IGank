package cn.edu.ustc.igank.database.cache;

import android.database.Cursor;
import android.os.Handler;
import android.support.design.widget.Snackbar;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.api.GankAPI;
import cn.edu.ustc.igank.database.table.GirlTable;
import cn.edu.ustc.igank.model.GirlBean;
import cn.edu.ustc.igank.support.NetWorkUtil;
import cn.edu.ustc.igank.support.CONSTANT;
import cn.edu.ustc.igank.support.Snack;
import cn.edu.ustc.igank.support.Utils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Lehman on 2016-06-25.
 */
public class GirlCache extends BaseCache<GirlBean> {

    private int currentPage = 1;

    public GirlCache(Handler handler) {
        super(handler);
    }

    @Override
    protected void putData() {

        db.execSQL(mHelper.DELETE_TABLE_DATA + GirlTable.NAME);
        // db.execSQL(table.CREATE_TABLE);
        for (int i = 0; i < mList.size(); i++) {
            GirlBean girlBean = mList.get(i);
            values.put(GirlTable.ID, girlBean.get_id()+"");
            values.put(GirlTable.CREATED_AT, girlBean.getCreatedAt());
            values.put(GirlTable.DESC, girlBean.getDesc());
            values.put(GirlTable.PUBLISHED_AT, girlBean.getPublishedAt());
            values.put(GirlTable.SOURCE, girlBean.getSource());
            values.put(GirlTable.TYPE, girlBean.getType());
            values.put(GirlTable.URL, girlBean.getUrl());
            values.put(GirlTable.USED, girlBean.isUsed()?1:0);
            values.put(GirlTable.WHO, girlBean.getWho());

            db.insert(GirlTable.NAME, null, values);
        }

    }

    @Override
    protected void putData(GirlBean girlBean) {

        values.put(GirlTable.ID, girlBean.get_id());
        values.put(GirlTable.CREATED_AT, girlBean.getCreatedAt());
        values.put(GirlTable.DESC, girlBean.getDesc());
        values.put(GirlTable.PUBLISHED_AT, girlBean.getPublishedAt());
        values.put(GirlTable.SOURCE, girlBean.getSource());
        values.put(GirlTable.TYPE, girlBean.getType());
        values.put(GirlTable.URL, girlBean.getUrl());
        values.put(GirlTable.USED, girlBean.isUsed());
        values.put(GirlTable.WHO, girlBean.getWho());

        db.insert(GirlTable.NAME, null, values);
    }

    @Override
    public void refresh() {
        AsyncHttpClient client = NetWorkUtil.getInstance();
        client.get(GankAPI.getGirlAPI(CONSTANT.QUANTITY_OF_DATA, 1), new JsonHttpResponseHandler() {
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
                            mList.add(gson.fromJson(array.get(i).toString(), GirlBean.class));
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
        client.get(GankAPI.getGirlAPI(CONSTANT.QUANTITY_OF_DATA, currentPage + 1), new JsonHttpResponseHandler() {
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
                            mList.add(gson.fromJson(array.get(i).toString(), GirlBean.class));
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

        String sql = "select * from " + GirlTable.NAME + " order by " + GirlTable.ID + " desc";

        Cursor cursor = query(sql);
        while (cursor.moveToNext()) {
            GirlBean girlBean = new GirlBean();
            girlBean.set_id(cursor.getString(GirlTable.ID_ID));
            girlBean.setCreatedAt(cursor.getString(GirlTable.ID_CREATED_AT));
            girlBean.setDesc(cursor.getString(GirlTable.ID_DESC));
            girlBean.setPublishedAt(cursor.getString(GirlTable.ID_PUBLISHED_AT));
            girlBean.setSource(cursor.getString(GirlTable.ID_SOURCE));
            girlBean.setType(cursor.getString(GirlTable.ID_TYPE));
            girlBean.setUrl(cursor.getString(GirlTable.ID_URL));
            girlBean.setUsed(cursor.getInt(GirlTable.ID_USED) == 1 ? true : false);
            girlBean.setWho(cursor.getString(GirlTable.ID_WHO));

            mList.add(girlBean);
        }

        // notify
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        cursor.close();
    }
}
