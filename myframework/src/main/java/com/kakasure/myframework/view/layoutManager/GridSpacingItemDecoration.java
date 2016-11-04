package com.kakasure.myframework.view.layoutManager;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 自动调整GridLayoutManager的边距
 * danke
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount; // 个数
    private int spacing; // 间距
    private boolean includeEdge; // 是否最左右两边有边距
    private int spanSizeCount; // 撑满全屏的数量
    private boolean isTopSpacing; // top是否也需要边距

    public GridSpacingItemDecoration(int spanCount, int spacing, int spanSizeCount, boolean isTopSpacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.spanSizeCount = spanSizeCount;
        this.isTopSpacing = isTopSpacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = (position - spanSizeCount) % spanCount; // item column: 仅仅使用头部有撑满的，如果中间又撑满的item，可能就会有问题

        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount();
        int spanSize = layoutManager.getSpanSizeLookup().getSpanSize(position);
        if (spanCount == spanSize) {
            return;
        }
        if (includeEdge) {
            outRect.left = spacing - column * spacing / this.spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / this.spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if ((position == spanSizeCount || position == spanSizeCount + 1) && isTopSpacing) { // top edge // 如果顶部需要边距，第一行设置top值，否则不设置top值
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / this.spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / this.spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= this.spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}