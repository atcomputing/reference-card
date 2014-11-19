package nl.atcomputing.refcard.tabs;

import nl.atcomputing.refcard.R;
import nl.atcomputing.refcard.fragments.CommandFragment;
import nl.atcomputing.refcard.fragments.RegExpFragment;
import nl.atcomputing.refcard.fragments.ViFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * TODO replace PagerAdapter with FragmentPagerAdapter https://github.com/codepath/android_guides/wiki/ViewPager-with-FragmentPagerAdapter
 * @author martijn
 *
 */
public class SlidingTabFragment extends Fragment {

	private SlidingTabLayout mSlidingTabLayout;

	private ViewPager mViewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slidingtabfragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
		
		mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(mViewPager);
	}
	
	class MyPagerAdapter extends FragmentPagerAdapter {
		private int NUM_ITEMS = 3;

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return CommandFragment.getInstance();
			case 1:
				return RegExpFragment.getInstance();
			case 2:
				return ViFragment.getInstance();
			default:
				return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return CommandFragment.getName();
			case 1:
				return RegExpFragment.getName();
			case 2:
				return ViFragment.getName();
			default:
				return "";
			}
		}
	}
}
