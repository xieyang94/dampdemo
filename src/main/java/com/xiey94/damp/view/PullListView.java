package com.xiey94.damp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * @author : xiey.
 * @!package name  : com.xiey94.damp.view.
 * @project name : As30.
 * @date : 2018/1/18.
 * @signature : do my best
 * @explain : 自定义阻尼效果
 * @from : http://blog.51cto.com/sunjilife/1165473
 * @整体思想 : 第一次点击下去的时候，获取点击的位置,如果从顶部往上滑，在ACTION_MOVE那是达不到位置条件的，所以mDistance就=0，
 * 就在ACTION_UP中到不了条件中了；在对地段往下滑，同样在ACTION_MOVE那是达不到条件的，所以后续一样，够不成阻尼效果；
 * 只有在顶端往下滑或者底端往上滑才可以实现阻尼效果；
 * 顶端往下滑时，mLastDownY最开始是等于0的，或者从中途位置过来，最终结果是mLastDownY不等于0的，在ACTION_MOVE中达到位置条件，
 * 所以有了滑动效果，此时mDistance不等于0；手指从开时，在ACTION_UP中达到条件，mPositive表示当前在做阻尼运动是在顶端还是在底端
 * 的一种标识，mStep是一个"加速带"，然后响应了run方法，在run方法中，主要功能就是恢复以达到阻尼效果
 * mDistance开始骤减，然后滑动回缩，然后判断在顶端下滑的状态下（正常情况下是：往下滑是mDistance<0的，顶端往上滑是mDistance>0的），
 * 如果mDistance>=0，则说明已经恢复完成了甚至已经超过了，此时达到了条件，然后全部置0，如果没有达到这个置0的条件，则说明mStep这个“加速带”
 * 还不给力，还要加大，然后再接着减小mDistance；
 * 底部往上滑原理差不多
 */

public class PullListView extends ListView implements Runnable {

    private float mLastDownY = 0f;
    /**
     * 滑动的距离
     */
    private int mDistance = 0;
    /**
     * 缓冲过程，将滑动的距离一步一步还原
     */
    private int mStep = 0;
    /**
     * 表示向上或者向下的一种状态
     */
    private boolean mPositive = false;

    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mLastDownY == 0 && mDistance == 0) {
                    mLastDownY = ev.getY();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mDistance != 0) {
                    mStep = 1;
                    mPositive = mDistance >= 0;
                    this.post(this);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mLastDownY != 0) {
                    mDistance = (int) (mLastDownY - ev.getY());
                    if (mDistance < 0
                            && getFirstVisiblePosition() == 0
                            && getChildAt(0).getTop() == 0
                            || (mDistance > 0 && getLastVisiblePosition() == getCount() - 1)) {
                        //第一个往下拉，最后一个往上拉都要这种效果
                        mDistance /= 2;
                        scrollTo(0, mDistance);
                        return true;
                    }
                }
                mDistance = 0;
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public void run() {
        mDistance += mDistance > 0 ? -mStep : mStep;
        scrollTo(0, mDistance);
        //滑动回归后不再滑动
        if ((mPositive && mDistance <= 0) || (!mPositive && mDistance >= 0)) {
            scrollTo(0, 0);
            mDistance = 0;
            mLastDownY = 0;
            return;
        }
        mStep += 1;
        this.post(this);
    }

}
