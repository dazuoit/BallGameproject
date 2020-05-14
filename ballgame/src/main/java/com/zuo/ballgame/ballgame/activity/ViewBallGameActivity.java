package com.zuo.ballgame.ballgame.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zuo.ballgame.ballgame.BallsAdapter;
import com.zuo.ballgame.ballgame.GameEventMsg;
import com.zuo.ballgame.ballgame.R;
import com.zuo.ballgame.ballgame.level.GameManager;
import com.zuo.ballgame.ballgame.level.LevelManager;
import com.zuo.ballgame.ballgame.options.BallGameOptions;
import com.zuo.ballgame.ballgame.thing.BallSpirit;
import com.zuo.ballgame.ballgame.thing.MyBallFlowBean;
import com.zuo.ballgame.ballgame.utils.GameUtils;
import com.zuo.ballgame.ballgame.view.CircleProgressView;
import com.zuo.ballgame.ballgame.view.LineView;
import com.zuo.ballgame.ballgame.view.MineBallFlowView;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author zuo
 * @filename: GameActivity
 * @date: 2020/5/6
 * @description: 描述
 * @version: 版本号
 */
public abstract class ViewBallGameActivity extends BaseGameActivity implements View.OnClickListener {
	protected BaseQuickAdapter mAdapter;
	protected RecyclerView mRecyclerView;
	protected LineView mLineView;
	protected RelativeLayout mRlLineGroup;
	protected ImageView mViewGlass;
	protected ImageView mIvLauchBall;
	protected RelativeLayout mRlLauchGroup;
	protected RelativeLayout mRlRoot;
	protected MineBallFlowView mBldown;
	protected ImageView mIvXiaomai;
	protected ImageView mIvGlassBomb;
	protected TextView mTvLeftTime;
	protected TextView mTvCurrentLevel;
	protected TextView mTvCurrentNum;
	protected TextView mTvGoalNum;
	protected TextView mTvScore;
	protected ImageView mIvMusic;
	protected ImageView mIvStop;
	protected ImageView mIvOver;
	protected ImageView mIvHelp;
	protected CircleProgressView cpGame;
	protected TextView mTvLeftBallNum;
	protected TextView mTvCpSecond;
	protected View popGroup;
	protected View popHelp;
	protected View popLevelFail;
	protected View popLevelSuccess;
	protected View popGameOver;
	protected View popExit;
	protected ImageView popStop;
	protected View vPopExitCancle;
	protected View vPopExitConfirm;
	// 炸弹弹珠
	protected CopyOnWriteArrayList<Integer> bombList = new CopyOnWriteArrayList<>();
	protected CopyOnWriteArrayList<View> popList = new CopyOnWriteArrayList<>();
	Random random = new Random();
	public boolean isBallAnmi = false; //是否正在动画中
	public boolean isGameFinish = false; //是否此关结束
	protected int collect = 0;//采集数量
	protected int left_ball = BallGameOptions.MAX_BALL_NUM;//最大球数
	protected int score = 0;//得分
	protected int total_score =  0 ;
	private TextView tvScoreLevelFail;
	private TextView tvScoreLevelSuccess;
	private TextView tvScoreGameOver;
	private RelativeLayout rl_game_bg;


	@Override
	public int getLayoutId() {
		return R.layout.activity_ball_game;
	}

