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
 * @date : 2018/1/22.
 * @signature : do my best.
 * @from : http://www.haolizi.net/example/view_799.html
 * @explain :
 */

public class PersonalScrollView extends ScrollView {

    //孩子VIew，也就是整个
    private View inner;
    //点击时的Y坐标
    private float touchY;
    //Y轴滑动的距离
    private float deltaY;
    //首次点击的Y坐标
    private float initTouchY;
    //记录位置
    private Rect normal = new Rect();
    //是否开始移动
    private boolean isMoveing = false;
    //背景图控件
    private ImageView imageView;
    //初始高度
    private int initTop, initBottom;
    //拖动时高度
    private int current_Top, current_Bottom;

    //状态：上部、下部、默认
    private enum State {
        UP, DOWN, NORMAL
    }

    //默认状态
    private State state = State.NORMAL;

    //注入背景图
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }


    public PersonalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner != null) {
            commonTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 触摸事件
     */
    public void commonTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initTouchY = ev.getY();
                current_Top = initTop = imageView.getTop();
                current_Bottom = initBottom = imageView.getBottom();
                break;
            case MotionEvent.ACTION_MOVE:
                touchY = ev.getY();
                //滑动距离
                deltaY = touchY - initTouchY;
                //首次Touch要判断方位
                if (deltaY < 0 && state == State.NORMAL) {
                    //往上滑
                    state = State.UP;
                } else if (deltaY > 0 && state == State.NORMAL) {
                    //往下滑
                    state = State.DOWN;
                }

                if (state == State.UP) {
                    deltaY = deltaY < 0 ? deltaY : 0;
                    isMoveing = false;
                } else if (state == State.DOWN) {
                    if (getScaleY() <= deltaY) {
                        isMoveing = true;
                    }
                    deltaY = deltaY < 0 ? 0 : deltaY;
                }

                if (isMoveing) {
                    if (normal.isEmpty()) {
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                    }

                    //移动布局
                    float inner_move_H = deltaY / 5;

                    inner.layout(normal.left, (int) (normal.top + inner_move_H), normal.right, (int) (normal.bottom + inner_move_H));

                    float image_move_H = deltaY / 10;
                    current_Top = (int) (initTop - image_move_H * 2);
                    current_Bottom = (int) (initBottom);
                    imageView.layout(imageView.getLeft(), current_Top, imageView.getRight(), current_Bottom);

                }
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animation();
                }

                //到顶部的时候
                if (getScaleY() == 0) {
                    state = State.NORMAL;
                }

                isMoveing = false;
                touchY = 0;
                break;
            default:
                break;
        }
    }

    public void animation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", current_Top, initTop);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(inner, "translationY", inner.getTop(), normal.top);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.playTogether(animator, animator2);
        set.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                showLog.show("" + value);
                imageView.layout(imageView.getLeft(), initTop, imageView.getRight(), (int) (initBottom - value));
            }
        });
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    /**
     * 是否需要开启动画
     */
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }


}
