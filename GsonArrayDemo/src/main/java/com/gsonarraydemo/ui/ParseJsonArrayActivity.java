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
        //parseNoHeaderJArray();

        //解析有消息头的纯数组
        //parseHaveHeaderJArray();

        //常规方式解析有消息头的复杂数据
        //parseComplexJArrayByCommon();

        //直接方式解析有消息头的复杂数据
        parseComplexJArrayByDirect();

    }

    /**
     * 解析没有数据头的纯数组
     */
    private void parseNoHeaderJArray() {
        //拿到本地JSON 并转成String
        String strByJson = JsonToStringUtil.getStringByJson(this, R.raw.juser_1);

        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();

        Gson gson = new Gson();
        ArrayList<UserBean> userBeanList = new ArrayList<>();

        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            UserBean userBean = gson.fromJson(user, UserBean.class);
            userBeanList.add(userBean);
        }
        mainLView.setAdapter(new UserAdapter(this, userBeanList));
    }

    /**
     * 解析有数据头的纯数组
     */
    private void parseHaveHeaderJArray() {
        //拿到本地JSON 并转成String
        String strByJson = JsonToStringUtil.getStringByJson(this, R.raw.juser_2);

        //先转JsonObject
        JsonObject jsonObject = new JsonParser().parse(strByJson).getAsJsonObject();
        //再转JsonArray 加上数据头
        JsonArray jsonArray = jsonObject.getAsJsonArray("muser");

        Gson gson = new Gson();
        ArrayList<UserBean> userBeanList = new ArrayList<>();

        //循环遍历
        for (JsonElement user : jsonArray) {
            //通过反射 得到UserBean.class
            UserBean userBean = gson.fromJson(user, new TypeToken<UserBean>() {
            }.getType());
            userBeanList.add(userBean);
        }
        mainLView.setAdapter(new UserAdapter(this, userBeanList));
    }

    /**
     * 有数据头 复杂数据 常规方式
     */
    private void parseComplexJArrayByCommon() {
        //拿到Json字符串
        String strByJson = JsonToStringUtil.getStringByJson(this, R.raw.juser_3);
        //GSON直接解析成对象
        ResultBean resultBean = new Gson().fromJson(strByJson, ResultBean.class);
        //对象中拿到集合
        List<ResultBean.UserBean> userBeanList = resultBean.getMuser();
        //展示到UI中
        mainLView.setAdapter(new ResultAdapter(this, userBeanList));

    }

    /**
     * 有数据头 复杂数据 截取方式
     */
    private void parseComplexJArrayByDirect() {
        //拿到JSON字符串
        String strByJson = JsonToStringUtil.getStringByJson(this, R.raw.juser_3);
        List<UserBean> userBeanList = new ArrayList<>();

        //拿到数组
        JsonObject jsonObject = new JsonParser().parse(strByJson).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("muser");

        //循环遍历数组
        for (JsonElement user : jsonArray) {
            UserBean userBean = new Gson().fromJson(user, new TypeToken<UserBean>() {
            }.getType());
            //根据条件过滤
            if (Integer.parseInt(userBean.getAge()) > 30) {
                userBeanList.add(userBean);
            }
        }
        mainLView.setAdapter(new UserAdapter(this, userBeanList));
    }

}
