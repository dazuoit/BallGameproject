package com.zuo.ballgame.ballgame.options;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * @author zuo
 * @filename: BallGameOptions
 * @date: 2020/4/30
 * @description: 描述
 * @version: 版本号
 */
public class BallGameOptions {
	public static int Level = 1;//等级数
	public static float MAX_STRENGTH = 80;//最大肌力值  大于10/3最小值
	public static float MIN_STRENGTH = 20;//最小肌力值
	public static float GOAL_STRENGTH = 0;//目标值
	public static int Stay_Time = 2;//保持时间(s)

	public static int Total_Time = 1000 * 300;//游戏总时长
	public static int IntervalTime = 250;//游戏时间间隔返回时长


	public static final int COLLECT_ONE_SECOND = 4;//美秒采集的数值
	public static final int MAX_BALL_NUM = 15;//每关最大发射求的数量
	public static final int MAX_LEVEL = 15;//最大关卡数
	public static final int ROW_NUM = 20;//列数
	public static final int SPAN_NUM = 5;//行数
	public static final int ball_FLOW_Duration = 4000;//下落时间
	public static final int ball_FLOW_ADD_RANDOM = 2000;//下落随机时间
	public static final int BALL_LAUCH_RotateAnimation = 720;//发射旋转角度
	public static final int BALL_LAUCH_Duration = 1000;//发射时间
	public static final int LINE_TIME = 200;//线刷新贞率
	public static final int FLOW_TIME = 40;//surfaceview刷新贞率
	public static final int FLOW_DELAY = 1000;//下落延时
	public static final int XIAOMAI_Duration = 300;//小麦抛弃动画时长
	public static final int BALL_TRANS_Duration = 1000;//小球动画时长
	public static final int BALL_POSITION_CHECK = 1;//视觉误差调整
	public static final int BALL_Progress_Stoke = 4;//圆环宽度(dp)
	public static final String BOMB_TAG = "EVENT_TAG_BALL_ITEM";

	public static final String GAME_TIME_OVER = "GAME_TIME_OVER";//游戏时间到
	public static final String GAME_TIME_LEFT = "GAME_TIME_LEFT";//游戏剩余时间
	public static final String GAME_SREGTH = "GAME_SREGTH";//肌电值传入

	public static final float BALL_LENGTH = (ScreenUtils.getAppScreenWidth() / 2) / BallGameOptions.ROW_NUM;//小球半径

	public static final float BALL_LENGTH_BOMB = (int) ((ScreenUtils.getAppScreenWidth() / 2) / BallGameOptions.ROW_NUM * 1.5);//大球半径

	//是否需要占格
	public static boolean isNeed3(int position) {
		return position == BallGameOptions.ROW_NUM || position == BallGameOptions.ROW_NUM * 3;
	}

	/**
	 * 显示区域高度
	 *
	 * @return
	 */
	public static int ballAreaShowSize() {
		return (int) (BallGameOptions.BALL_LENGTH * BallGameOptions.SPAN_NUM - BallGameOptions.BALL_LENGTH * (BallGameOptions.SPAN_NUM - 1) / 8);
	}
}
