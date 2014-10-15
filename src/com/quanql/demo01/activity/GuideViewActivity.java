package com.quanql.demo01.activity;

import java.util.ArrayList;

import com.quanql.demo01.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * http://blog.csdn.net/wsscy2004/article/details/7611529
 */
public class GuideViewActivity extends Activity {

	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ImageView imageView;
	private ImageView[] imageViews;
	// 包裹滑动图片LinearLayout
	private ViewGroup main;
	// 包裹小圆点的LinearLayout
	private ViewGroup group;
	// 左箭头按钮
	private ImageView imageViewLeft;
	// 右箭头按钮
	private ImageView imageViewRight;
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
		imageViews = new ImageView[pageViews.size()];
		main = (ViewGroup) inflater.inflate(R.layout.activity_guidepages, null);

		group = (ViewGroup) main.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) main.findViewById(R.id.guidePages);
//		imageViewLeft = (ImageView) main.findViewById(R.id.imageView1);
//		imageViewRight = (ImageView) main.findViewById(R.id.imageView2);
//		imageViewLeft.setAlpha(0);
//		imageViewRight.setAlpha(0);

		// 将小圆点放到imageView数组当中
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(GuideViewActivity.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;

			if (i == 0) {
				// 默认选中第一张图片
				imageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator);
			}

			group.addView(imageViews[i]);
		}

		setContentView(main);

		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
//		imageViewLeft.setOnClickListener(new ButtonListener());
//		imageViewRight.setOnClickListener(new ButtonListener());
	}

	// 左右切换屏幕的按钮监听器
	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int showNext = 0;
			if (v.getId() == R.id.imageView1) {
				System.out.println("点击了向左的按钮");
				if (currentIndex == 0)
					showNext = currentIndex;
				else
					showNext = currentIndex - 1;
				viewPager.setCurrentItem(showNext);
			}
			if (v.getId() == R.id.imageView2) {
				System.out.println("点击了向右的按钮");
				if (currentIndex == imageViews.length)
					showNext = currentIndex;
				else
					showNext = currentIndex + 1;
				viewPager.setCurrentItem(showNext);
			}
			System.out.println("当前页码：" + showNext);

		}

	}

	/**
	 * 设置按钮渐显效果
	 */
/*	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1 && mAlpha < 255) {
				// 通过设置不透明度设置按钮的渐显效果
				mAlpha += 50;

				if (mAlpha > 255)
					mAlpha = 255;

				imageViewLeft.setAlpha(mAlpha);
				imageViewLeft.invalidate();
				imageViewRight.setAlpha(mAlpha);
				imageViewRight.invalidate();

				if (!isHide && mAlpha < 255)
					mHandler.sendEmptyMessageDelayed(1, 100);
			} else if (msg.what == 0 && mAlpha > 0) {
				mAlpha -= 3;

				if (mAlpha < 0)
					mAlpha = 0;
				imageViewLeft.setAlpha(mAlpha);
				imageViewLeft.invalidate();
				imageViewRight.setAlpha(mAlpha);
				imageViewRight.invalidate();

				if (isHide && mAlpha > 0)
					mHandler.sendEmptyMessageDelayed(0, 2);
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
		System.out.println("this is dispatch");
		System.out.println("触碰屏幕");
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_DOWN:
			showImageButtonView();
			break;
		case MotionEvent.ACTION_UP:
			hideImageButtonView();
			break;
		}

		return super.dispatchTouchEvent(ev);
	}
*/
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
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	// 指引页面更改事件监听器,左右滑动图片时候，小圆点变换显示当前图片位置
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			currentIndex = arg0;
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.page_indicator_focused);

				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator);
				}
			}
		}
	}

}
