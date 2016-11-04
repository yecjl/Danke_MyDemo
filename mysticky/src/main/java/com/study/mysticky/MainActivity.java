package com.study.mysticky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.id_recycler_view)
    WrapRecyclerView idRecyclerView;
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.btn2)
    Button btn2;
    private CircleView mCircleView;

    private List<String> mList;

    private TestAdapter mAdapter;

    private WrapRecyclerView mListView;

    private Test1Adapter mAdapter1;

    private ListView mListView1;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mList.add("my love " + i);
        }
//        mAdapter1 = new Test1Adapter(mList) ;
//        mListView1 = (ListView) findViewById(R.id.id_list_view);
//        mListView1.setAdapter(mAdapter1);

        mAdapter = new TestAdapter(mList);
        mListView = (WrapRecyclerView) findViewById(R.id.id_recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mListView.setLayoutManager(manager);
        textView = new TextView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setText("我是头部View001");
        mListView.addHeaderView(textView);
        mListView.setAdapter(mAdapter);

        mListView.addOnScrollListener(new OnScrollListenerStikky());
    }

    @OnClick(R.id.btn)
    public void blew(View view) {
        mScrolledY += 10;
        StikkyCompat.setTranslationY(textView, mScrolledY);
    }

    private int mScrolledY = 0;

    private class OnScrollListenerStikky extends RecyclerView.OnScrollListener {


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (mScrolledY == Integer.MIN_VALUE) {
                mScrolledY = calculateScrollRecyclerView();
            } else {
                mScrolledY += dy;
            }

            StikkyCompat.setTranslationY(textView, Math.max(-mScrolledY, -100));
        }

    }


    private int calculateScrollRecyclerView() {

        final View firstChild = mListView.getChildAt(0);
        int positionFirstItem = mListView.getChildAdapterPosition(firstChild);
        int heightDecorator = 0;
        if (positionFirstItem != 0) {
            heightDecorator = 100;
        }

        return mListView.computeVerticalScrollOffset() + heightDecorator;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    public void startAnimator(View view){
//        ValueAnimator animator = ValueAnimator.ofFloat(0,1,0);
//        animator.setDuration(5000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float aFloat = (Float) animation.getAnimatedValue() ;
////                if (aFloat < 0.01)
////                    aFloat = 0 ;
//                mCircleView.setPercent(aFloat);
//            }
//        });
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setRepeatMode(ValueAnimator.INFINITE);
//        animator.start();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
