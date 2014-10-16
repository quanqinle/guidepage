package com.quanql.guidepage.activity;

import java.util.ArrayList;

import com.quanql.guidepage.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * http://blog.csdn.net/wsscy2004/article/details/7611529
 */
public class GuideViewActivity extends Activity {
    private String TAG = "GuidePage";
	private ViewPager viewPager;
	private ArrayList<View> pageViews; // 背景图
	private int pageViewsCount = 0; // 背景图数量
	private ImageView imgViewRound; // 圆点
//	private ImageView[] imgViewRounds; // 存储点的数组
	// 包裹滑动图片LinearLayout
	private ViewGroup main;
	// 包裹小圆点的LinearLayout
	private ViewGroup viGroupRound;
	// 左箭头按钮
	private ImageView btnLeft;
	// 右箭头按钮
	private ImageView btnRight;
	// 当前页码
	private int currentIndex;

	// ImageView的alpha值
	private int mAlpha = 0;
	private boolean isHide;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// 将要显示的图片放到ArrayList当中，存到适配器中
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.item01, null));
		pageViews.add(inflater.inflate(R.layout.item02, null));
		pageViews.add(inflater.inflate(R.layout.item03, null));
		pageViewsCount = pageViews.size();
		
		main = (ViewGroup) inflater.inflate(R.layout.activity_guidepages, null);
		viewPager = (ViewPager) main.findViewById(R.id.guidePages);
		btnLeft = (ImageView) main.findViewById(R.id.imageViewLeft);
		btnRight = (ImageView) main.findViewById(R.id.imageViewRight);
		btnLeft.setAlpha(0);
		btnRight.setAlpha(0);
		viGroupRound = (ViewGroup) main.findViewById(R.id.viewGroup);
		
		initNavigationLayout();
		
		setContentView(main);
		
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setCurrentItem(currentIndex);
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		btnLeft.setOnClickListener(new ButtonListener());
		btnRight.setOnClickListener(new ButtonListener());
	}

	/**
	 * 初始化导航圆点
	 */
	private void initNavigationLayout() {
		if (pageViewsCount > 1) {
			for (int i = 0; i < pageViewsCount; i++) {
				int resId = R.drawable.page_indicator;
				if (0 == i) {
					resId = R.drawable.page_indicator_focused;
				}
				imgViewRound = new ImageView(this);
				imgViewRound.setImageResource(resId);
				imgViewRound.setPadding(0, 0, 20, 0);
				viGroupRound.addView(imgViewRound);
			}
			viGroupRound.setVisibility(View.VISIBLE);
		} else {
			viGroupRound.setVisibility(View.GONE);
		}
	}
	
/*
    // 另一种实现方法
	private void initNavigationLayout_Old() {
		for (int i = 0; i < pageViewsCount; i++) {
			imgViewRound = new ImageView(GuideViewActivity.this);
			imgViewRound.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 30));
			imgViewRound.setPadding(20, 0, 20, 0);
			imgViewRounds[i] = imgViewRound;

			if (i == 0) {
				imgViewRounds[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imgViewRounds[i].setBackgroundResource(R.drawable.page_indicator);
			}

			viGroupRound.addView(imgViewRounds[i]);
		}
	}
	*/
	
	// 左右切换屏幕的按钮监听器
	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int showNext = 0;
			if (v.getId() == R.id.imageViewLeft) {
				Log.d(TAG, "点击了向左的按钮");
				if (currentIndex == 0) {
					showNext = currentIndex;
				} else {
					showNext = currentIndex - 1;
				}
				viewPager.setCurrentItem(showNext);
			}
			if (v.getId() == R.id.imageViewRight) {
				Log.d(TAG, "点击了向右的按钮");
				if (currentIndex == pageViewsCount) {
					showNext = currentIndex;
				} else {
					showNext = currentIndex + 1;
				}
				viewPager.setCurrentItem(showNext);
			}
			Log.d(TAG, "当前页码：" + showNext);
		}

	}

	/**
	 * 设置按钮渐显效果
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1 && mAlpha < 255) {
				// 通过设置不透明度设置按钮的渐显效果
				mAlpha += 50;

				if (mAlpha > 255) {
					mAlpha = 255;
				}

				btnLeft.setAlpha(mAlpha);
				btnLeft.invalidate();
				btnRight.setAlpha(mAlpha);
				btnRight.invalidate();

				if (!isHide && mAlpha < 255) {
					mHandler.sendEmptyMessageDelayed(1, 100);
				}
			} else if (msg.what == 0 && mAlpha > 0) {
				mAlpha -= 3;

				if (mAlpha < 0) {
					mAlpha = 0;
				}
				btnLeft.setAlpha(mAlpha);
				btnLeft.invalidate();
				btnRight.setAlpha(mAlpha);
				btnRight.invalidate();

				if (isHide && mAlpha > 0) {
					mHandler.sendEmptyMessageDelayed(0, 2);
				}
			}
		}
	};

	private void showImageButtonView() {
		isHide = false;
		mHandler.sendEmptyMessage(1);
	}

	private void hideImageButtonView() {
		new Thread() {
			public void run() {
				try {
					isHide = true;
					mHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					;
				}
			}
		}.start();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.d(TAG, "this is dispatch");
		Log.d(TAG, "触碰屏幕");
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_DOWN: {
			showImageButtonView();
			break;
			}
		case MotionEvent.ACTION_UP: {
			hideImageButtonView();
			break;
			}
		}

		return super.dispatchTouchEvent(ev);
	}

	// 指引页面数据适配器,实现适配器方法
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	// 指引页面更改事件监听器,左右滑动图片时候，小圆点变换显示当前图片位置
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			currentIndex = arg0;
			for (int i = 0; i < pageViewsCount; i++) {
				int resId = R.drawable.page_indicator;
				if (arg0 == i) {
					resId = R.drawable.page_indicator_focused;
				}
				imgViewRound = (ImageView)viGroupRound.getChildAt(i);
				imgViewRound.setImageResource(resId);
			}
		}
	}

}
