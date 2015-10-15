package com.ylqhust.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ylqhust.adapter.ViewPagerAdapter;
import com.ylqhust.v2ex.Global;
import com.ylqhust.v2ex.R;

public class Account extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match

    private String mission_html;
    private ViewPagerAdapter.SetUserPage setUserPage;
    public void setSetUserPage(ViewPagerAdapter.SetUserPage setUserPage)
    {
        this.setUserPage = setUserPage;
    }
    public Account(String mission_html) {
        this.mission_html = mission_html;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,container,false);
        Button loginOut = (Button) view.findViewById(R.id.fragment_account_button_loginout);
        loginOut.setOnClickListener(this);
        /**
        WebView view = new WebView(getActivity());
        view.loadData(mission_html,"text/html","UTF-8");
         **/
        return view;
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id)
        {
            case R.id.fragment_account_button_loginout:
                loginOut();
                break;
            default:
                break;
        }
        return;
    }

    private void loginOut() {
        Global.isLogin = false;
        Global.v2EXManager.setmClient();
        setUserPage.setUserPage(null);
    }
}
