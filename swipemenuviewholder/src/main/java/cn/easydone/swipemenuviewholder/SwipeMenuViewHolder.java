package cn.easydone.swipemenuviewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-07-17
 * Time: 18:46
 */
public abstract class SwipeMenuViewHolder {
    /* 从左侧滑出菜单 */
    public static final int EDGE_LEFT = 1;
    /* 从右侧滑出菜单 */
    public static final int EDGE_RIGHT = 2;
    /* 默认从右侧滑出菜单 */
    private int mTrackingEdges = EDGE_RIGHT;
    /* 滑出来的布局 */
    private View swipMenuView;
    /* 可以拖动的布局 */
    private View captureView;
    /* 整个条目的布局 */
    protected DragLayout itemView;

    private Context context;

    private DragViewHolder dragViewHolder;

    public SwipeMenuViewHolder(Context context, View swipMenuView, View captureView) {
        this.swipMenuView = swipMenuView;
        this.captureView = captureView;
        this.context = context;
        initItemView();
    }

    public SwipeMenuViewHolder(Context context, View swipMenuView, View captureView, int mTrackingEdges) {
        this.mTrackingEdges = mTrackingEdges;
        this.swipMenuView = swipMenuView;
        this.captureView = captureView;
        this.context = context;
        initItemView();
    }

    private void initItemView() {
        itemView = new DragLayout(context);
        itemView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        itemView.setmTrackingEdges(mTrackingEdges);
        itemView.initView(captureView, swipMenuView);
        dragViewHolder = new DragViewHolder(this, itemView);
    }

    public boolean isOpen() {
        return itemView.state == DragLayout.STATE_OPNE;
    }

    public DragViewHolder getDragViewHolder() {
        return dragViewHolder;
    }

    /**
     * 初始化控件
     *
     * @param itemView 条目的布局
     */
    public abstract void initView(View itemView);

    /**
     * 获取this
     *
     * @param viewHolder 获取当前的 ViewHolder
     * @return RecyclerViewDragHolder
     */
    public static SwipeMenuViewHolder getHolder(RecyclerView.ViewHolder viewHolder) {
        return ((DragViewHolder) viewHolder).holder;
    }

    class DragViewHolder extends RecyclerView.ViewHolder {

        public SwipeMenuViewHolder holder;

        public DragViewHolder(SwipeMenuViewHolder holder, View itemView) {
            super(itemView);
            this.holder = holder;
            initView(itemView);

        }
    }

    private class DragLayout extends FrameLayout {

        private ViewDragHelper mDragHelper;
        private View captureView;
        private View swipeMenuView;
        /* 左侧起点 */
        private int startX;
        /* 可拉出来布局的宽度 */
        private int swipeMenuWidth;
        private int mTrackingEdges = EDGE_RIGHT;
        /* 滑出来的百分比，大于这个值松开自动打开，否则就关闭 */
        private float scrollPercent = 0.5f;
        /* 当前打开状态 */
        protected int state = STATE_CLOSE;
        protected static final int STATE_OPNE = 1;
        protected static final int STATE_CLOSE = 2;

        public DragLayout(Context context) {
            super(context);
            init();
        }

        public DragLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        public void initView(View captureView, View swipeMenuView) {
            this.captureView = captureView;
            this.swipeMenuView = swipeMenuView;
            startX = 0;
            addView(createBgView(swipeMenuView));
            addView(captureView);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (widthMeasureSpec != 0) {
                swipeMenuWidth = swipeMenuView.getWidth();
            }
        }

        /* 可以滑出来的布局 */
        private View createBgView(View view) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setGravity(mTrackingEdges == EDGE_RIGHT ? Gravity.END : Gravity.START);
            linearLayout.addView(view);
            return linearLayout;
        }

        private void init() {
            mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragCallBack());
            mDragHelper.setEdgeTrackingEnabled(mTrackingEdges == EDGE_RIGHT ? ViewDragHelper.EDGE_RIGHT : ViewDragHelper.EDGE_LEFT);
        }

        private class ViewDragCallBack extends ViewDragHelper.Callback {

            public ViewDragCallBack() {
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == captureView;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (left != startX) {
                    if (swipeMenuView.getVisibility() == View.GONE)
                        swipeMenuView.setVisibility(View.VISIBLE);
                } else {
                    if (swipeMenuView.getVisibility() == View.VISIBLE)
                        swipeMenuView.setVisibility(View.GONE);

                }
                invalidate();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (releasedChild != captureView)
                    return;
                int endX;
                Log.d("captureView.Left", captureView.getLeft() + "");
                Log.d("scrollSwipeMenuWidth", (int) ((float) swipeMenuWidth * scrollPercent) + "");
                Log.d("state", state + "");
                Log.d("mTrackingEdges", mTrackingEdges + "");
                Log.d("startX", +startX + "");
                if (mTrackingEdges == EDGE_LEFT) {
                    if (captureView.getLeft() < (int) ((float) swipeMenuWidth * scrollPercent) || state == STATE_OPNE) {
                        endX = startX;
                        state = STATE_CLOSE;
                    } else {
                        endX = swipeMenuWidth;
                        state = STATE_OPNE;
                    }
                } else {//bgView从右边缘滑出
                    if (captureView.getLeft() > -(int) ((float) swipeMenuWidth * scrollPercent) || state == STATE_OPNE) {//向右滑动关闭bgView
                        endX = startX;
                        state = STATE_CLOSE;
                    } else {//向左滑动拉出bgView
                        endX = -1 * swipeMenuWidth;
                        state = STATE_OPNE;
                    }
                }

                Log.d("endX", + endX + "");
                Log.d("swipeMenuWidth", +swipeMenuWidth + "");
                if (mDragHelper.smoothSlideViewTo(captureView, endX, 0)) {
                    ViewCompat.postInvalidateOnAnimation(DragLayout.this);
                }
                invalidate();
            }

            /**
             * @param child 被拖动到view
             * @param left  captureView被拖动后距x轴的距离
             * @param dx    横向移动的距离
             * @return captureView被拖动后距x轴的距离
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (mTrackingEdges == EDGE_LEFT) {
                    if (left > swipeMenuWidth && dx > 0) return swipeMenuWidth;
                    if (left < 0 && dx < 0) return 0;
                } else {
                    if (left > 0 && dx > 0) return 0;
                    if (left < -swipeMenuWidth && dx < 0) return -swipeMenuWidth;
                }
                return left;
            }
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (state == STATE_CLOSE)
                swipeMenuView.setVisibility(View.GONE);
            return mDragHelper.shouldInterceptTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            mDragHelper.processTouchEvent(event);
            return true;
        }

        @Override
        public void computeScroll() {
            super.computeScroll();
            if (mDragHelper.continueSettling(true)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        public void setmTrackingEdges(int mTrackingEdges) {
            this.mTrackingEdges = mTrackingEdges;
            mDragHelper.setEdgeTrackingEnabled(mTrackingEdges == EDGE_RIGHT ? ViewDragHelper.EDGE_RIGHT : ViewDragHelper.EDGE_LEFT);
        }
    }
}
