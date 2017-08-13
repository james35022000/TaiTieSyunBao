package jcchen.taitiesyunbao.View.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Vector;

/**
 * Created by JCChen on 2017/7/16.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    Vector<Fragment> vector;

    public MainPagerAdapter(FragmentManager fm, Vector<Fragment> vector) {
        super(fm);
        this.vector = vector;
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return vector.get(arg0);
    }
}
