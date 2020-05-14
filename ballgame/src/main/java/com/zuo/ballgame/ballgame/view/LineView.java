package com.zuo.ballgame.ballgame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.zuo.ballgame.ballgame.GameEventMsg;
import com.zuo.ballgame.ballgame.activity.BaseGameActivity;
import com.zuo.ballgame.ballgame.options.BallGameOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

public class LineView extends View {
	// 路径
	public Path path;
	// 画笔
	public Paint paint = null;


	private ImageView iv_ball;

	public float x;
	public float xLauch = 0;


	int measuredWidth;
	int measuredHeight;


	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	int i = 0;
	double dvX = 0;
	private AnimationSet animationSet;

	public LineView(Context context, AttributeSet set) {
		super(context, set);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float temp = event.getX();
		if (temp > measuredWidth - BallGameOptions.BALL_LENGTH) {
			x = measuredWidth - BallGameOptions.BALL_LENGTH;
		} else if (temp < BallGameOptions.BALL_LENGTH / 2) {
			x = BallGameOptions.BALL_LENGTH / 2;
		} else {
			x = temp;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPath(path, paint);
		path.reset();
	}


	public void setLayout(int measuredWidths, int measuredHeights, ImageView iv_ball) {
		measuredHeight = measuredHeights;
		measuredWidth = measuredWidths;
		dvX = (measuredWidth) / (BallGameOptions.MAX_STRENGTH - BallGameOptions.MIN_STRENGTH) / (BallGameOptions.ROW_NUM + 0.5) * BallGameOptions.ROW_NUM;
		this.iv_ball = iv_ball;
		path = new Path();
		path.moveTo(measuredWidth / 2, measuredHeight);
		paint = new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setPathEffect(new DashPathEffect(new float[]{20f, 10f}, 0));
		x = BallGameOptions.BALL_LENGTH / 2;
		path.lineTo(x, 0);
		invalidate();
		beat();
	}


	public void startLauch() {
		xLauch = x;
		animationSet = new AnimationSet(true);
		RotateAnimation rotateAnimation = new RotateAnimation(0, BallGameOptions.BALL_LAUCH_RotateAnimation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(BallGameOptions.BALL_LAUCH_Duration);
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, xLauch - (measuredWidth / 2), Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -measuredHeight);
		translateAnimation.setDuration(BallGameOptions.BALL_LAUCH_Duration);
		animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(translateAnimation);
		iv_ball.startAnimation(animationSet);

		animationSet.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				EventBus.getDefault().post(new GameEventMsg(BallGameOptions.BOMB_TAG, xLauch));
				BaseGameActivity mActivity = (BaseGameActivity) getContext();
				((BaseGameActivity) getContext()).playSound(mActivity.brickSoundId, false);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}


	public synchronized void beat() {
		if (paint == null) {
			return;
		}
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {

					if (i == 0) {
						i++;
						paint.setPathEffect(new DashPathEffect(new float[]{20f, 10f}, 0));
					} else {
						i = 0;
						paint.setPathEffect(new DashPathEffect(new float[]{20f, 10f}, 10));
					}
					path.moveTo(measuredWidth / 2, measuredHeight);
					path.lineTo(x, 0);
					((BaseGameActivity) getContext()).runOnUiThread(() -> invalidate());

				}
			};
			if (mTimer != null) {
				mTimer.schedule(mTimerTask, BallGameOptions.LINE_TIME, BallGameOptions.LINE_TIME);
			}
		}
	}


	// 取消心跳包
	public void stopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}

	public void setStrengthV(float mStrength) {
		LogUtils.w("setStrength_1", mStrength + "::" + x);
		if (dvX == 0) {
			return;
		}

		if (mStrength < BallGameOptions.MIN_STRENGTH) {
			mStrength = BallGameOptions.MIN_STRENGTH;
		}
		if (mStrength > BallGameOptions.MAX_STRENGTH) {
			mStrength = BallGameOptions.MAX_STRENGTH;
		}
		mStrength = mStrength - BallGameOptions.MIN_STRENGTH;
		float temp = (float) (dvX * mStrength);
		//将截断位置移动到小球的中心点
		x = temp - BallGameOptions.BALL_LENGTH / 2;

		if (temp > measuredWidth - (BallGameOptions.BALL_LENGTH)) {
			x = measuredWidth - BallGameOptions.BALL_LENGTH;
		} else if (temp < BallGameOptions.BALL_LENGTH / 2) {
			x = BallGameOptions.BALL_LENGTH / 2;
		}
		LogUtils.w("setStrength_1", mStrength + "::" + x);
	}

	@Override
	public void setVisibility(int visibility) {
		if (visibility == INVISIBLE || visibility == GONE) {
			stopTimer();
		} else {
			beat();
		}
	}
}
