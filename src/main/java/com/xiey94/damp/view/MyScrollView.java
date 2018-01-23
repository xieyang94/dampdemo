package com.xiey94.damp.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.xiey94.damp.util.showLog;

/**
 * @author : xiey
 * @project name : As30.
 * @package name  : com.xiey94.damp.view.
 * @date : 2018/1/19.
 * @signature : do my best.
 * @from : http://blog.csdn.net/baiyuliang2013/article/details/25815407
 * @explain : 头部图片进行拉伸，但是头部以下不跟着动，不方便调整
 */

public class MyScrollView extends ScrollView {
    private View inner;

    private float y;

    //记录位置
    private Rect normal = new Rect();
    //过滤到第一次移动
    private boolean isCount = false;
    //是否开始移动
    private boolean isMoveing = false;
    private ImageView imageView;
    private int initTop, initBootom;
    private int top, bottom;

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 获取第一个View，也就是整个
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
        super.onFinishInflate();
    }

    /**
     * 事件处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner != null) {
            commonTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 事件处理
     */
    private void commonTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                top = initTop = imageView.getTop();
                bottom = initBootom = imageView.getBottom();
                break;
            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                int dalteY = (int) (nowY - y);
                if (!isCount) {
                    dalteY = 0;
                }

                //当下往上滑，脱离了阻尼的范畴
                if (dalteY < 0 && top <= initTop) {
                    return;
                }

                isNeedMove();

                if (isMoveing) {
                    //保存位置信息
                    if (normal.isEmpty()) {
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                    }
                    //移动布局
                    inner.layout(inner.getLeft(), inner.getTop() + dalteY / 5, inner.getRight(), inner.getBottom() + dalteY / 5);

                    top = top - dalteY / 5;
                    bottom = initBootom;
                    imageView.layout(imageView.getLeft(), top, imageView.getRight(), bottom);
                    isCount = true;
                    y = nowY;
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isMoveing = false;
                if (isNeedAnimation()) {
                    animation();
                }
                break;
            default:
                break;
        }
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    /***
     * 回缩动画
     */
    public void animation() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", top, initTop);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(inner, "translationY", inner.getTop(), normal.top);
        //此处定义的是100毫秒，这里有个弊端：那就是当你100毫秒没有执行完又点击滑动了，此时图片的高度就变大了，会一直变大，
        // 你可以把时间设置的更短一些，让他掌握不了人类的速度，或者设置一个标签
        set.setDuration(100);
        set.playTogether(animator, animator2);
        set.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                showLog.show("" + value);
                imageView.layout(imageView.getLeft(), initTop, imageView.getRight(), (int) (initBootom - value));
            }
        });

        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
        isCount = false;
        y = 0;// 手指松开要归0.

    }

    public void isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // 0是顶部，后面那个是底部
//      if (scrollY == 0 || scrollY == offset) {
//          isMoveing = true;
//      }
        if (scrollY == 0) {
            isMoveing = true;
        }
    }
}
