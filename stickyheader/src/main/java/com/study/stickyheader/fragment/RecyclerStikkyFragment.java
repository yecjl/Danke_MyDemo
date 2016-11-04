package com.study.stickyheader.fragment;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.stickyheader.R;
import com.study.stickyheader.UIUtil;
import com.study.stickyheader.Utils;
import com.study.stickyheader.video.VideoView;
import com.study.stickylib.StikkyHeaderBuilder;

public class RecyclerStikkyFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public RecyclerStikkyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        final VideoView videoView = (VideoView) view.findViewById(R.id.videoView);
        videoView.setVideoPath("http://test.qimanet.com/uploadify/uploads/20160722/0535196060.mp4");
        //开始播放视频
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StikkyHeaderBuilder.stickTo(mRecyclerView)
                .setHeader(R.id.fl_video, (ViewGroup) getView())
                .minHeightHeader(UIUtil.Dp2Px(getActivity(), 300))
                .build();

        Utils.populateRecyclerView(mRecyclerView);
    }
}
