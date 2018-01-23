package com.xiey94.damp.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

/**
 * @author : xiey
 * @project name : As30.
 * @package name  : com.xiey94.damp.view.
 * @date : 2018/1/19.
 * @signature : do my best.
 * @from : http://blog.csdn.net/darling_shadow/article/details/41514233
 * @explain :
 */

public class dampHorizontalScrollView extends HorizontalScrollView {

    //整个子View
    private View inner;
    //x的位置
    private float x;
    //记录位置信息的
    private Rect normal = new Rect();
    //初次点击的时候，和上次遗留的位置信息产生的距离不是我们想要的，会产生位置差，所以要过滤掉第一次移动事件
    private boolean isCount = false;
    //判断是否需要移动
    private boolean isMoveing = false;

    public dampHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 获取第一个子元素
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
        super.onFinishInflate();
    }

    /**
     * 点击事件处理
     * 存在子View才处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner != null) {
            commonTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滑动事件
     * 让滑动的速度变为原来的1/2
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 2);
    }

    /**
     * 事件处理
     */
    public void commonTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                final float preX = x;
                float nowX = ev.getX();
                //滑动位移
                int deltaX = (int) (nowX - preX);
                //第一次滑动同样要置0，不然会有晃动效果
                if (!isCount) {
                    deltaX = 0;
                }

                isNeedMove();

                if (isMoveing) {
                    //记录位置信息
                    if (normal.isEmpty()) {
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                    }

                    //绘制位置信息,在整个儿的高度上进行移动
                    inner.layout(inner.getLeft() + deltaX / 3, inner.getTop(),
                            inner.getRight() + deltaX / 3, inner.getBottom());
                }

                isCount = true;
                x = nowX;

                break;
            case MotionEvent.ACTION_UP:
                isMoveing = false;
                //手指松开
                if (isNeedAnimation()) {
                    animation();
                }
                break;
            default:
                break;
        }
    }

    /***
     * 回缩动画
     */
    public void animation() {
        TranslateAnimation ta = new TranslateAnimation(inner.getLeft(), normal.left, 0, 0);
        ta.setDuration(100);
        inner.startAnimation(ta);
        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
        isCount = false;
        // 手指松开要归0.
        x = 0;

    }

    /**
     * 是否开启动画
     */
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    /**
     * 是否需要移动
     */
    public void isNeedMove() {
        int offset = inner.getMeasuredWidth() - getWidth();
        int scrollX = getScrollX();
        if (scrollX == 0 || scrollX == offset) {
            isMoveing = true;
        } else {
            isMoveing = false;
        }
    }

}