	@Override
	protected void initView() {
		super.initView();
		setEventTag(this);
		rl_game_bg = findViewById(R.id.rl_game_bg);
		cpGame = findViewById(R.id.cp_game);
		mRecyclerView = findViewById(R.id.game_recyclerview);
		mLineView = findViewById(R.id.line_view);
		mRlLineGroup = findViewById(R.id.rl_line_group);
		mViewGlass = findViewById(R.id.rl_glass);
		mIvLauchBall = findViewById(R.id.iv_lauch_ball);
		mRlLauchGroup = findViewById(R.id.rl_lauch_group);
		mRlRoot = findViewById(R.id.rl_root);
		mBldown = findViewById(R.id.bl_down);
		mTvCpSecond = findViewById(R.id.tv_cp_second);
		mIvXiaomai = findViewById(R.id.iv_xiaomai);
		mIvGlassBomb = findViewById(R.id.iv_glass_bomb);
		mTvLeftTime = findViewById(R.id.tv_left_time);
		mTvCurrentLevel = findViewById(R.id.tv_current_level);
		mTvCurrentNum = findViewById(R.id.tv_current_num);
		mTvGoalNum = findViewById(R.id.tv_goal_num);
		mTvScore = findViewById(R.id.tv_score);
		mTvLeftBallNum = findViewById(R.id.tv_left_ball_num);
		mIvMusic = findViewById(R.id.iv_music);
		mIvMusic.setOnClickListener(this);
		mIvStop = findViewById(R.id.iv_stop);
		mIvStop.setOnClickListener(this);
		mIvOver = findViewById(R.id.iv_over);
		mIvOver.setOnClickListener(this);
		mIvHelp = findViewById(R.id.iv_help);
		mIvHelp.setOnClickListener(this);
		popGroup = findViewById(R.id.pop_group);


		popHelp = findViewById(R.id.pop_help);
		popStop = findViewById(R.id.pop_stop);
		popLevelFail = findViewById(R.id.pop_level_fail);
		popLevelSuccess = findViewById(R.id.pop_level_success);
		popGameOver = findViewById(R.id.pop_game_over);
		popExit = findViewById(R.id.pop_exit);
		vPopExitCancle = findViewById(R.id.iv_pop_exit_cancle);
		vPopExitConfirm = findViewById(R.id.iv_pop_exit_confirm);

		tvScoreLevelFail = findViewById(R.id.tv_score_level_fail);
		tvScoreLevelSuccess = findViewById(R.id.tv_score_level_success);
		tvScoreGameOver = findViewById(R.id.tv_score_game_over);

		initLayout();

		popList.add(popHelp);
		popList.add(popStop);
		popList.add(popLevelFail);
		popList.add(popLevelSuccess);
		popList.add(popGameOver);
		popList.add(popExit);
	}

	@Override
	protected void initData() {
		startLevel();
	}

	@Override
	protected void initListener() {
		mRlLauchGroup.setOnClickListener(this);
		popHelp.setOnClickListener(this);
		popStop.setOnClickListener(this);
		popLevelFail.setOnClickListener(this);
		popLevelSuccess.setOnClickListener(this);
		popGameOver.setOnClickListener(this);
		popExit.setOnClickListener(this);
		vPopExitCancle.setOnClickListener(this);
		vPopExitConfirm.setOnClickListener(this);
	}

	protected void initTitle() {
		mTvCurrentLevel.setText("第" + BallGameOptions.Level + "关");
		mTvLeftTime.setText(GameUtils.getTimeString((int) (GameManager.getInstance().getDownTimer().remainTime / 1000)));
		mTvCurrentNum.setText("实时肌力值: " + 0);
		score = 0;
		setScoreView();
		left_ball = BallGameOptions.MAX_BALL_NUM;
		mTvLeftBallNum.setText(BallGameOptions.MAX_BALL_NUM + "");
		mTvCpSecond.setText("");
		rl_game_bg.setBackgroundResource(LevelManager.backGroundIds[(BallGameOptions.Level-1)/3]);
	}

	public void setScoreView() {
		mTvScore.setText(score + "");
		tvScoreLevelFail.setText(score + "");
		tvScoreLevelSuccess.setText(score + "");
		tvScoreGameOver.setText(total_score +"");
	}

