package com.example.rangeseekbar;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class RangeSeekBar extends View {
	private Paint mPaint1, mPaint2, mPaint3;
	private float[] mOffsets = new float[2];
	private float mSpace;
	float mPerWidth;
	private int mStart, mEnd;
	private int mSelected;
	private float mX;
	private OnRangeChangeListener mChangeListener;

	public RangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint1 = new Paint();
		mPaint1.setDither(true);
		mPaint1.setAntiAlias(true);
		mPaint1.setColor(Color.BLACK);
		mPaint1.setStyle(Paint.Style.FILL);
		mPaint1.setStrokeWidth(4);

		mPaint2 = new Paint();
		mPaint2.setDither(true);
		mPaint2.setAntiAlias(true);
		mPaint2.setColor(Color.BLACK);
		mPaint2.setAlpha(128);
		mPaint2.setStyle(Paint.Style.FILL);
		mPaint2.setStrokeWidth(12);

		mPaint3 = new Paint();
		mPaint3.setDither(true);
		mPaint3.setAntiAlias(true);
		mPaint3.setColor(Color.MAGENTA);
		mPaint3.setStyle(Paint.Style.FILL);
		mPaint3.setStrokeWidth(16);
	}

	public RangeSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public void setStartValue(int start) {
		this.mStart = start;
	}

	public void setEndValue(int end) {
		this.mEnd = end;
	}

	public void setOnRangeChangeListener(OnRangeChangeListener l) {
		this.mChangeListener = l;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mSpace = getMeasuredHeight() / 5;
		mOffsets[0] = mSpace;
		mOffsets[1] = getMeasuredWidth() - mSpace;
		int count = mEnd - mStart;
		mPerWidth = (getMeasuredWidth() - 2 * mSpace) / count;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = mStart; i <= mEnd; i++) {
			if (i == mStart || i == mEnd) {
				canvas.drawLine(mSpace + (i - mStart) * mPerWidth, 3 * mSpace,
						mSpace + (i - mStart) * mPerWidth, 4 * mSpace, mPaint1);
			} else {
				canvas.drawLine(mSpace + (i - mStart) * mPerWidth,
						3.5f * mSpace, mSpace + (i - mStart) * mPerWidth,
						4 * mSpace, mPaint1);
			}
		}

		canvas.drawLine(mSpace, 4 * mSpace, getMeasuredWidth() - mSpace,
				4 * mSpace, mPaint2);

		canvas.drawLine(mOffsets[0], 4 * mSpace, mOffsets[1], 4 * mSpace,
				mPaint3);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int actioin = event.getAction();
		mX = event.getX();
		if (mX < mSpace) {
			mX = mSpace;
		} else if (mX > getMeasuredWidth() - mSpace) {
			mX = getMeasuredWidth() - mSpace;
		}

		switch (actioin) {
		case MotionEvent.ACTION_DOWN:
			mSelected = Math.abs(mX - mOffsets[0]) < Math.abs(mX - mOffsets[1]) ? 0
					: 1;

		case MotionEvent.ACTION_MOVE:
			mOffsets[mSelected] = mX;
			break;

		case MotionEvent.ACTION_UP:
			// Log.i("TAG", mOffsets[0] + "," + mOffsets[1]);

			if (mChangeListener != null) {
				float start = Math.min(mOffsets[0], mOffsets[1]);
				float end = Math.max(mOffsets[0], mOffsets[1]);
				mChangeListener.onRangeChange(
						mStart + Math.round((start - mSpace) / mPerWidth),
						mStart + Math.round((end - mSpace) / mPerWidth));
			}
			int index = Math.round((mX - mSpace) / mPerWidth);
			handleUp(index);
			break;
		}
		invalidate();
		return true;
	}

	private void handleUp(int index) {
		ValueAnimator va = ValueAnimator
				.ofFloat(mX, mSpace + index * mPerWidth);
		va.setDuration(200);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		va.start();
		va.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (Float) animation.getAnimatedValue();
				mOffsets[mSelected] = value;
				postInvalidate();
			}
		});
	}

	public interface OnRangeChangeListener {
		void onRangeChange(int start, int end);
	}
}
