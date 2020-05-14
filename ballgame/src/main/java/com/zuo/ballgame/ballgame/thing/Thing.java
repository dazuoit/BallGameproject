package com.zuo.ballgame.ballgame.thing;


import android.support.annotation.DrawableRes;

/**
 * @author zuo
 * @filename: Thing
 * @date: 2020/5/6
 * @description: 实体类
 * @version: 版本号
 */
public class Thing {

	public Thing(int image, boolean isShow,int id) {
		this.image = image;
		this.isShow = isShow;
		this.id = id;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean show) {
		isShow = show;
	}

	private @DrawableRes
	int image ;

	private boolean isShow;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int id;


}
