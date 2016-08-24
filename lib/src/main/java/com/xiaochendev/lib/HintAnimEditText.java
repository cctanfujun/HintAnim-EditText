package com.xiaochendev.lib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.PathInterpolator;
import android.widget.EditText;

/**
 * Created by tanfujun on 8/18/16.
 * <p/>
 * 注意使用这个控件不要使用EditText 原有的hint，使用控件内方法替代
 */
public class HintAnimEditText extends EditText {


    CharSequence mAnimHintString = "";

    Paint mHintPaint;

    int mDefaultAlpha;
    float mDefaultTextSize;
    float mTextLength;


    public HintAnimEditText(Context context) {
        super(context);
        init();
    }

    public HintAnimEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HintAnimEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHintPaint = new Paint();
        mHintPaint.setAntiAlias(true);
        mHintPaint.setColor(getHintTextColors().getDefaultColor());
        mHintPaint.setAlpha((int) getAlpha());
        mHintPaint.setColor(Color.GRAY);
        mHintPaint.setTextSize(getTextSize());
        mHintPaint.setTextAlign(Paint.Align.CENTER);
        mDefaultTextSize = getTextSize();
        mDefaultAlpha = mHintPaint.getAlpha();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (TextUtils.isEmpty(getText().toString())) {
            if (Float.floatToRawIntBits(mTextLength) == 0) {
                mTextLength = getPaint().measureText(mAnimHintString.toString());
            }
            float startX = getCompoundPaddingLeft();
            canvas.drawText(mAnimHintString.toString(), startX + mTextLength / 2, getLineBounds(0, null), mHintPaint);
        }

    }

    /**
     * 修改hint 文字
     *
     * @param hint
     */
    public void setHintString(final CharSequence hint) {
        mAnimHintString = hint;
        if (mAnimHintString != null) {
            mTextLength = getPaint().measureText(mAnimHintString.toString());
        }else{
            mTextLength = 0;
        }
        invalidate();
    }

    /**
     * 使用动画切换hint
     *
     * @param hint
     */
    public void changeHintWithAnim(final CharSequence hint) {


        ValueAnimator rollPlayAnim = ValueAnimator.ofFloat(100, 90);
        rollPlayAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();

                float textsize = mDefaultTextSize * (value / 100);
                float alpha = (value - 90) / 10 * mDefaultAlpha;

                mHintPaint.setTextSize(textsize);
                mHintPaint.setAlpha((int) alpha);

                invalidate();
            }
        });
        rollPlayAnim.setInterpolator(new PathInterpolator(0.3f, 0, 0.7f, 1));
        rollPlayAnim.setDuration(250);
        rollPlayAnim.setRepeatMode(ValueAnimator.REVERSE);
        rollPlayAnim.setRepeatCount(1);
        rollPlayAnim.setStartDelay(30);


        rollPlayAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTextLength = getPaint().measureText(mAnimHintString.toString());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimHintString = hint;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mAnimHintString = hint;
                mTextLength = getPaint().measureText(mAnimHintString.toString());
            }
        });

        rollPlayAnim.start();

    }

    public CharSequence getAnimHintString() {
        return mAnimHintString;
    }


}
