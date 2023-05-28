/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jakewharton.u2020.ui.misc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[] { android.R.attr.listDivider };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private final boolean rtl;

    private final Drawable divider;

    private int orientation;

    private float paddingStart;

    public DividerItemDecoration(Context context, int orientation, float paddingStart, boolean rtl) {
        this.rtl = rtl;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        divider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
        setPaddingStart(paddingStart);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.orientation = orientation;
    }

    public void setPaddingStart(float paddingStart) {
        if (paddingStart < 0) {
            throw new IllegalArgumentException("paddingStart < 0");
        }
        this.paddingStart = paddingStart;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (orientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = (int) (parent.getPaddingLeft() + (rtl ? 0 : paddingStart));
        final int right = (int) (parent.getWidth() - parent.getPaddingRight() + (rtl ? paddingStart : 0));
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            final int right = left + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (orientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, divider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
        }
    }
}
