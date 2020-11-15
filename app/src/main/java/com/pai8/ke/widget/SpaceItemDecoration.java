package com.pai8.ke.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 设置RecyclerView分割线间距
 * Created by gh on 2020/11/14.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int top, left, right, bottom;
    private boolean isHeader;

    public SpaceItemDecoration(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;

    }

    public SpaceItemDecoration(boolean isHeader, int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.isHeader = isHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if (pos != 0)
            outRect.top = top;
        outRect.left = left;
        outRect.right = right;
        if (isHeader) {
            if (pos != 0) {
                outRect.bottom = bottom;
            }
        } else {
            outRect.bottom = bottom;
        }
    }
}
