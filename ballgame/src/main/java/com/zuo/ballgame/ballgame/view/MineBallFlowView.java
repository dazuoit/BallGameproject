package com.zuo.ballgame.ballgame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zuo.ballgame.ballgame.options.BallGameOptions;
import com.zuo.ballgame.ballgame.thing.MyBallFlowBean;

import java.util.concurrent.CopyOnWriteArrayList;

public class MineBallFlowView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder surfaceHolder;

	/**
	 * 心的个数
	 */
	private CopyOnWriteArrayList<MyBallFlowBean> ballFlowBeans = new CopyOnWriteArrayList<>();
	private Paint p;
	private DrawThread drawThread;
	boolean isRun = true;

	/**
	 * 负责绘制的工作线程
	 */

	public MineBallFlowView(Context context) {
		this(context, null);
	}

	public MineBallFlowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MineBallFlowView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.setZOrderOnTop(true);
		/**设置画布  背景透明*/
		this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		p = new Paint();
		p.setAntiAlias(true);
	}


	public void addBall(MyBallFlowBean ballFlowBean) {
		ballFlowBeans.add(ballFlowBean);
		start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	Canvas canvas = null;

	class DrawThread extends Thread {

		@Override
		public void run() {
			super.run();
			/**绘制的线程 死循环 不断的跑动*/
			while (isRun) {
				try {
					synchronized (surfaceHolder) {
						canvas = surfaceHolder.lockCanvas();
						/**清除画面*/
						canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
						/**对所有心进行遍历绘制*/
						for (int i = 0; i < ballFlowBeans.size(); i++) {
							if (ballFlowBeans.get(i) != null) {
								ballFlowBeans.get(i).draw(canvas, p);
							}
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
				try {
					/**用于控制绘制帧率*/
					Thread.sleep(BallGameOptions.FLOW_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void start() {
		isRun = true;
		if (drawThread == null) {
			drawThread = new DrawThread();
			drawThread.start();
		}
	}

	public void relase() {
		for (int i = 0; i < ballFlowBeans.size(); i++) {
			if (ballFlowBeans != null) {
				ballFlowBeans.get(i).release();
			}
		}
		ballFlowBeans.clear();
	}
}

