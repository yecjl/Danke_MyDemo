package com.study.mydemo;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.study.mydemo.product.VerticalFragment1;
import com.study.mydemo.product.VerticalFragment3;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.ll_head)
    LinearLayout llHead;
    @Bind(R.id.dl_content)
    DragLayout dlContent;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_cart)
    ImageView ivCart;
    @Bind(R.id.nav_bar)
    LinearLayout navBar;
    @Bind(R.id.scrollView)
    ProductScrollView scrollView;
    @Bind(R.id.iv_back2)
    ImageView ivBack2;
    @Bind(R.id.iv_cart2)
    ImageView ivCart2;
    private int alphaMax = 0xFF;
    private Drawable bgNavBarDrawable;
    private Drawable bgIvBackDrawable;
    private Drawable bgIvBack2Drawable;
    private Drawable bgIvCartDrawable;
    private Drawable bgIvCart2Drawable;
    private VerticalFragment1 fragment1;
    private VerticalFragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        // 计算图片区域显示高度，和实际高度
        llHead.getLayoutParams().height = UIUtil.getScreenWidth(this);
        Drawable drawable = imageView.getDrawable();
        final int intrinsicHeight = drawable.getIntrinsicHeight();

        // 等View的树状结构全部渲染完毕的时候，再设置ImageView，此时就可以获取ImageView的高度
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollView.setParallaxView(llHead, intrinsicHeight, navBar.getHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        scrollView.setOnTouchEventMoveListener(new ProductScrollView.OnTouchEventMoveListener() {
            private int percent3 = (int) (alphaMax * 1.0 * 3 / 5); // 3/5
            private int percent4 = (int) (alphaMax * 1.0 * 4 / 5); // 4/5
            private int alphaReverse2;

            @Override
            public void onSlideUp(int mOriginalHeaderHeight, int mHeaderHeight) {
            }

            @Override
            public void onSlideDown(int mOriginalHeaderHeight, int mHeaderHeight) {
            }

            @Override
            public void onSlide(int alpha) {
                // 0 -- 3/5的显示范围
                int alphaReverse = (int) (alphaMax - (alpha * 1.0 * 5 / 3));
                if (alphaReverse < 0) {
                    alphaReverse = 0;
                }

                // 3/5 -- 4/5的范围 -- 显示范围1/5
                if (alpha <= percent3) {
                    alphaReverse2 = 0;
                } else if (alpha > percent3 && alpha <= percent4) {
                    alphaReverse2 = 5 * (alpha - percent3);
                } else if (alpha > percent4) {
                    alphaReverse2 = alphaMax;
                }
                Log.d("alpha: ", alpha + ", alphaReverse: " + alphaReverse + ", alphaReverse2 =" + alphaReverse2);
                bgIvBackDrawable.setAlpha(alphaReverse);
                bgIvCartDrawable.setAlpha(alphaReverse);
                bgIvBack2Drawable.setAlpha(alphaReverse2);
                bgIvCart2Drawable.setAlpha(alphaReverse2);
                bgNavBarDrawable.setAlpha(alpha);
            }
        });

        bgNavBarDrawable = navBar.getBackground();
        bgIvBackDrawable = ivBack.getBackground();
        bgIvBack2Drawable = ivBack2.getBackground();
        bgIvCartDrawable = ivCart.getBackground();
        bgIvCart2Drawable = ivCart2.getBackground();
        bgNavBarDrawable.setAlpha(0);
        bgIvBackDrawable.setAlpha(alphaMax);
        bgIvBack2Drawable.setAlpha(0);
        bgIvCartDrawable.setAlpha(alphaMax);
        bgIvCart2Drawable.setAlpha(0);

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
        dlContent.setNextPageListener(nextIntf);
    }
}
