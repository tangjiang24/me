package com.tj.myandroid.recyclertest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class  MyRecyclerView extends RecyclerView {
    public MyRecyclerView(@NonNull Context context) {
       this(context,null);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void smoothScrollToPosition(int position) {
        if (getLayoutManager() != null && getLayoutManager() instanceof LinearLayoutManager) {
            // exclude item decoration
//            LinearSmoothScroller linearSmoothScroller =
//                    new LinearSmoothScroller(getContext()) {
//                        @Override
//                        public PointF computeScrollVectorForPosition(int targetPosition) {
//                            if (getLayoutManager() == null) {
//                                return null;
//                            }
//                            return ((LinearLayoutManager) getLayoutManager()).computeScrollVectorForPosition(targetPosition);
//                        }
//
//                        @Override
//                        protected void onTargetFound(View targetView, RecyclerView.State state, SmoothScroller.Action action) {
//                            if (getLayoutManager() == null) {
//                                return;
//                            }
//                            int dx = calculateDxToMakeVisible(targetView, getHorizontalSnapPreference());
//                            int dy = calculateDyToMakeVisible(targetView, getVerticalSnapPreference());
//                            if (dx > 0) {
//                                dx = dx - getLayoutManager().getLeftDecorationWidth(targetView);
//                            } else {
//                                dx = dx + getLayoutManager().getRightDecorationWidth(targetView);
//                            }
//                            if (dy > 0) {
//                                dy = dy - getLayoutManager().getTopDecorationHeight(targetView);
//                            } else {
//                                dy = dy + getLayoutManager().getBottomDecorationHeight(targetView);
//                            }
//                            final int distance = (int) Math.sqrt(dx * dx + dy * dy);
//                            final int time = calculateTimeForDeceleration(distance);
//                            if (time > 0) {
//                                action.update(-dx, -dy, time, mDecelerateInterpolator);
//                            }
//                        }
//                    };
//            linearSmoothScroller.setTargetPosition(position);
//            getLayoutManager().startSmoothScroll(linearSmoothScroller);
            super.smoothScrollToPosition(position);
        } else {
            super.smoothScrollToPosition(position);
        }
    }
}
