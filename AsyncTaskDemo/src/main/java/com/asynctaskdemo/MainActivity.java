package com.asynctaskdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 异步任务AsyncTask的使用
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button netpicButton, progressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        netpicButton = (Button) findViewById(R.id.bt_netpic);
        progressButton = (Button) findViewById(R.id.bt_progress);

        netpicButton.setOnClickListener(this);
        progressButton.setOnClickListener(this);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_netpic:
                gotoOtherActivity(NetPicActivity.class);
                break;
            case R.id.bt_progress:
                gotoOtherActivity(ProgressActivity.class);
                break;
        }
    }

    /**
     * 跳转到其他Activity
     */
    private void gotoOtherActivity(Class clazz) {
        Intent intent = new Intent(MainActivity.this, clazz);
        startActivity(intent);
    }


    /**
     * 自定义异步任务类
     */
    class CustomTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.w("Task", " ----> onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            publishProgress();
            Log.w("Task", " ----> doInBackground");
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.w("Task", " ----> onProgressUpdate");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.w("Task", " ----> onPostExecute");
        }

    }
}
