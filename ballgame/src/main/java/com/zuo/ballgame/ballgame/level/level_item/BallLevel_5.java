package com.zuo.ballgame.ballgame.level.level_item;

import com.zuo.ballgame.ballgame.level.BaseLevel;
import com.zuo.ballgame.ballgame.level.LevelManager;

/**
 * @author zuo
 * @filename: BallLevel_1
 * @date: 2020/5/7
 * @description: 等级1
 * @version: 版本号
 */
public class BallLevel_5 implements BaseLevel {
	int level;

	public BallLevel_5(int level) {
		this.level = level;
	}


	@Override
	public int[] level_balls() {
		return new int[]{
				1, 1, 5, 4, 6, 1, 3, 3, 3, 3, 1, 5, 1, 6, 1, 6, 0, 0, 0, 0,
				1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 2, 2, 4, 4, 4,
				1, 5, 5, 4, 1, 1, 3, 3, 3, 3, 1, 5, 1, 6, 1, 6, 0, 0, 0, 0,
				1, 5, 1, 6, 1, 1, 0, 3, 3, 3, 3, 3, 3, 3, 3, 5, 5, 5, 4, 4,
				0, 0, 0, 1, 1, 1, 5, 4, 3, 0, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4
		};

	}

	@Override
	public int[][] levelBind() {
		return new int[][]{
				{
						46,47,48,49,67,78,69,70,71,72,73,74, 88
				},

				{
						75, 76, 77, 90, 92, 93, 94, 95
				}
		};
	}

	@Override
	public int[] level_bombs() {
		return LevelManager.getBombPosition(level);
	}

	@Override
	public int[] level_bombs_image() {
		return new int[]{
				3, 1
		};
	}


}
