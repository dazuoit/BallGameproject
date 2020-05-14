package com.zuo.ballgame.ballgame.activity;

import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zuo.ballgame.ballgame.GameEventMsg;
import com.zuo.ballgame.ballgame.R;
import com.zuo.ballgame.ballgame.level.GameManager;
import com.zuo.ballgame.ballgame.level.LevelManager;
import com.zuo.ballgame.ballgame.options.BallGameOptions;
import com.zuo.ballgame.ballgame.utils.GameUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author zuo
 * @filename: LantingBallActivity
 * @date: 2020/5/8
 * @description: 游戏;逻辑抽取,与view分开
 * @version: 版本号
 */
public class LantingBallActivity extends ViewBallGameActivity {


	float strength = BallGameOptions.MIN_STRENGTH;
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.rl_lauch_group) {
			if (GameManager.getInstance().getDownTimer().isPause || GameManager.getInstance().getDownTimer().remainTime <= 0) {
				return;
			}
			if (isBallAnmi) {
				return;
			}
			if (left_ball == 0) {
				return;
			}
			if (isGameFinish) {
				return;
			}
			//发射求
			playSound(clickSoundId, false);
			lanchBall();
		} else if (id == R.id.iv_music) {
			//音乐点击
			soundClick();
		} else if (id == R.id.iv_stop) {
			//游戏暂停
			if (isBallAnmi) {
				return;
			}
			if (left_ball == 0) {
				return;
			}
			if (isGameFinish) {
				return;
			}
			if (!GameManager.getInstance().getDownTimer().isPause) {
				stopGame();
			}
			showPopAnim(popStop);
		} else if (id == R.id.iv_over) {
			//退出弹窗
			onBackPressed();
		} else if (id == R.id.iv_help) {
			//帮助菜单
			showPopAnim(popHelp);
		} else if (id == R.id.pop_stop) {
			//暂停弹窗
			if (GameManager.getInstance().getDownTimer().isPause) {
				resumeGame();
			}
			hidePop(popStop);
		} else if (id == R.id.pop_level_success) {
			//成功过关
			hidePop(popLevelSuccess);
			if (BallGameOptions.Level > BallGameOptions.MAX_LEVEL){
				super.onBackPressed();
			}
			startLevel();
		} else if (id == R.id.pop_game_over) {
			//时间到
			hidePop(popGameOver);
			super.onBackPressed();
		} else if (id == R.id.iv_pop_exit_confirm) {
			//确认退出
			hidePop(popExit);
			super.onBackPressed();
		} else if (id == R.id.iv_pop_exit_cancle) {
			//取消退出
			if (popStop.getVisibility() == View.GONE && !isGameFinish) {
				resumeGame();
			}
			hidePop(popExit);
		} else if (id == R.id.pop_level_fail) {
			//过关失败
			hidePop(popLevelFail);
			startLevel();
		}

	}


	//开始/重新开始/下一关
	@Override
	protected void startLevel() {
		if (GameManager.getInstance().getDownTimer().isPause) {
			GameManager.getInstance().getDownTimer().resume();
		}
		bombList.clear();
		isGameFinish = false;
		initTitle();
		BallGameOptions.Stay_Time = LevelManager.STAY_TIME[BallGameOptions.Level];
		cpGame.setTotal(BallGameOptions.Stay_Time);
		mAdapter.setNewData(LevelManager.levelBalls(BallGameOptions.Level));
		mLineView.setVisibility(View.VISIBLE);
		mLineView.setStrengthV(BallGameOptions.MIN_STRENGTH);
		mIvStop.setImageResource(R.drawable.iv_stop_game);

	}


	/**
	 * 顺利通关
	 */
	@Override
	public void successLevel() {
		GameManager.getInstance().getDownTimer().pause();
		isGameFinish = true;

		release();
		//播放声音,动画
		if (BallGameOptions.Level == BallGameOptions.MAX_LEVEL) {
			//通过全关
			//TODO: .....
			showPopAnim(popGameOver);
			return;
		}
		BallGameOptions.Level ++;
		showPopAnim(popLevelSuccess);
	}



	/**
	 * 音乐操作
	 */
	public void soundClick() {
		isOpenSound = !isOpenSound;
		if (!isOpenSound) {
			mIvMusic.setImageResource(R.drawable.iv_close_music);
			stopSound(soundMap.get(backGroudSoundId));
			ToastUtils.showShort("音乐关闭");
		} else {
			mIvMusic.setImageResource(R.drawable.iv_open_music);
			resumeSound(soundMap.get(backGroudSoundId));
			ToastUtils.showShort("音乐开启");
		}
	}



	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventBall(GameEventMsg eventMsg) {
		if (eventMsg == null) {
			return;
		}
		if (BallGameOptions.BOMB_TAG.equals(eventMsg.getTag())) {
			//发生碰撞
			eventGetBombBall(eventMsg);
			/**
			 * 球打完了
			 */
			if (left_ball == 0 && bombList.size() != 0) {
				failedGame();
			}
		} else if (BallGameOptions.GAME_TIME_OVER.equals(eventMsg.getTag())) {
			//游戏结束
			finishGameTimeOver();
		} else if (BallGameOptions.GAME_TIME_LEFT.equals(eventMsg.getTag())) {
			//时间变化
			long left_time = (long) eventMsg.getData();
			mTvLeftTime.setText(GameUtils.getTimeString((int) (left_time / 1000)));

			/*setStrength(strength);
			strength++;
			if (strength > BallGameOptions.MAX_STRENGTH) {
				strength = BallGameOptions.MIN_STRENGTH;
			}*/
		}else if (BallGameOptions.GAME_TIME_LEFT.equals(eventMsg.getTag())){
			if (GameManager.getInstance().getDownTimer().isPause || GameManager.getInstance().getDownTimer().remainTime <= 0) {
				return;
			}
			if (left_ball == 0) {
				return;
			}
			if (isGameFinish) {
				return;
			}
			float strength_s =  (float) eventMsg.getData();
			setStrength(strength_s);
		}
	}

	/**
	 * 设置肌力值
	 *
	 * @param mStrength
	 */
	public void setStrength(float mStrength) {
		LogUtils.w("setStrength", mStrength + "");
		if (GameManager.getInstance().getDownTimer().isPause || GameManager.getInstance().getDownTimer().remainTime <= 0) {
			return;
		}
		if (isBallAnmi) {
			return;
		}
		if (left_ball == 0) {
			return;
		}
		if (isGameFinish) {
			return;
		}
		mTvCurrentNum.setText("实时肌力值: " + mStrength);
		mLineView.setStrengthV(mStrength);
		if (Math.abs(mStrength - BallGameOptions.GOAL_STRENGTH) <= LevelManager.CHECK_UV[BallGameOptions.Level - 1]) {
			collect++;
			cpGame.setProcess(collect);
			mTvCpSecond.setText(cpGame.getmProcess() == 0 ? "1" : cpGame.getmProcess() + 1 + "");
			if (collect >= BallGameOptions.COLLECT_ONE_SECOND * BallGameOptions.Stay_Time) {
				setCircleView();
				playSound(clickSoundId, false);
				lanchBall();
			}
		} else {
			setCircleView();
		}
	}


	//发射球
	public void lanchBall() {
		if (isGameFinish) {
			return;
		}
		if (isBallAnmi) {
			return;
		}
		isBallAnmi = true;
		startLanuch(mIvXiaomai);
		mLineView.startLauch();
		mIvLauchBall.setVisibility(View.INVISIBLE);
		left_ball--;
		mTvLeftBallNum.setText(left_ball + "");
	}


	/**
	 * 暂停
	 */
	public void stopGame() {
		stopSound(soundMap.get(backGroudSoundId));
		setCircleView();
		GameManager.getInstance().getDownTimer().pause();
		mLineView.setVisibility(View.INVISIBLE);
	}

	/**
	 * 暂停后开始
	 */
	public void resumeGame() {
		resumeSound(soundMap.get(backGroudSoundId));
		GameManager.getInstance().getDownTimer().resume();
		mLineView.setVisibility(View.VISIBLE);
	}

	/**
	 * 游戏结束,时间到
	 */
	public void finishGameTimeOver() {
		isGameFinish = true;
		mTvLeftTime.setText("00:00");
		setCircleView();
		mLineView.setVisibility(View.INVISIBLE);
		showPopAnim(popGameOver);
	}

	/**
	 * 15个球打完,没有通关
	 */
	private void failedGame() {
		stopGame();
		isGameFinish = true;
		setCircleView();
		mLineView.setVisibility(View.INVISIBLE);
		showPopAnim(popLevelFail);
	}

	@Override
	public void onBackPressed() {
		stopGame();
		showPopAnim(popExit);
	}
}
