/**
 * 
 * Copyright 2014 AT Computing BV
 *
 * This file is part of Linux Reference Card.
 *
 * Linux Reference Card is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Linux Reference Card is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Linux Reference Card.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package nl.atcomputing.refcard.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.atcomputing.refcard.R;
import nl.atcomputing.refcard.fragments.CommandFragment;
import nl.atcomputing.refcard.fragments.RegExpFragment;
import nl.atcomputing.refcard.fragments.ViFragment;

/**
 * @author martijn
 *
 */
public class SlidingTabFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slidingtab_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		viewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));

		SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
		slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.atbluedark));
		slidingTabLayout.setDividerColors(getResources().getColor(R.color.atbluedark));
		slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.atbluelight));
        //Does not work in landscape mode
//		mSlidingTabLayout.setCustomTabView(R.layout.slidingtab_tabview, R.id.textview);

		slidingTabLayout.setViewPager(viewPager);
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
				return new CommandFragment();
			case 1:
				return new RegExpFragment();
			case 2:
				return new ViFragment();
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
