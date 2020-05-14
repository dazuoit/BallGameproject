package com.zuo.ballgame.ballgame;


import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zuo.ballgame.ballgame.activity.ViewBallGameActivity;
import com.zuo.ballgame.ballgame.options.BallGameOptions;
import com.zuo.ballgame.ballgame.thing.BallSpirit;


/**
 * @author zuo
 * @filename: GameAdapter
 * @date: 2020/4/28
 * @description: 描述
 * @version: 版本号
 */
public class BallsAdapter extends BaseQuickAdapter<BallSpirit, BaseViewHolder> {
	ViewBallGameActivity mActivity;
	public BallsAdapter(ViewBallGameActivity activity) {
		super(R.layout.item_ball);
		mActivity = activity;

	}

	@Override
	protected void convert(BaseViewHolder helper, BallSpirit item) {
		ImageView imageView = helper.getView(R.id.iv_ball);
		RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
		rl.height = (int) BallGameOptions.BALL_LENGTH;
		if (BallGameOptions.isNeed3(helper.getLayoutPosition())) {
			rl.width = (int) BallGameOptions.BALL_LENGTH;
			rl.setMargins((int) (BallGameOptions.BALL_LENGTH * 0.5), 0, 0, 0);
		} else {
			rl.width = (int) BallGameOptions.BALL_LENGTH;
		}
		imageView.setLayoutParams(rl);
		imageView.setImageResource( item.getImage());
		if (!item.isShow() ) {
			imageView.setVisibility(View.INVISIBLE);
		} else {
			imageView.setVisibility(View.VISIBLE);
		}

		if (item.isBomb() && item.getBomb_view() == null) {
			imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					int[] location = new int[2];
					imageView.getLocationInWindow(location);
					item.setLoacationX(location[0]);
					item.setLoacationY(location[1]);
					mActivity.addBombView(item,helper.getLayoutPosition());
				}
			});

		}
	}

}
