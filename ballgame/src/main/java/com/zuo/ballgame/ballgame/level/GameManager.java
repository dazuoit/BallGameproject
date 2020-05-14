package com.zuo.ballgame.ballgame.level;


import com.zuo.ballgame.ballgame.GameEventMsg;
import com.zuo.ballgame.ballgame.options.BallGameOptions;
import com.zuo.ballgame.ballgame.utils.DownTimer;

import org.greenrobot.eventbus.EventBus;

/**
 * @author zuo
 * @filename: GameManager
 * @date: 2020/5/9
 * @description: 游戏全局管理器
 * @version: 版本号
 */


public class GameManager implements DownTimer.TimeListener {


	private DownTimer downTimer;

	private static class GameManagerHolder {
		private static GameManager mGameManager = new GameManager();
	}

	public static GameManager getInstance() {
		return GameManagerHolder.mGameManager;
	}

	public GameManager() {
		init();
	}

	private void init() {
		initTimer();
	}

	public DownTimer getDownTimer() {
		if (downTimer == null) {
			initTimer();
		}
		return downTimer;
	}


	private void initTimer() {
		downTimer = new DownTimer();
		downTimer.start();
		downTimer.setTimerLiener(this);
	}

	@Override
	public void onFinish() {
		EventBus.getDefault().post(new GameEventMsg(BallGameOptions.GAME_TIME_OVER, true));
	}

	@Override
	public void onInterval(long remainTime) {
		EventBus.getDefault().post(new GameEventMsg(BallGameOptions.GAME_TIME_LEFT, remainTime));
	}

	public void release() {
		if (downTimer != null) {
			downTimer.cancel();
			downTimer = null;
		}
	}
}
