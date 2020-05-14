package com.zuo.ballgame.ballgame.utils;

import android.view.View;

/**
 * @author zuo
 * @filename: GameUtils
 * @date: 2020/5/8
 * @description: 描述
 * @version: 版本号
 */
public class GameUtils {

	public static int[] getLocation(View view) {
		int[] location = new int[2];
		if (view != null) {
			view.getLocationInWindow(location);
		}
		return location;
	}

	/**
	 * 把时间转换为：时分秒格式。
	 *
	 * @param second ：秒，传入单位为秒
	 * @return
	 */
	/**
	 * 把时间转换为：时分秒格式。
	 *
	 * @param time
	 * @return
	 */
	public static String getTimeString(int time) {
		int miao = time % 60;
		int fen = time / 60;
		int hour = 0;
		if (fen >= 60) {
			hour = fen / 60;
			fen = fen % 60;
		}
		String timeString = "";
		String miaoString = "";
		String fenString = "";
		String hourString = "";
		if (miao < 10) {
			miaoString = "0" + miao;
		} else {
			miaoString = miao + "";
		}
		if (fen < 10) {
			fenString = "0" + fen;
		} else {
			fenString = fen + "";
		}
		if (hour < 10) {
			hourString = "0" + hour;
		} else {
			hourString = hour + "";
		}
		if (hour != 0) {
			timeString = hourString + ":" + fenString + ":" + miaoString;
		} else {
			timeString = fenString + ":" + miaoString;
		}
		return timeString;
	}
}



