package com.zuo.ballgame.ballgame.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.zuo.ballgame.ballgame.options.BallGameOptions;
import com.zuo.ballgame.ballgame.thing.BallSpirit;


import java.util.Random;

/**
 * FileName: BallDownLayout
 * Author: zuo
 * Description: 球下降
 * Version: 4.9.1
 */
public class BallDownLayout extends RelativeLayout {

	/**
	 * 插补器
	 */
	private Interpolator[] interpolators = new Interpolator[]{new AccelerateDecelerateInterpolator(),
			new AccelerateInterpolator(), new DecelerateInterpolator()};

	private int mWidth, mHeight; // 宽,高

	private Random mRandom;// 随机数

	/**
	 * 进入动画持续时间
	 */
	private int mEnterDuration = 600;
	/**
	 * 动画持续时间
	 */
	private int mDuration = 4000;
	/**
	 * 球的缩放比例
	 */
	private float mScale = 1.0f;

	// 布局
	private LayoutParams mParams;

	/**
	 * 是否是相同大小（如果是则只计算一次）
	 */
	private boolean mIsSameSize = true;

	// 起始点
	private PointF mStartPointF;


	public BallDownLayout(@NonNull Context context) {
		this(context, null);
	}

	public BallDownLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BallDownLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	/**
	 * 初始化
	 *
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {
		mRandom = new Random();
		mStartPointF = new PointF();
		mParams = new LayoutParams((int) BallGameOptions.BALL_LENGTH, (int)BallGameOptions.BALL_LENGTH);
	}

	/**
	 * 侧量
	 *
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 测量后的宽高
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();

	}

	/**
	 * 添加桃心
	 */
	public void addBall(BallSpirit ballSpirit) {
		ImageView iv = getHeartView(ballSpirit.getImage());
		addView(iv);
		updateStartPointF(iv, ballSpirit);

		Animator animator = getAnimator(iv, ballSpirit);
		animator.addListener(new EndAnimatorListener(iv));
		animator.start();

	}

	/**
	 * 获取一个桃心
	 *
	 * @param resId
	 * @return
	 */
	private ImageView getHeartView(@DrawableRes int resId) {
		ImageView iv = new ImageView(getContext());
		iv.setLayoutParams(mParams);
		iv.setImageResource(resId);
		return iv;
	}


	private AnimatorSet getEnterAnimator(View target) {
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 1f, mScale);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 1f, mScale);
		ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.5f, 1f);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(alpha);
		animatorSet.setInterpolator(new LinearInterpolator());
		animatorSet.setDuration(mEnterDuration);
		return animatorSet;
	}
	/**
	 * 贝塞尔曲线动画
	 *
	 * @param target
	 * @param ballSpirit
	 * @return
	 */
	private ValueAnimator getBezierCurveAnimator(final View target, BallSpirit ballSpirit) {

		// 传入 起点 和 终点
		ValueAnimator valueAnimator = ValueAnimator.ofObject(new TypeEvaluator<PointF>() {
			@Override
			public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

				// 三次方贝塞尔曲线 逻辑 通过四个点确定一条三次方贝塞尔曲线
				PointF pointF = new PointF();
				pointF.x = ballSpirit.getLoacationX();

				pointF.y = (ballSpirit.getLoacationY()) + (ScreenUtils.getAppScreenHeight()- SizeUtils.dp2px(24)-ballSpirit.getLoacationY())*fraction;
				return pointF;
			}
			// 起点和终点
		}, mStartPointF, new PointF(mRandom.nextInt(mWidth), ScreenUtils.getAppScreenHeight()- SizeUtils.dp2px(24)));

		valueAnimator.setInterpolator(new BounceInterpolator());

		valueAnimator.addUpdateListener(animation -> {
			PointF pointF = (PointF) animation.getAnimatedValue();
			// 更新target的坐标
			target.setX(pointF.x);
			target.setY(pointF.y);

		});
		valueAnimator.setDuration(mDuration+mRandom.nextInt(2000));
		return valueAnimator;
	}

	/**
	 * 起始动画
	 *
	 * @param target
	 * @param ballSpirit
	 * @return
	 */
	private Animator getAnimator(View target, BallSpirit ballSpirit) {

		AnimatorSet animatorSet = new AnimatorSet();
		//animatorSet.playSequentially(getEnterAnimator(target), getBezierCurveAnimator(target, ballSpirit));
		animatorSet.playSequentially(getBezierCurveAnimator(target, ballSpirit));
		return animatorSet;

	}

	/**
	 * 测量
	 *
	 * @param target
	 */
	private void makeMeasureSpec(View target) {
		int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		target.measure(spec, spec);
	}

	/**
	 * 起点
	 *
	 * @param target
	 * @param ballSpirit
	 * @return
	 */
	private void updateStartPointF(View target, BallSpirit ballSpirit) {
		if (mStartPointF.x == 0 || mStartPointF.y == 0 || !mIsSameSize) {
			makeMeasureSpec(target);
			mStartPointF.x = ballSpirit.getLoacationX();
			mStartPointF.y = ballSpirit.getLoacationX();
		}
	}

	/**
	 * 随机贝塞尔曲线中间的点
	 *
	 * @param scale
	 * @return
	 */
	private PointF randomPointF(float scale) {
		PointF pointF = new PointF();
		pointF.x = mRandom.nextInt(mWidth);
		pointF.y = mRandom.nextInt(mHeight) / scale;

		return pointF;
	}

	/**
	 * 随机一个插补器
	 *
	 * @return
	 */
	private Interpolator randomInterpolator() {
		return interpolators[mRandom.nextInt(interpolators.length)];
	}


	/**
	 * 动画监听
	 */
	private class EndAnimatorListener extends AnimatorListenerAdapter {

		private View target;

		public EndAnimatorListener(View target) {
			this.target = target;
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			super.onAnimationEnd(animation);
			// 动画结束 移除target
			removeView(target);
		}
	}

}
