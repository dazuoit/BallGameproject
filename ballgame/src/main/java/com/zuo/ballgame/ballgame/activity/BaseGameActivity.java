package com.zuo.ballgame.ballgame.activity;

import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.blankj.utilcode.util.BarUtils;
import com.zuo.ballgame.ballgame.R;
import com.zuo.ballgame.ballgame.options.BallGameOptions;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author zuo
 * @filename: BaseGameActivity
 * @date: 2020/5/6
 * @description: 描述
 * @version: 版本号
 */
public abstract class BaseGameActivity extends AppCompatActivity {

	private ValueAnimator valueAnimator;
	int[] drawable = {R.drawable.xiaomai_static, R.drawable.xiaomai_lauch, R.drawable.xiaomai_static};
	protected SoundPool soundPool;
	public Map<Integer, Integer> soundMap;
	protected MyHandler handler = new MyHandler(this); // handler
	protected int backGroudSoundId = 1;
	public int brickSoundId = 2;
	protected int clickSoundId = 3;
	protected boolean isOpenSound = true;
	private View mRootView;
	protected AppCompatActivity eventTag; // eventbus tag

	protected static class MyHandler extends Handler {
		private final WeakReference<BaseGameActivity> mActivity;

		public MyHandler(BaseGameActivity activity) {
			mActivity = new WeakReference<BaseGameActivity>(activity);
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(getOrientation());
		setContentView(getLayoutId());
		mRootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
		if (mRootView != null) {
			mRootView.setFitsSystemWindows(true);
		}
		initView();
		initData();
		initListener();
	}

	public int getOrientation() {
		return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	}

	public abstract int getLayoutId();

	protected void initView() {
		BarUtils.setStatusBarVisibility(this, false);
		// 不待机
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 得到屏幕长宽
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		loadSounds();

	}

	protected void initData() {
	}


	protected void initListener() {
	}

	private void loadSounds() {
		if (Build.VERSION.SDK_INT >= 21) {
			SoundPool.Builder builder = new SoundPool.Builder();
			//传入最多播放音频数量,
			builder.setMaxStreams(3);
			//AudioAttributes是一个封装音频各种属性的方法
			AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
			//设置音频流的合适的属性
			attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
			//加载一个AudioAttributes
			builder.setAudioAttributes(attrBuilder.build());
			soundPool = builder.build();
		} else {
			/**
			 * 第一个参数：int maxStreams：SoundPool对象的最大并发流数
			 * 第二个参数：int streamType：AudioManager中描述的音频流类型
			 *第三个参数：int srcQuality：采样率转换器的质量。 目前没有效果。 使用0作为默认值。
			 */
			soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		}


		soundMap = new HashMap<Integer, Integer>();
		soundMap.put(backGroudSoundId, soundPool.load(this, R.raw.background, 1));
		soundMap.put(brickSoundId, soundPool.load(this, R.raw.bricksound2, 1));
		soundMap.put(clickSoundId, soundPool.load(this, R.raw.click, 1));

		soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
			if (status == 0 && sampleId == soundMap.get(backGroudSoundId)) {
				playSound(backGroudSoundId, true);
			}
		});
	}

	/**
	 * 设置eventbus
	 *
	 * @param eventTag
	 */
	protected void setEventTag(AppCompatActivity eventTag) {
		if (eventTag != null) {
			this.eventTag = eventTag;
			registerEventBus(eventTag);
		}
	}

	// 多布局
	protected void initGridLayoutManager(RecyclerView recyclerView) {
		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, BallGameOptions.ROW_NUM * 2 + 1);
		gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				//偶数行
				if (BallGameOptions.isNeed3(position)) {
					return 3;
				} else {
					return 2;
				}
			}
		});
		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				super.getItemOffsets(outRect, view, parent, state);
				//交错为1/6
				outRect.bottom = (int) -(BallGameOptions.BALL_LENGTH / 6);
			}
		});
	}


	/**
	 * 隐藏球
	 *
	 * @param view
	 */
	public void hideView(final View view) {
		long startDelay = 300L;
		ValueAnimator valueAnimator = new ValueAnimator();
		valueAnimator.setDuration(startDelay + 1000).setFloatValues(0f, 1f);
		// 使View颤抖
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			Random random = new Random();

			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				view.setTranslationX((random.nextFloat() - 0.5F) * view.getWidth() * 0.05F);
				view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.05f);
			}
		});
		valueAnimator.start();
		// 将View 缩放至0、透明至0
		view.animate().setDuration(260).setStartDelay(startDelay).scaleX(0).scaleY(0).alpha(0).start();
	}

	/**
	 * @param imageView
	 */
	public void startLanuch(ImageView imageView) {
		if (valueAnimator == null) {
			valueAnimator = ValueAnimator.ofInt(0, 2).setDuration(BallGameOptions.XIAOMAI_Duration);
			valueAnimator.addUpdateListener(animation -> {
				int i = (int) animation.getAnimatedValue();
				imageView.setImageResource(drawable[i]);
			});
		}
		valueAnimator.start();
	}


	/**
	 * view1 转到view2
	 *
	 * @param view1
	 * @param view2
	 */
	public AnimationSet viewTransAnim(View view1, View view2) {
		int[] location1 = new int[2];
		view1.getLocationInWindow(location1);
		int[] location2 = new int[2];
		view2.getLocationInWindow(location2);
		AnimationSet animationSet = new AnimationSet(true);
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(BallGameOptions.BALL_TRANS_Duration);
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, location2[0] - location1[0], Animation.START_ON_FIRST_FRAME, 0, Animation.ABSOLUTE, location2[1] - location1[1]);
		translateAnimation.setDuration(BallGameOptions.BALL_TRANS_Duration);
		animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(translateAnimation);
		view1.startAnimation(animationSet);
		return animationSet;
	}

	/**
	 * 音频id
	 *
	 * @param
	 */
	public void playSound(int id, boolean isLoop) {
		if (isOpenSound) {
			soundPool.play(soundMap.get(id), 1, 1, 0, isLoop ? -1 : 1, 1);
		}
	}

	public void resumeSound(int soundId) {
		if (isOpenSound) {
			soundPool.resume(soundId);

		}
	}

	/**
	 * autopause 有问题
	 *
	 * @param soundId
	 */
	public void stopSound(int soundId) {
		for (Integer key : soundMap.keySet()) {
			soundPool.pause(soundMap.get(key));
		}
	}

	/**
	 * 订阅EventBus
	 *
	 * @param subscribe 订阅
	 */
	protected void registerEventBus(Object subscribe) {
		if (!isEventBusRegistered(subscribe)) {
			EventBus.getDefault().register(subscribe);
		}
	}

	/**
	 * 是否订阅EventBus
	 *
	 * @param subscribe 订阅
	 * @return boolean
	 */
	protected boolean isEventBusRegistered(Object subscribe) {
		return EventBus.getDefault().isRegistered(subscribe);
	}

	/**
	 * 取消订阅EventBus
	 *
	 * @param subscribe
	 */
	protected void unregisterEventBus(Object subscribe) {
		if (isEventBusRegistered(subscribe)) {
			EventBus.getDefault().unregister(subscribe);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (eventTag != null && isEventBusRegistered(eventTag)) {
			unregisterEventBus(eventTag);
		}
		if (valueAnimator != null) {
			valueAnimator.cancel();
			valueAnimator = null;
		}
		soundMap.clear();
		soundPool.release();
		soundPool = null;
	}


}
