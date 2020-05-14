package com.zuo.ballgame.ballgame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;
import com.zuo.ballgame.ballgame.options.BallGameOptions;


/**
 * FileName: CircleProgressView
 * Author: zuo
 * Description: 球下降
 * Version: 4.9.1
 */
public class CircleProgressView extends android.support.v7.widget.AppCompatImageView {

	public int getmProcess() {
		return mProcess/128;
	}

	private int mProcess = 0;



	private int mTotal = BallGameOptions.COLLECT_ONE_SECOND *BallGameOptions.Stay_Time;
	private int mStartAngle = -90;
	private RectF mRectF;

	private Paint mPaint;
	private Drawable mDrawable;
	private int mStroke;

	public CircleProgressView(Context context) {
		this(context, null);
	}

	public CircleProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mStroke = SizeUtils.dp2px(BallGameOptions.BALL_Progress_Stoke);
		mPaint = new Paint();
		mPaint.setColor(Color.TRANSPARENT);
		mPaint.setStrokeWidth(mStroke);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true);
		mDrawable = new Progress();

		setImageDrawable(mDrawable);
	}

	public void setTotal(int seconds) {
		this.mTotal = BallGameOptions.COLLECT_ONE_SECOND * seconds;
		mDrawable.invalidateSelf();
	}
	public int getmTotal() {
		return mTotal;
	}

	/**
	 * 每个采集点算1
	 * @param process
	 */
	public void setProcess(int process) {
		this.mProcess = process;
		post(new Runnable() {
			@Override
			public void run() {
				mDrawable.invalidateSelf();
			}
		});
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
			super.onMeasure(heightMeasureSpec, heightMeasureSpec);
		} else {
			super.onMeasure(widthMeasureSpec, widthMeasureSpec);
		}
	}

	private class Progress extends Drawable {
		@Override
		public void draw(Canvas canvas) {
			int width = getWidth();
			int pd = mStroke / 2 + 1;
			if (mRectF == null) {
				mRectF = new RectF(pd, pd, width - pd, width - pd);
			}
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(Color.TRANSPARENT);
			canvas.drawCircle(width / 2, width / 2, width / 2 - pd, mPaint);
			mPaint.setColor(Color.YELLOW);
			canvas.drawArc(mRectF, mStartAngle, mProcess * 360 / (float) mTotal, false, mPaint);
		}

		@Override
		public void setAlpha(int alpha) {

		}

		@Override
		public void setColorFilter(ColorFilter colorFilter) {

		}

		@Override
		public int getOpacity() {
			return PixelFormat.TRANSPARENT;
		}
	}

}
