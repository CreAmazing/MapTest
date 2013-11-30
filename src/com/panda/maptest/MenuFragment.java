package com.panda.maptest;

import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

public class MenuFragment extends PreferenceFragment implements
		OnPreferenceClickListener {

	int index = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.menu);
		findPreference("a").setOnPreferenceClickListener(this);
		findPreference("b").setOnPreferenceClickListener(this);
		findPreference("c").setOnPreferenceClickListener(this);
		findPreference("d").setOnPreferenceClickListener(this);
		findPreference("e").setOnPreferenceClickListener(this);
		
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		String key = preference.getKey();
		if ("a".equals(key)) {
			if (index == 0) {
				((MainActivity) getActivity()).getSlidingMenu().toggle();
				return true;
			}

			index = 0;
			FragmentManager fragmentManager = ((MainActivity) getActivity())
					.getFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(R.id.content,
							new ARListFragment()).commit();
		}else if("b".equals(key)){
			if(index == 1) {
                ((MainActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            index = 1;
            FragmentManager fragmentManager = ((MainActivity)getActivity()).getFragmentManager();
            fragmentManager.beginTransaction()
            .replace(R.id.content, new ContentFragment("This is 景区地图"))
            .commit();
		}else if("c".equals(key)) {

            if(index == 2) {
                ((MainActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            index = 2;
            FragmentManager fragmentManager = ((MainActivity)getActivity()).getFragmentManager();
            fragmentManager.beginTransaction()
            .replace(R.id.content, new ContentFragment("This is 景点介绍"))
            .commit();
        }else if("d".equals(key)) {

            if(index == 3) {
                ((MainActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            index = 3;
            FragmentManager fragmentManager = ((MainActivity)getActivity()).getFragmentManager();
            fragmentManager.beginTransaction()
            .replace(R.id.content, new ContentFragment("This is 景区服务"))
            .commit();
        }else if("e".equals(key)) {

            if(index == 4) {
                ((MainActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            index = 4;
            FragmentManager fragmentManager = ((MainActivity)getActivity()).getFragmentManager();
            fragmentManager.beginTransaction()
            .replace(R.id.content, new ContentFragment("更多"))
            .commit();
        }
		
		((MainActivity)getActivity()).getSlidingMenu().toggle();
		return false;
	}

}
