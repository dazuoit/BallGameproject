package com.zuo.ballgame.ballgame.level;


/**
 * @author zuo
 * @filename: BallLevel_1
 * @date: 2020/5/7
 * @description: 等级基类
 * @version: 版本号
 */

public interface BaseLevel {

	/**
	 * 球的色号  0-6
	 * @return
	 */
	int[] level_balls() ;

	/**
	 * 0-99
	 */
	 int[] level_bombs();
	/**
	 * 0-6
	 */
	int[] level_bombs_image();


	/**
	 * 必须一一对等,否则直接崩溃
	 * @return
	 */
	int[][] levelBind();

}