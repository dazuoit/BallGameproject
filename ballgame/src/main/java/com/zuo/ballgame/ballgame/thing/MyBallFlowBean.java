package com.zuo.ballgame.ballgame.thing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.animation.BounceInterpolator;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.zuo.ballgame.ballgame.options.BallGameOptions;


import java.util.Random;

/**
 * @author zuo
 * @filename: MyBallFlowBean
 * @date: 2020/5/6
 * @description: 下落的view
 * @version: 版本号
 */

public class MyBallFlowBean {

	/**
	 * 心图
	 */
	private Bitmap bitmap;
	/**
	 * 绘制bitmap的矩阵  用来做缩放和移动的
	 */
	private Matrix matrix = new Matrix();
	/**
	 * 产生随机数
	 */
	private Random random;

	public boolean isEnd = false;//是否结束

	public boolean isPause = false;

	/**
	 * 动画持续时间
	 */

	private PointF pointF;
	private ValueAnimator valueAnimator;
	private float dy = 0;
	private float dx = 0;

	public MyBallFlowBean(BallSpirit ballSpirit, Context context) {
		random = new Random();
		Bitmap mbitmap = BitmapFactory.decodeResource(context.getResources(), ballSpirit.getImage());
		bitmap = Bitmap.createScaledBitmap(mbitmap, (int) BallGameOptions.BALL_LENGTH, (int) BallGameOptions.BALL_LENGTH, true);
		if (!bitmap.isRecycled()) {
			mbitmap.recycle();
		}
		Animator animator = getAnimator(ballSpirit);
		animator.addListener(new EndAnimatorListener());
		animator.start();

	}


	private Animator getAnimator(BallSpirit ballSpirit) {
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playSequentially(getBezierCurveAnimator(ballSpirit));
		return animatorSet;

	}

	public void pause(){
		if (valueAnimator != null  &&!isEnd){
			valueAnimator.pause();
			isPause = true;
		}
	}

	public void resume(){
		if (valueAnimator != null  &&!isEnd){
			valueAnimator.resume();
			isPause =false;
		}
	}
	private ValueAnimator getBezierCurveAnimator(BallSpirit ballSpirit) {
		valueAnimator = ValueAnimator.ofObject(new TypeEvaluator<PointF>() {
			@Override
			public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

				// 三次方贝塞尔曲线 逻辑 通过四个点确定一条三次方贝塞尔曲线
				PointF pointF = new PointF();
				pointF.x = ballSpirit.getLoacationX();

				pointF.y = (ballSpirit.getLoacationY()) + (ScreenUtils.getAppScreenHeight() - SizeUtils.dp2px(24) - ballSpirit.getLoacationY()) * fraction;
				return pointF;
			}
			// 起点和终点
		}, new PointF(ballSpirit.getLoacationX(), ballSpirit.getLoacationY()), new PointF(ballSpirit.getLoacationX(), ScreenUtils.getAppScreenHeight() - SizeUtils.dp2px(24)));

		valueAnimator.setInterpolator(new BounceInterpolator());

		valueAnimator.addUpdateListener(animation -> {
			pointF = (PointF) animation.getAnimatedValue();

			// 更新target的坐标
		});
		valueAnimator.setDuration(BallGameOptions.ball_FLOW_Duration + random.nextInt(BallGameOptions.ball_FLOW_ADD_RANDOM));
		return valueAnimator;
	}


	/**
	 * 主要绘制函数
	 */
	public void draw(Canvas canvas, Paint p) {
		if (bitmap != null) {
			matrix.postTranslate(pointF.x - dx, pointF.y - dy);
			dy = pointF.y;
			dx = pointF.x;
			canvas.drawBitmap(bitmap, matrix, p);
		} else {
			isEnd = true;
		}
	}

	/**
	 * 动画监听
	 */
	private class EndAnimatorListener extends AnimatorListenerAdapter {

		@Override
		public void onAnimationEnd(Animator animation) {
			super.onAnimationEnd(animation);
			isEnd = true;
		}
	}

	public void release() {
		if (valueAnimator != null) {
			valueAnimator.cancel();
			valueAnimator = null;
		}
		if (bitmap != null) {
			if (!bitmap.isRecycled()) {
				//TODO: 不知道原因
				//Error, cannot access an invalid/free'd bitmap here!
				//bitmap.recycle();
			}
			bitmap = null;
		}
	}

}
