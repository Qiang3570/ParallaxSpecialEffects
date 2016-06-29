package com.johnny.parallaxspecialeffects.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * 带有视差特效的ListView
 * @author poplar
 *
 */
public class ParallaxListView extends ListView {

    private ImageView iv_header;
    /**图片的高度*/
    private int drawableHeight;
    /**ImageView的原始高度*/
    private int originalHeight;

    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setParallaxImage(ImageView iv_header) {
        this.iv_header = iv_header;
        drawableHeight = iv_header.getDrawable().getIntrinsicHeight();
        originalHeight = iv_header.getMeasuredHeight();

    }

    /**
     *
     * @param deltaX
     * @param deltaY 竖直方向的瞬时偏移量, 顶部下拉 -, 底部上拉 +
     * @param scrollX
     * @param scrollY 垂直方向超出滚动的距离, 顶部-, 底部+
     * @param scrollRangeX
     * @param scrollRangeY 垂直方向滚动范围
     * @param maxOverScrollX
     * @param maxOverScrollY 最大超出滚动的距离.
     * @param isTouchEvent 是否是手指触摸.true 触摸到边界, false 惯性到边界
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if(deltaY < 0 && isTouchEvent){/*顶部下拉, 手指触摸*/
            int newHeight = (int) (iv_header.getHeight() + Math.abs(deltaY) / 2.0f);/*下拉的瞬时偏移量加给Header*/
            if(newHeight <= drawableHeight){/*限制最大范围*/
                iv_header.getLayoutParams().height = newHeight;/*让新的高度生效*/
                iv_header.requestLayout();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP){/*弹回去*/
            ValueAnimator animator = ValueAnimator.ofInt(iv_header.getHeight(), originalHeight);/*把当前的Header高度(220), 设置回原来的高度160*/
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator anim) {
                    float fraction = anim.getAnimatedFraction();
                    Integer newHeight = (Integer) anim.getAnimatedValue();
                    iv_header.getLayoutParams().height = newHeight;/*让新的高度生效*/
                    iv_header.requestLayout();
                }
            });
            animator.setInterpolator(new OvershootInterpolator(4));
            animator.setDuration(500);
            animator.start();
        }
        return super.onTouchEvent(ev);
    }
}