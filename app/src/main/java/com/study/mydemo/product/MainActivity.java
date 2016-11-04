package com.study.mydemo.product;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.study.mydemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.draglayout)
    DragLayout draglayout;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_back2)
    ImageView ivBack2;
    @Bind(R.id.iv_cart)
    ImageView ivCart;
    @Bind(R.id.iv_cart2)
    ImageView ivCart2;
    @Bind(R.id.nav_bar)
    LinearLayout navBar;
    private VerticalFragment1 fragment1;
    private VerticalFragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        fragment1 = new VerticalFragment1();
        fragment3 = new VerticalFragment3();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.first, fragment1).add(R.id.second, fragment3)
                .commit();

        DragLayout.ShowNextPageNotifier nextIntf = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                fragment3.initView();
            }
        };
        // 等View的树状结构全部渲染完毕的时候，再设置ImageView，此时就可以获取ImageView的高度
        draglayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                draglayout.setTitleHeight(navBar.getHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    draglayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        draglayout.setNextPageListener(nextIntf);
    }

}