	private void initLayout() {
		LogUtils.w("reference_2", "initLayout");
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mAdapter = new BallsAdapter(this);
		if (mAdapter != null) {
			mAdapter.bindToRecyclerView(mRecyclerView);
		}
		initGridLayoutManager(mRecyclerView);
		RelativeLayout.LayoutParams mRecyclerViewLayoutParams = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
		mRecyclerViewLayoutParams.height = BallGameOptions.ballAreaShowSize();
		mRecyclerView.setLayoutParams(mRecyclerViewLayoutParams);

		//线区域的大小
		RelativeLayout.LayoutParams mRlLineGroupLayoutParams = (RelativeLayout.LayoutParams) mRlLineGroup.getLayoutParams();
		mRlLineGroupLayoutParams.setMargins(0, (int) (BallGameOptions.ballAreaShowSize() + SizeUtils.dp2px(50 - 3)), 0, SizeUtils.dp2px(12 + 25));
		mRlLineGroupLayoutParams.width = (int) (BallGameOptions.BALL_LENGTH * (BallGameOptions.ROW_NUM + 0.5));
		mRlLineGroup.setLayoutParams(mRlLineGroupLayoutParams);


		//发射球的大小
		RelativeLayout.LayoutParams mIvLauchBallLayoutParams = (RelativeLayout.LayoutParams) mIvLauchBall.getLayoutParams();
		mIvLauchBallLayoutParams.width = (int) BallGameOptions.BALL_LENGTH_BOMB;
		mIvLauchBallLayoutParams.height = (int) BallGameOptions.BALL_LENGTH_BOMB;
		mIvLauchBallLayoutParams.bottomMargin = (int) (SizeUtils.dp2px(12 + 25) - (BallGameOptions.BALL_LENGTH_BOMB / 2));
		mIvLauchBall.setLayoutParams(mIvLauchBallLayoutParams);

		//进度框的位置
		RelativeLayout.LayoutParams cpGameLayoutParams = (RelativeLayout.LayoutParams) cpGame.getLayoutParams();
		cpGameLayoutParams.width = (int) (BallGameOptions.BALL_LENGTH_BOMB + SizeUtils.dp2px(BallGameOptions.BALL_Progress_Stoke * 2 + 2));
		cpGameLayoutParams.height = (int) (BallGameOptions.BALL_LENGTH_BOMB + SizeUtils.dp2px(BallGameOptions.BALL_Progress_Stoke * 2 + 2));
		cpGameLayoutParams.bottomMargin = (int) (SizeUtils.dp2px(12 + 25 - (BallGameOptions.BALL_Progress_Stoke + 1)) - (BallGameOptions.BALL_LENGTH_BOMB / 2));
		cpGame.setLayoutParams(cpGameLayoutParams);


		RelativeLayout.LayoutParams mTvCpSecondLayoutParams = (RelativeLayout.LayoutParams) mTvCpSecond.getLayoutParams();
		mTvCpSecondLayoutParams.width = (int) BallGameOptions.BALL_LENGTH_BOMB;
		mTvCpSecondLayoutParams.height = (int) BallGameOptions.BALL_LENGTH_BOMB;
		mTvCpSecondLayoutParams.bottomMargin = (int) (SizeUtils.dp2px(12 + 25) - (BallGameOptions.BALL_LENGTH_BOMB / 2));
		mTvCpSecond.setLayoutParams(mTvCpSecondLayoutParams);

		//玻璃瓶的大小
		RelativeLayout.LayoutParams mRlGlassLayoutParams = (RelativeLayout.LayoutParams) mViewGlass.getLayoutParams();
		mRlGlassLayoutParams.width = (int) (BallGameOptions.BALL_LENGTH_BOMB * 2);
		mRlGlassLayoutParams.height = (int) (BallGameOptions.BALL_LENGTH_BOMB * 2);
		mViewGlass.setLayoutParams(mRlGlassLayoutParams);
		//玻璃瓶中球大小
		mViewGlass.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mViewGlass.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				int[] loaction = GameUtils.getLocation(mViewGlass);
				RelativeLayout.LayoutParams mIvGlassBombLayoutParams = (RelativeLayout.LayoutParams) mIvGlassBomb.getLayoutParams();
				mIvGlassBombLayoutParams.width = (int) BallGameOptions.BALL_LENGTH_BOMB;
				mIvGlassBombLayoutParams.height = (int) BallGameOptions.BALL_LENGTH_BOMB;
				mIvGlassBombLayoutParams.topMargin = (int) (BallGameOptions.BALL_LENGTH_BOMB / 2 + loaction[1] + BallGameOptions.BALL_LENGTH_BOMB / 10);
				mIvGlassBombLayoutParams.leftMargin = (int) (BallGameOptions.BALL_LENGTH_BOMB / 2 + loaction[0] - BallGameOptions.BALL_LENGTH_BOMB / 10);
				mIvGlassBomb.setLayoutParams(mIvGlassBombLayoutParams);
			}
		});


		mLineView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mLineView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				mLineView.setLayout(mLineView.getMeasuredWidth(), mLineView.getMeasuredHeight(), mIvLauchBall);
			}
		});

	}


	/**
	 * 增加炸弹球
	 *
	 * @param ballSpirit
	 * @param layoutPosition
	 */
	public void addBombView(BallSpirit ballSpirit, int layoutPosition) {
		ImageView bomb = new ImageView(this);
		bomb.setVisibility(View.GONE);
		bomb.setScaleType(ImageView.ScaleType.FIT_XY);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.width = (int) BallGameOptions.BALL_LENGTH_BOMB;
		lp.height = (int) BallGameOptions.BALL_LENGTH_BOMB;
		lp.topMargin = (int) (ballSpirit.getLoacationY() - (BallGameOptions.BALL_LENGTH / 4));
		lp.leftMargin = (int) (ballSpirit.getLoacationX() - (BallGameOptions.BALL_LENGTH / 4));
		bomb.setLayoutParams(lp);
		bomb.setImageResource(ballSpirit.getmBombViewid());
		ballSpirit.setBomb_view(bomb);
		mAdapter.notifyItemChanged(layoutPosition);
		mRlRoot.addView(ballSpirit.getBomb_view());
		bombList.add(ballSpirit.getId());
		if (bombList.size() == LevelManager.MVC_LIMIT[BallGameOptions.Level - 1].length) {
			Collections.shuffle(bombList);
			BallSpirit ball = (BallSpirit) mAdapter.getData().get(bombList.get(0));
			ball.getBomb_view().setVisibility(View.VISIBLE);
			setGlassImage();
			BallGameOptions.GOAL_STRENGTH = (BallGameOptions.MAX_STRENGTH - BallGameOptions.MIN_STRENGTH) * ((float) (bombList.get(0) - 79) / 20) + BallGameOptions.MIN_STRENGTH;
			mTvGoalNum.setText("目标肌力值: " + (BallGameOptions.GOAL_STRENGTH));

		}
	}


	/**
	 * 设置水杯
	 */
	public void setGlassImage() {
		if (bombList.size() > 0 && left_ball > 1) {
			mIvGlassBomb.setVisibility(View.VISIBLE);
			BallSpirit ball = (BallSpirit) mAdapter.getData().get(bombList.get(0));
			mIvGlassBomb.setImageResource(ball.getmBombViewid());
		} else {
			mIvGlassBomb.setVisibility(View.INVISIBLE);
		}
		if (bombList.size() > 0 && left_ball > 0) {
			mIvLauchBall.setVisibility(View.VISIBLE);
			BallSpirit ball = (BallSpirit) mAdapter.getData().get(bombList.get(0));
			mIvLauchBall.setImageResource(ball.getmBombViewid());
		} else {
			mIvLauchBall.clearAnimation();
			mIvLauchBall.setVisibility(View.INVISIBLE);
		}
	}


	/**
	 * 球下降
	 *
	 * @param position
	 * @param ballSpirit
	 */
	public void flowBall(int position, BallSpirit ballSpirit) {
		if (ballSpirit.isShow()) {
			ballSpirit.setShow(false);
			int[] location = GameUtils.getLocation(mAdapter.getViewByPosition(position, R.id.iv_ball));
			ballSpirit.setLoacationX(location[0]);
			ballSpirit.setLoacationY(location[1]);
			mBldown.addBall(new MyBallFlowBean(ballSpirit,this));
		}
	}

	/**
	 * 动画
	 *
	 * @param position
	 * @param ballSpirit
	 */
	public void bombAnim(int position, BallSpirit ballSpirit) {
		for (int i = 0; i < bombList.size(); i++) {
			if (ballSpirit.getId() == bombList.get(i)) {
				bombList.remove(i);
			}
		}

		if (bombList.size() > 0) {
			BallSpirit ball = (BallSpirit) mAdapter.getData().get(bombList.get(0));
			ball.getBomb_view().setVisibility(View.VISIBLE);
			BallGameOptions.GOAL_STRENGTH = (BallGameOptions.MAX_STRENGTH - BallGameOptions.MIN_STRENGTH) * ((float) (bombList.get(0) - 79) / 20) + BallGameOptions.MIN_STRENGTH;
			mTvGoalNum.setText("目标肌力值: " + (BallGameOptions.GOAL_STRENGTH));
		}


		ballSpirit.setShow(false);
		ballSpirit.getBomb_view().setVisibility(View.GONE);
		boolean isAllBomb = true;
		for (int i = 0; i < mAdapter.getData().size(); i++) {
			BallSpirit ball = (BallSpirit) mAdapter.getData().get(i);
			if (ball.isShow() && ball.isBomb()) {
				isAllBomb = false;
			}
			if (ball.isShow() && ball.getBindId() == position) {
				ball.setShow(false);
				hideView(mAdapter.getViewByPosition(i, R.id.iv_ball));
			}
		}
		if (isAllBomb) {
			mLineView.setVisibility(View.INVISIBLE);
			handler.postDelayed(() -> {
				for (int i = 0; i < mAdapter.getData().size(); i++) {
					BallSpirit ball = (BallSpirit) mAdapter.getData().get(i);
					flowBall(i, ball);
				}
				mAdapter.notifyDataSetChanged();
			}, BallGameOptions.FLOW_DELAY);

			handler.postDelayed(() -> {
				successLevel();
			}, BallGameOptions.FLOW_DELAY + BallGameOptions.ball_FLOW_Duration + BallGameOptions.ball_FLOW_ADD_RANDOM);
		}
	}

	protected abstract void startLevel();

	protected abstract void successLevel();

	public void hidePop(final View view) {
		long startDelay = 300L;
		ValueAnimator valueAnimator = new ValueAnimator();
		valueAnimator.setDuration(startDelay + 500).setFloatValues(0f, 1f);
		// 使View颤抖
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			Random random = new Random();

			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				view.setTranslationX((random.nextFloat() - 0.5F) * view.getWidth() * 0.01F);
				view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.01f);
			}
		});
		valueAnimator.start();
		// 将View 缩放至0、透明至0
		view.animate().setListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				view.setVisibility(View.GONE);
				popGroupVisbale();
				valueAnimator.cancel();
				valueAnimator.removeAllListeners();
				view.clearAnimation();
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		view.animate().setDuration(260).setStartDelay(startDelay).alpha(0).start();
	}

	/**
	 * 得分计算
	 * @param position
	 * @param isBomb
	 */
	public void setScore(int position, boolean isBomb) {
		if (isBomb) {
			int bindScore = 0;
			for (int i = 0; i < mAdapter.getData().size(); i++) {
				BallSpirit ball = (BallSpirit) mAdapter.getData().get(i);
				if (ball.getBindId() == position) {
					bindScore = bindScore + 100;
				}
			}
			score = bindScore + 10 + score;
			total_score = total_score + bindScore + 10 ;
		} else {
			score = score - 50;
			total_score = total_score - 50;
		}
		setScoreView();
	}

	public void showPopAnim(View view) {
		popGroup.setVisibility(View.VISIBLE);
		view.setVisibility(View.VISIBLE);
		long startDelay = 300L;
		ValueAnimator valueAnimator = new ValueAnimator();
		valueAnimator.setDuration(startDelay + 500).setFloatValues(0f, 1f);
		// 使View颤抖
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				view.setTranslationX((random.nextFloat() - 0.5F) * view.getWidth() * 0.01F);
				view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.01f);
			}
		});
		valueAnimator.start();
		// 将View 缩放至0、透明至0

		view.animate().setListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				valueAnimator.cancel();
				valueAnimator.removeAllListeners();
				view.clearAnimation();
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		view.animate().setDuration(260).setStartDelay(startDelay).alpha(1).start();
	}


	public void popGroupVisbale() {
		boolean isVisable = false;
		for (int i = 0; i < popList.size(); i++) {
			if (popList.get(i).getVisibility() == View.VISIBLE) {
				isVisable = true;
			}
		}
		if (!isVisable) {
			popGroup.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取碰撞的小球
	 *
	 * @param eventMsg
	 */
	protected void eventGetBombBall(GameEventMsg eventMsg) {
		float x = (float) eventMsg.getData();
		int position = (int) (x / (BallGameOptions.BALL_LENGTH)) + BallGameOptions.ROW_NUM * (BallGameOptions.SPAN_NUM - 1);
		//偏移量更正 ,炸弹球大一点,所以位置偏误更正

		position = getPositionCheck(position, x);

		if (position == -1) {
			return;
		}

		BallSpirit ballSpirit = (BallSpirit) mAdapter.getData().get(position);
		if (ballSpirit.isShow()) {
			if (ballSpirit.isBomb() && ballSpirit.bombViewIsShow()) {
				ballSpirit.setShow(false);
				mAdapter.notifyItemChanged(position);

				ballSpirit.getBomb_view().setVisibility(View.GONE);
				bombAnim(position, ballSpirit);
				setScore(position, true);

			} else {
				setScore(position, false);
				/*flowBall(position, ballSpirit);
				mAdapter.notifyItemChanged(position);*/
			}

		}
		if (left_ball != 0) {
			AnimationSet animationSet = viewTransAnim(mIvGlassBomb, mIvLauchBall);
			animationSet.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					setGlassImage();
					isBallAnmi = false;
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
		} else {
			setGlassImage();
		}
	}

	/**
	 * 偏移量矫正
	 *
	 * @param position
	 * @param x
	 * @return
	 */
	public int getPositionCheck(int position, float x) {
		if (mAdapter.getData().size() < position + 1) {
			return -1;
		}
		BallSpirit ballSpirit = (BallSpirit) mAdapter.getData().get(position);
		if (ballSpirit.isBomb() && ballSpirit.bombViewIsShow()) {
			return position;
		}

		if (bombList.size() > 0) {

			if (bombList.get(0) + 1 == position) {
				//炸弹球在左侧
				int dx = (int) (x % BallGameOptions.BALL_LENGTH);
				if (dx <= ((BallGameOptions.BALL_LENGTH_BOMB - BallGameOptions.BALL_LENGTH) / 2) + SizeUtils.dp2px(BallGameOptions.BALL_POSITION_CHECK)) {
					return position - 1;
				}
			}

			if (bombList.get(0) - 1 == position) {
				//炸弹球在右侧
				int dx = (int) (x % BallGameOptions.BALL_LENGTH);
				if (dx >= ((BallGameOptions.BALL_LENGTH_BOMB - BallGameOptions.BALL_LENGTH) / 2) - SizeUtils.dp2px(BallGameOptions.BALL_POSITION_CHECK)) {
					return position + 1;
				}
			}
		}
		return position;
	}

	/**
	 * 进度框设置
	 */
	protected void setCircleView() {
		collect = 0;
		cpGame.setProcess(collect);
		mTvCpSecond.setText("");
	}

	/**
	 * 游戏退出
	 */
	public void destroyGame() {
		release();
		mLineView.stopTimer();
		bombList.clear();
		GameManager.getInstance().release();
	}

	/**
	 * 清屏
	 */
	public void release() {
		mLineView.setVisibility(View.INVISIBLE);
		setCircleView();
		bombList.clear();
		mBldown.relase();
		mAdapter.setNewData(null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyGame();
		popList.clear();
		bombList.clear();
		mLineView.stopTimer();
		mBldown.relase();
	}

}
