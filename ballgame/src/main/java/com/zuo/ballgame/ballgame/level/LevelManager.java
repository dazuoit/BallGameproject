package com.zuo.ballgame.ballgame.level;

import android.support.annotation.NonNull;


import com.zuo.ballgame.ballgame.R;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_1;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_10;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_11;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_12;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_13;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_14;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_15;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_2;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_3;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_4;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_5;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_6;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_7;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_8;
import com.zuo.ballgame.ballgame.level.level_item.BallLevel_9;
import com.zuo.ballgame.ballgame.options.BallGameOptions;
import com.zuo.ballgame.ballgame.thing.BallSpirit;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author zuo
 * @filename: LevelManager
 * @date: 2020/5/7
 * @description: 等级管理器
 * @version:
 */
public class LevelManager {
	public static int[] imageIds = {R.drawable.ball_b1, R.drawable.ball_b2, R.drawable.ball_b3, R.drawable.ball_b4, R.drawable.ball_b5, R.drawable.ball_b6, R.drawable.ball_b7};
	public static int[] bombIds = {R.drawable.ball_bomb_b1, R.drawable.ball_bomb_b2, R.drawable.ball_bomb_b3, R.drawable.ball_bomb_b4, R.drawable.ball_bomb_b5, R.drawable.ball_bomb_b6, R.drawable.ball_bomb_b7};
	public static int[] backGroundIds = {R.drawable.bgn1,R.drawable.bgn2,R.drawable.bgn3,R.drawable.bgn4,R.drawable.bgn5};
	private static Random rand = new Random();
	/**
	 * 保持时间,每3关一个难度
	 */
	public static int[] STAY_TIME = {
			2, 2, 2,
			3, 3, 3,
			4, 4, 4,
			5, 5, 5,
			5, 5, 5
	};

	/**
	 * 每关误差值
	 */
	public static int[] CHECK_UV = {
			7,7,7,
			6,6,6,
			5,5,5,
			5,5,5,
			4,4,4

	};

	/**
	 * 范围
	 */
	public static float[][] MVC_LIMIT = {
			{0.3f,0.4f},{0.4f,0.5f},{0.3f,0.4f,0.5f},
			{0.4f,0.5f},{0.5f,0.6f},{0.4f,0.5f,0.6f},
			{0.5f,0.6f},{0.6f,0.7f},{0.5f,0.6f,0.7f},
			{0.6f,0.7f},{0.7f,0.8f},{0.6f,0.7f,0.8f},
			{0.7f,0.8f},{0.8f,0.9f},{0.7f,0.8f,0.9f}
	};

	public static ArrayList<BallSpirit> levelBalls(int level) {
		ArrayList<BallSpirit> balls = new ArrayList<>();
		switch (level) {
			case 1:
				BaseLevel baseBallLevel = new BallLevel_1(level);
				balls = getBallSpirits(level, baseBallLevel, balls);
				break;
			case 2:
				BaseLevel baseBallLeve2 = new BallLevel_2(level);
				balls = getBallSpirits(level, baseBallLeve2, balls);
				break;
			case 3:
				BaseLevel baseBallLeve3 = new BallLevel_3(level);
				balls = getBallSpirits(level, baseBallLeve3, balls);
				break;
			case 4:
				BaseLevel baseBallLeve4 = new BallLevel_4(level);
				balls = getBallSpirits(level, baseBallLeve4, balls);
				break;
			case 5:
				BaseLevel baseBallLeve5 = new BallLevel_5(level);
				balls = getBallSpirits(level, baseBallLeve5, balls);
				break;
			case 6:
				BaseLevel baseBallLeve6 = new BallLevel_6(level);
				balls = getBallSpirits(level, baseBallLeve6, balls);
				break;
			case 7:
				BaseLevel baseBallLeve7 = new BallLevel_7(level);
				balls = getBallSpirits(level, baseBallLeve7, balls);
				break;
			case 8:
				BaseLevel baseBallLeve8 = new BallLevel_8(level);
				balls = getBallSpirits(level, baseBallLeve8, balls);
				break;
			case 9:
				BaseLevel baseBallLeve9 = new BallLevel_9(level);
				balls = getBallSpirits(level, baseBallLeve9, balls);
				break;
			case 10:
				BaseLevel baseBallLeve10 = new BallLevel_10(level);
				balls = getBallSpirits(level, baseBallLeve10, balls);
				break;
			case 11:
				BaseLevel baseBallLeve11 = new BallLevel_11(level);
				balls = getBallSpirits(level, baseBallLeve11, balls);
				break;
			case 12:
				BaseLevel baseBallLeve12 = new BallLevel_12(level);
				balls = getBallSpirits(level, baseBallLeve12, balls);
				break;
			case 13:
				BaseLevel baseBallLeve13 = new BallLevel_13(level);
				balls = getBallSpirits(level, baseBallLeve13, balls);
				break;
			case 14:
				BaseLevel baseBallLeve14 = new BallLevel_14(level);
				balls = getBallSpirits(level, baseBallLeve14, balls);
				break;
			case 15:
				BaseLevel baseBallLeve15 = new BallLevel_15(level);
				balls = getBallSpirits(level, baseBallLeve15, balls);
				break;
			default:
				break;
		}
		return balls;
	}

	@NonNull
	private static ArrayList<BallSpirit> getBallSpirits(int level, BaseLevel baseBallLevel, ArrayList<BallSpirit> balls) {
		setOption(level);
		for (int i = 0; i < baseBallLevel.level_balls().length; i++) {
			BallSpirit ballSpirit = new BallSpirit(imageIds[baseBallLevel.level_balls()[i]], true, i);
			balls.add(ballSpirit);
		}
		shuffle(baseBallLevel.level_bombs(),baseBallLevel.levelBind());
		/**
		 * 加载大球
		 */
		for (int i = 0; i < baseBallLevel.level_bombs().length; i++) {
			BallSpirit ball = balls.get(baseBallLevel.level_bombs()[i]);
			ball.setBomb(true);
			ball.setmBombViewid(bombIds[baseBallLevel.level_bombs_image()[i]]);
		}

		for (int i = 0; i < baseBallLevel.levelBind().length; i++) {
			for (int j = 0; j < baseBallLevel.levelBind()[i].length; j++) {
				BallSpirit ball = balls.get(baseBallLevel.levelBind()[i][j]);
				ball.setBindId(baseBallLevel.level_bombs()[i]);
			}
		}
		return balls;
	}

	//设置参数 等级从1开始
	private static void setOption(int level) {
		BallGameOptions.Level = level;
	}

	public static int[] getBombPosition(int level){
		int[] bombs = new int[ MVC_LIMIT[level-1].length];
		float[] arr = MVC_LIMIT[level-1];
		for (int i = 0; i <arr.length ; i++) {
			bombs[i] = (int) (arr[i]*20) + 79;
		}
		return bombs;
	}

	public static  void swap(int[] a,int[][] levelBind, int i, int j){
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;

		int[] temp2 = levelBind[i];
		levelBind[i] = levelBind[j];
		levelBind[j] = temp2;

	}

	public  static  void shuffle(int[] arr,int[][] levelBind) {
		int length = arr.length;
		for ( int i = length; i > 0; i-- ){
			int randInd = rand.nextInt(i);
			swap(arr, levelBind, randInd, i - 1);
		}
	}

}
