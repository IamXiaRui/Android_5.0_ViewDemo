package com.asynctaskdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

/**
 * 用异步任务模拟进度条的更新
 */
public class ProgressActivity extends AppCompatActivity {
    private ProgressBar mainProgressBar;
    private PbAsyncTask pbAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mainProgressBar = (ProgressBar) findViewById(R.id.pb_main);

        //开启异步任务
        pbAsyncTask = new PbAsyncTask();
        pbAsyncTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pbAsyncTask != null && pbAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            //cancel方法只是将对应的AsyncTask标记为cancelt状态,并不是真正的取消线程的执行.
            pbAsyncTask.cancel(true);
        }
    }

    /**
     * 自定义异步任务类
     */
    class PbAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //使用for循环来模拟进度条的进度.
            for (int i = 0; i < 100; i++) {
                //如果task是cancel状态,则终止for循环,以进行下个task的执行.
                if (isCancelled()) {
                    break;
                }
                //调用publishProgress方法将自动触发onProgressUpdate方法来进行进度条的更新.
                publishProgress(i);
                try {
                    //通过线程休眠模拟耗时操作
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //通过publishProgress方法传过来的值进行进度条的更新.
            mainProgressBar.setProgress(values[0]);
        }
    }

}
