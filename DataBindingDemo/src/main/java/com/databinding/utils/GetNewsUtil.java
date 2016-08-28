package com.databinding.utils;

import com.databinding.bean.NewsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:从网络获取数据
 */
public class GetNewsUtil {

    /**
     * 解析JSON数据
     *
     * @param url 网络URL
     * @return 对象集合
     */
    public static List<NewsBean> getJson(String url) {
        List<NewsBean> newsBeanList = new ArrayList<>();
        NewsBean newsBean;
        try {
            //获取请求返回字符串
            String jsonString = JsonToStringUtil.JsonToString(new URL(url).openStream());
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("newslist");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dataJson = jsonArray.getJSONObject(i);
                    newsBean = new NewsBean();
                    newsBean.setTitle(dataJson.getString("title"));
                    newsBean.setTime(dataJson.getString("ctime"));
                    newsBean.setDesc(dataJson.getString("description"));
                    newsBean.setUrl(dataJson.getString("url"));
                    newsBean.setPicUrl(dataJson.getString("picUrl"));
                    newsBeanList.add(newsBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsBeanList;
    }

}

