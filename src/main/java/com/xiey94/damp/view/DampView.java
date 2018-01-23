package com.xiey94.damp.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.xiey94.damp.util.showLog;

/**
 * @author : xiey
 * @project name : As30.
 * @package name  : com.xiey94.damp.view.
 * @date : 2018/1/19.
 * @signature : do my best.
 * @from : https://www.cnblogs.com/anni-qianqian/p/5755734.html
 * @explain :
 */

public class DampView extends ScrollView {

    public static final int DURATION = 200;
    private Scroller mScroller;
    private int top;
    private float startY, currentY;
    private ImageView imageView;
    private int imageViewH;
    private boolean isFirst = true;
    private View inner;

    public DampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        int action = ev.getAction();
        if (!mScroller.isFinished()) {
            return super.onTouchEvent(ev);
        }
        currentY = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                top = imageView.getBottom();
                imageViewH = imageView.getHeight();
                startY = currentY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (imageView.isShown() && getScrollY() == 0) {
                    if (isFirst) {
                        startY = currentY;
                        isFirst = false;
                    }
                    int height = (int) (top + (currentY - startY) / 5);
                    ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    params.height = height;
                    imageView.setLayoutParams(params);
                }

                if (inner.getMeasuredHeight() <= getScrollY() + getHeight()) {
                    if (isFirst) {
                        startY = currentY;
                        isFirst = false;
                    }

                    int height = (int) ((currentY - startY) / 5);
                    inner.layout(inner.getLeft(), 0, inner.getRight(), inner.getMeasuredHeight() - height);
                }

                break;
            case MotionEvent.ACTION_UP:
                mScroller.startScroll(0, imageView.getBottom(), 0, imageViewH - imageView.getBottom(), DURATION);
                isFirst = true;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.height = mScroller.getCurrY();
            showLog.show(mScroller.getCurrY());
            imageView.setLayoutParams(params);
        }
    }
}