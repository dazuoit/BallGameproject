package com.zuo.ballgame.ballgame.thing;

import android.support.annotation.DrawableRes;
import android.view.View;



/**
 * @author zuo
 * @filename: BallSpirit
 * @date: 2020/5/6
 * @description: 球精灵 类
 * @version: 版本号
 */
public class BallSpirit extends Thing {
	private int loacationX;//x
	private int loacationY;//y
	private int bindId = -1;  //小球绑定的id
	private View bomb_view;//增加的view
	private boolean isBomb = false;//是否是炸弹球
	private @DrawableRes
	int mBombViewid ;//大球id
	public BallSpirit(int image, boolean isShow, int id) {
		super(image, isShow, id);
	}

	public int getmBombViewid() {
		return mBombViewid;
	}

	public void setmBombViewid(int mBombViewid) {
		this.mBombViewid = mBombViewid;
	}



	public int getBindId() {
		return bindId;
	}

	public void setBindId(int bindId) {
		this.bindId = bindId;
	}


	public int getLoacationX() {
		return loacationX;
	}

	public void setLoacationX(int loacationX) {
		this.loacationX = loacationX;
	}

	public int getLoacationY() {
		return loacationY;
	}

	public void setLoacationY(int loacationY) {
		this.loacationY = loacationY;
	}

	public boolean isBomb() {
		return isBomb;
	}

	public void setBomb(boolean bomb) {
		isBomb = bomb;
	}

	public View getBomb_view() {
		return bomb_view;
	}

	public void setBomb_view(View bombView) {
		bomb_view = bombView;
	}

	public boolean bombViewIsShow(){
		return bomb_view!= null && bomb_view.getVisibility() == View.VISIBLE;
	}
}
