package com.xiey94.damp.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.xiey94.damp.util.showLog;

/**
 * @author : xiey
 * @project name : As30.
 * @package name  : com.xiey94.damp.view.
 * @date : 2018/1/19.
 * @signature : do my best.
 * @from : https://www.jianshu.com/p/59c366f27185
 * @explain : 整体上下有阻尼效果
 */

public class BounceScrollView extends ScrollView {

    private boolean isCalled;
    private Callback callBack;
    //取ScrollView的第一个子View，也就是整个
    private View mView;
    //只是用于记录位置
    private Rect mRect = new Rect();
    //记录y坐标
    private int y;
    private boolean isFirst = true;

    public BounceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 获取到要实现阻尼效果的View
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mView = getChildAt(0);
        }
        super.onFinishInflate();
    }

    /**
     * 事件拦截处理
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mView != null) {
            commonOnTouch(ev);
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 具体处理
     */
    private void commonOnTouch(MotionEvent ev) {
        int action = ev.getAction();
        //当前位置
        int cy = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                //移动的距离
                int dy = cy - y;
                //第一次移动的时候，因为上次残留的位置信息和当前的并不一定是连贯的，
                //可能会有一个很大的跳跃过程。所以从第二次开始记录位移比较准确一点
                if (isFirst) {
                    dy = 0;
                    isFirst = false;
                }
                y = cy;
                //判断是否到了顶端或者底部需要做阻尼运动的时候
                if (isNeedMove()) {
                    //因为运动，所以需要不停的记录信息并绘制
                    if (mRect.isEmpty()) {
                        mRect.set(mView.getLeft(), mView.getTop(), mView.getRight(), mView.getBottom());
                    }
                    //保持阻尼运动的比例差，看起来效果要好一点，有橡皮经效果
                    mView.layout(mView.getLeft(), mView.getTop() + dy / 5,
                            mView.getRight(), mView.getBottom() + dy / 5);
                    if (shouldCallBack(dy)) {
                        //如果设置了响应条件则响应事件，里面的逻辑可以自己处理
                        if (callBack != null) {
                            if (!isCalled) {
                                isCalled = true;
                                // 这个处理感觉有点问题，滑到这个位置还没松手就自己弹回去了，体验差
//                                resetPosition();
                                callBack.callback();
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mRect.isEmpty()) {
                    resetPosition();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 往下滑到一定距离则有了响应事件的条件，其实到了这里就没必要了，可以直接拿下了
     *
     * @param dy
     * @return
     */
    private boolean shouldCallBack(int dy) {
        if (dy > 0 && mView.getTop() > getHeight() / 10) {
            return true;
        }
        return false;
    }

    private void resetPosition() {
        //这里用的是mView.getTop()和mRect.top计算的是顶部的距离缓冲差，因为是一个整体，所以顶部位移是多少则底部位移就是多少
        Animation animation = new TranslateAnimation(0, 0, mView.getTop(), mRect.top);
        animation.setDuration(100);
        animation.setFillAfter(true);
        mView.startAnimation(animation);
        //不停的绘制位置
        mView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
        //不停的重置位置信息
        mRect.setEmpty();
        isFirst = true;
        isCalled = false;
    }

    public boolean isNeedMove() {
        //滑动距离
        int offset = mView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        //==0就是在顶部从上往下滑，scrollY = offset就是从底部往上滑
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

    public void setCallBack(Callback callBack) {
        this.callBack = callBack;
    }

    public interface Callback {
        void callback();
    }

    private int x;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //很重要
        super.onInterceptTouchEvent(ev);
        boolean intercept = false;
        int cx = (int) ev.getX();
        int cy = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = cy - y;
                int dx = cx - x;
                if (Math.abs(dy) > Math.abs(dx)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                break;
        }
        y = cy;
        x = cx;
        showLog.show(intercept);
        return intercept;
    }
}
