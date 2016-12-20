package com.example.httpsdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/12.
 */

public class MainActivity2  extends AppCompatActivity {

    @Bind(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                getHttp();
                getHttpsClient();
            }
        };
        new Thread(runnable).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tvContent.setText(String.valueOf(msg.obj));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void getHttpsClient() {
        String https = "https://testapi.kakasure.com" + "/index/recommend.json";
        int[] ints = {R.raw.kakasurecom};
        HttpClient httpclient = HttpClientUtils.getNewHttpClient(this, R.raw.kakasurecom);
        HttpGet httpget = new HttpGet(https);
        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = null;
        try {
            responseBody = (String) httpclient.execute(httpget, responseHandler);
            // 读取结果，发送到主线程
            handler.obtainMessage(0, responseBody).sendToTarget();
            System.out.println(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}