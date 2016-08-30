package com.gsonarraydemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.stream.JsonReader;
import com.gsonarraydemo.R;
import com.gsonarraydemo.utils.JsonToStringUtil;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by xiarui on 2016/8/30.
 * GSON 中JsonReader的使用方式
 */
public class ParseByReaderActivity extends AppCompatActivity {

    /*===== 控件相关 =====*/
    private TextView nameText;
    private TextView ageText;
    private TextView phoneText;
    private TextView emailText;
    private TextView addressText;
    private TextView workText;
    private TextView payText;
    private TextView mottoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_reader);

        //初始化View
        initView();

        //JsonReader方式解析有消息头的复杂数据
        try {
            parseComplexJArrayByReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化View
     */
    private void initView() {
        nameText = (TextView) findViewById(R.id.tv_reader_name);
        ageText = (TextView) findViewById(R.id.tv_reader_age);
        phoneText = (TextView) findViewById(R.id.tv_reader_phone);
        emailText = (TextView) findViewById(R.id.tv_reader_email);
        addressText = (TextView) findViewById(R.id.tv_reader_address);
        workText = (TextView) findViewById(R.id.tv_reader_work);
        payText = (TextView) findViewById(R.id.tv_reader_pay);
        mottoText = (TextView) findViewById(R.id.tv_reader_motto);
    }


    /**
     * 通过JsonReader的方式去解析
     */
    private void parseComplexJArrayByReader() throws IOException {
        String strByJson = JsonToStringUtil.getStringByJson(this, R.raw.juser_4);
        JsonReader reader = new JsonReader(new StringReader(strByJson));
        try {
            reader.beginObject();
            String tagName = reader.nextName();
            if (tagName.equals("group")) {
                //读group这个节点
                readGroup(reader);
            }
            reader.endObject();
        } finally {
            reader.close();
        }
    }

    /**
     * 读group这个节点
     *
     * @param reader JsonReader
     */
    private void readGroup(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String tagName = reader.nextName();
            if (tagName.equals("user")) {
                readUser(reader);
            } else if (tagName.equals("info")) {
                readInfo(reader);
            }
        }
        reader.endObject();
    }

    /**
     * 读用户基本消息 user节点
     *
     * @param reader JsonReader
     */
    private void readUser(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String tag = reader.nextName();
            if (tag.equals("name")) {
                String name = reader.nextString();
                nameText.setText(name);
            } else if (tag.equals("age")) {
                String age = reader.nextString();
                ageText.setText(age);
            } else if (tag.equals("phone")) {
                String phone = reader.nextString();
                phoneText.setText(phone);
            } else if (tag.equals("email")) {
                String email = reader.nextString();
                emailText.setText(email);
            } else {
                reader.skipValue();//忽略
            }
        }
        reader.endObject();
    }

    /**
     * 读用户其他消息 info节点
     *
     * @param reader JsonReader
     */
    private void readInfo(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String tag = reader.nextName();
            if (tag.equals("address")) {
                String address = reader.nextString();
                addressText.setText(address);
            } else if (tag.equals("work")) {
                String work = reader.nextString();
                workText.setText(work);
            } else if (tag.equals("pay")) {
                String pay = reader.nextString();
                payText.setText(pay);
            } else if (tag.equals("motto")) {
                String motto = reader.nextString();
                mottoText.setText(motto);
            } else {
                reader.skipValue();//忽略
            }
        }
        reader.endObject();
    }
}
