package com.study.stickyheader.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.study.stickyheader.R;
import com.study.stickyheader.Utils;
import com.study.stickylib.StikkyHeaderBuilder;
import com.study.stickylib.animator.AnimatorBuilder;
import com.study.stickylib.animator.HeaderStikkyAnimator;

public class ParallaxStikkyFragment extends Fragment {

    private ListView mListView;

    public ParallaxStikkyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parallax, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = (ListView) view.findViewById(R.id.listview);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StikkyHeaderBuilder.stickTo(mListView)
                .setHeader(R.id.header, (ViewGroup) getView())
                .minHeightHeaderDim(R.dimen.min_height_header)
                .animator(new ParallaxStikkyAnimator())
                .build();

        Utils.populateListView(mListView);
    }

    private class ParallaxStikkyAnimator extends HeaderStikkyAnimator {
        @Override
        public AnimatorBuilder getAnimatorBuilder() {
            View mHeader_image = getHeader().findViewById(R.id.header_image);
            return AnimatorBuilder.create().applyVerticalParallax(mHeader_image);
        }
    }

}
