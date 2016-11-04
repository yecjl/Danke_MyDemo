package com.study.stickyheader;

import android.app.Activity;
import android.os.Bundle;

import com.study.stickyheader.video.VideoView;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 功能：
 * Created by 佳露 on 2016/9/30.
 */

public class MyActivity extends Activity {
    //    @Bind(R.id.recyclerview)
    //    RecyclerView recyclerview;
    @Bind(R.id.videoView)
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_video);
        ButterKnife.bind(this);

        videoView.setVideoPath("http://test.qimanet.com/uploadify/uploads/20160722/0535196060.mp4");
        //开始播放视频
        videoView.start();
    }
}
