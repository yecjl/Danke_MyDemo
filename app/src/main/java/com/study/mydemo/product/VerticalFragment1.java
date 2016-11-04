package com.study.mydemo.product;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.study.mydemo.ProductScrollView;
import com.study.mydemo.R;
import com.study.mydemo.UIUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VerticalFragment1 extends Fragment {

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.ll_head)
    LinearLayout llHead;
    @Bind(R.id.old_textview)
    TextView oldTextview;
    @Bind(R.id.custScrollView)
    CustScrollView custScrollView;
    ImageView ivBack;
    ImageView ivBack2;
    ImageView ivCart;
    ImageView ivCart2;
    View line;
    LinearLayout navBar;
    private Drawable bgNavBarDrawable;
    private Drawable bgIvBackDrawable;
    private Drawable bgIvBack2Drawable;
    private Drawable bgIvCartDrawable;
    private Drawable bgIvCart2Drawable;
    private Drawable bgLineDrawable;
    private int alphaMax = 0xFF; // 最大透明度
    private FragmentActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vertical_fragment1, null);
        TextView oldTextView = (TextView) rootView
                .findViewById(R.id.old_textview);
        oldTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        ButterKnife.bind(this, rootView);

        // 计算图片区域显示高度，和实际高度
        llHead.getLayoutParams().height = UIUtil.getScreenWidth(getActivity());
        Drawable drawable = imageView.getDrawable();
        final int intrinsicHeight = drawable.getIntrinsicHeight();

        // 等View的树状结构全部渲染完毕的时候，再设置ImageView，此时就可以获取ImageView的高度
        custScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                custScrollView.setParallaxView(llHead, intrinsicHeight, 44);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    custScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        // 设置顶部
        activity = getActivity();
        navBar = (LinearLayout) activity.findViewById(R.id.nav_bar);
        ivBack = (ImageView) activity.findViewById(R.id.iv_back);
        ivBack2 = (ImageView) activity.findViewById(R.id.iv_back2);
        ivCart = (ImageView) activity.findViewById(R.id.iv_cart);
        ivCart2 = (ImageView) activity.findViewById(R.id.iv_cart2);
        line = activity.findViewById(R.id.line);
        custScrollView.setOnTouchEventMoveListener(new ProductScrollView.OnTouchEventMoveListener() {
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
                bgLineDrawable.setAlpha(alpha);
            }
        });

        bgNavBarDrawable = navBar.getBackground();
        bgIvBackDrawable = ivBack.getBackground();
        bgIvBack2Drawable = ivBack2.getBackground();
        bgIvCartDrawable = ivCart.getBackground();
        bgIvCart2Drawable = ivCart2.getBackground();
        bgLineDrawable = line.getBackground();
        bgNavBarDrawable.setAlpha(0);
        bgIvBackDrawable.setAlpha(alphaMax);
        bgIvBack2Drawable.setAlpha(0);
        bgIvCartDrawable.setAlpha(alphaMax);
        bgIvCart2Drawable.setAlpha(0);
        bgLineDrawable.setAlpha(0);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
