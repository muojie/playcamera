package org.yanzi.playcamera;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.yanzi.playcamera.fragment.HomeFragment;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

// since v1.0.0, forced extends of SupportActivity is not required, you can use interface + delegate to implement your own SupportActivity
public class MainActivity extends SupportActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	public static final String TAG = MainActivity.class.getSimpleName();

	// 再点一次退出程序时间设置
	private static final long WAIT_TIME = 2000L;
	private long TOUCH_TIME = 0;

	private DrawerLayout mDrawer;
	private NavigationView mNavigationView;
	private TextView mTvName;   // NavigationView上的名字
	private ImageView mImgNav;  // NavigationView上的头像

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (findFragment(HomeFragment.class) == null) {
			loadRootFragment(R.id.fl_container, HomeFragment.newInstance());  //load root Fragment
		}
		initView();
	}

	private void initView() {
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawer.setDrawerListener(toggle);
		toggle.syncState();

		mNavigationView = (NavigationView) findViewById(R.id.nav_view);
		mNavigationView.setNavigationItemSelectedListener(this);
		mNavigationView.setCheckedItem(R.id.nav_home);

		LinearLayout llNavHeader = (LinearLayout) mNavigationView.getHeaderView(0);
		mTvName = (TextView) llNavHeader.findViewById(R.id.tv_name);
		mImgNav = (ImageView) llNavHeader.findViewById(R.id.img_nav);
//		llNavHeader.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mDrawer.closeDrawer(GravityCompat.START);
//				mDrawer.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						goLogin();
//					}
//				}, 250);
//			}
//		});
	}

	@Override
	public void onBackPressedSupport() {
		if (mDrawer.isDrawerOpen(GravityCompat.START)) {
			mDrawer.closeDrawer(GravityCompat.START);
		} else {
			ISupportFragment topFragment = getTopFragment();

			// 主页的Fragment
			/*
			if (topFragment instanceof BaseMainFragment) {
				mNavigationView.setCheckedItem(R.id.nav_home);
			}

			if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
				pop();
			} else {
				if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
					finish();
				} else {
					TOUCH_TIME = System.currentTimeMillis();
					Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
				}
			}
			*/
		}
	}

	@Override
	public boolean onNavigationItemSelected(final MenuItem item) {
		mDrawer.closeDrawer(GravityCompat.START);

		mDrawer.postDelayed(new Runnable() {
			@Override
			public void run() {
				int id = item.getItemId();

				final ISupportFragment topFragment = getTopFragment();

				if (id == R.id.nav_home) {

					HomeFragment fragment = findFragment(HomeFragment.class);
					Bundle newBundle = new Bundle();
					newBundle.putString("from", "From:" + topFragment.getClass().getSimpleName());
					fragment.putNewBundle(newBundle);

					start(fragment, SupportFragment.SINGLETASK);
				} else if (id == R.id.nav_discover) {
//					DiscoverFragment fragment = findFragment(DiscoverFragment.class);
//					if (fragment == null) {
//						popTo(HomeFragment.class, false, new Runnable() {
//							@Override
//							public void run() {
//								start(DiscoverFragment.newInstance());
//							}
//						});
//					} else {
//						// 如果已经在栈内,则以SingleTask模式start
//						start(fragment, SupportFragment.SINGLETASK);
//					}
				} else if (id == R.id.nav_msg) {
//					ShopFragment fragment = findFragment(ShopFragment.class);
//					if (fragment == null) {
//						popTo(HomeFragment.class, false, new Runnable() {
//							@Override
//							public void run() {
//								start(ShopFragment.newInstance());
//								// 设置pop动画为： popExit， 配合start()，视觉上动画会很合理（类似startWithPop()的动画）
//							}
//						}, getFragmentAnimator().getPopExit());
//					} else {
//						// 如果已经在栈内,则以SingleTask模式start,也可以用popTo
////                        start(fragment, SupportFragment.SINGLETASK);
//						popTo(ShopFragment.class, false);
//					}
				} else if (id == R.id.nav_login) {
//					goLogin();
				}
			}
		}, 300);

		return true;
	}

}
