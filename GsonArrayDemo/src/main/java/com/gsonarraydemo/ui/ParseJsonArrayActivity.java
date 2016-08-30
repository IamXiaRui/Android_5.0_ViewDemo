package com.gsonarraydemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gsonarraydemo.R;
import com.gsonarraydemo.adapter.ResultAdapter;
import com.gsonarraydemo.adapter.UserAdapter;
import com.gsonarraydemo.bean.ResultBean;
import com.gsonarraydemo.bean.UserBean;
import com.gsonarraydemo.utils.JsonToStringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiarui on 2016/8/30.
 * GSON解析数组的四种方式
 */
public class ParseJsonArrayActivity extends AppCompatActivity {
    private ListView mainLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_array);
        mainLView = (ListView) findViewById(R.id.lv_main);

        //解析没有消息头的纯数组
        parseNoHeaderJArray();

        //解析有消息头的纯数组
        //parseHaveHeaderJArray();

        //常规方式解析有消息头的复杂数据
        //parseComplexJArrayByCommon();

        //直接方式解析有消息头的复杂数据
        //parseComplexJArrayByDirect();

    }

    /**
     * 解析没有消息头的纯数组
     */
    private void parseNoHeaderJArray() {
        String strByJson = JsonToStringUtil.getStringByJson(this, R.raw.juser_1);
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        ArrayList<UserBean> userBeanList = new ArrayList<>();
        for (JsonElement obj : jsonArray) {
            UserBean userBean = gson.fromJson(obj, UserBean.class);
            userBeanList.add(userBean);
        }
        mainLView.setAdapter(new UserAdapter(this, userBeanList));
    }

    /**
     * 解析有消息头的纯数组
     */
    private void parseHaveHeaderJArray() {
        String strByJson = JsonToStringUtil.getStringByJson(this, R.raw.juser_2);
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonParser().parse(strByJson).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("muser");
        ArrayList<UserBean> userBeanList = new ArrayList<>();
        for (JsonElement obj : jsonArray) {
            UserBean userBean = gson.fromJson(obj, new TypeToken<UserBean>() {
            }.getType());
            userBeanList.add(userBean);
        }
        mainLView.setAdapter(new UserAdapter(this, userBeanList));
    }

    /**
     * 有消息头 复杂数据 常规方式
     */
    private void parseComplexJArrayByCommon() {
        String strByJson = JsonToStringUtil.getStringByJson(this, R.raw.juser_3);
        List<ResultBean.UserBean> userBeanList = new ArrayList<>();
        JsonObject jsonObject = new JsonParser().parse(strByJson).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("muser");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement el = jsonArray.get(i);
            ResultBean.UserBean userBean = new Gson().fromJson(el, new TypeToken<ResultBean.UserBean>() {
            }.getType());
            userBeanList.add(userBean);
        }
        mainLView.setAdapter(new ResultAdapter(this, userBeanList));
    }

    /**
     * 有消息头 复杂数据 直接方式
     */
    private void parseComplexJArrayByDirect() {
        String strByJson = JsonToStringUtil.getStringByJson(this, R.raw.juser_3);
        List<UserBean> userBeanList = new ArrayList<>();
        JsonObject jsonObject = new JsonParser().parse(strByJson).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("muser");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement el = jsonArray.get(i);
            UserBean userBean = new Gson().fromJson(el, new TypeToken<UserBean>() {
            }.getType());
            if (Integer.parseInt(userBean.getAge()) > 30) {
                userBeanList.add(userBean);
            }
        }
        mainLView.setAdapter(new UserAdapter(this, userBeanList));
    }

}
