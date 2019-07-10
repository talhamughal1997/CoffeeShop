package com.example.coffeeshop.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.coffeeshop.Fragments.MainActivity.FirstFragment;
import com.example.coffeeshop.Fragments.MainActivity.SecondFragment;
import com.example.coffeeshop.Fragments.MainActivity.ThirdFragment;

public class DiscoverAdapter extends FragmentStatePagerAdapter {
    public DiscoverAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return new FirstFragment(); //ChildFragment1 at position 0
            case 1:
                return new SecondFragment(); //ChildFragment2 at position 1
            case 2:
                return new ThirdFragment(); //ChildFragment3 at position 2
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 3;
    }
}
