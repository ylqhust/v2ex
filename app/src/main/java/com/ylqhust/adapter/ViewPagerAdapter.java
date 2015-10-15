package com.ylqhust.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ylqhust.fragment.Login;
import com.ylqhust.v2ex.Global;
import com.ylqhust.v2ex.R;

import java.util.List;

/**
 * Created by apple on 15/9/29.
 */
public class ViewPagerAdapter extends PagerAdapter {

    List<View> lists;
    private SetUserPage setUserPage;

    public ViewPagerAdapter(List<View> lists) {
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists==null?0:lists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object)
    {
        container.removeView(lists.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        container.addView(lists.get(position));
        if (position == 2)
            this.setUserPage.setUserPage("null");
        return lists.get(position);
    }


    public interface SetUserPage{
        public void setUserPage(String string);
    }

    public void setSetUserPage(SetUserPage setUserPage)
    {
        this.setUserPage = setUserPage;
    }

}
