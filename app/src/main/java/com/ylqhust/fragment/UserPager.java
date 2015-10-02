package com.ylqhust.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylqhust.contract.MemberScheam;
import com.ylqhust.image.AsyncDrawable;
import com.ylqhust.image.DownloadImageTask;
import com.ylqhust.utils.ImageUtils;
import com.ylqhust.utils.NetWork;
import com.ylqhust.v2ex.Global;
import com.ylqhust.v2ex.R;



public class UserPager extends Fragment implements View.OnClickListener {

    private MemberScheam memberScheam = null;

    private ImageView member_head_image;

    private TextView member_name;
    private TextView tagline;
    private TextView createTime;
    private TextView website_name;
    private TextView twitter_name;
    private TextView psn_name;
    private TextView btc_name;
    private TextView github_name;
    private TextView bio;

    public UserPager(MemberScheam memberScheam) {
        this.memberScheam = memberScheam;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.user_page, container, false);
        initView(view);
        setPage();
        setListener();
        return view;
    }




    private void initView(View view)
    {
        this.member_head_image = (ImageView) view.findViewById(R.id.member_head_image);
        this.member_name = (TextView) view.findViewById(R.id.member_name);
        this.tagline = (TextView) view.findViewById(R.id.tagline);
        this.createTime = (TextView) view.findViewById(R.id.createtime);
        this.website_name = (TextView) view.findViewById(R.id.website_name);
        this.twitter_name = (TextView) view.findViewById(R.id.twitter_name);
        this.psn_name = (TextView) view.findViewById(R.id.psn_name);
        this.btc_name = (TextView) view.findViewById(R.id.btc_name);
        this.github_name = (TextView) view.findViewById(R.id.github_name);
        this.bio = (TextView) view.findViewById(R.id.bio);
    }

    private void setPage()
    {
        //下载图片
        String member_head_url_mini = memberScheam.getAvatar_mini();
        if (ImageUtils.cancelPotentialWork(member_head_url_mini,member_head_image))
        {
            final DownloadImageTask downloadImageTask = new DownloadImageTask(member_head_image);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(null,null,downloadImageTask);
            member_head_image.setImageDrawable(asyncDrawable);
            downloadImageTask.execute(member_head_url_mini,"50","50");
        }

        member_name.setText(memberScheam.getUsername());
        tagline.setText(memberScheam.getTagLine());
        long currentTime = System.currentTimeMillis();
        long createdTime = memberScheam.getCreatedTime();
        int delt = (int)(currentTime/1000 - createdTime)/60/60/24;
        createTime.setText("创建于"+delt+"天前");
        website_name.setText(memberScheam.getWebsite());
        twitter_name.setText(memberScheam.getTwitter());
        psn_name.setText(memberScheam.getPsn());
        btc_name.setText(memberScheam.getBtc());
        github_name.setText(memberScheam.getGithub());
        bio.setText(memberScheam.getBio());
    }
    private void setListener()
    {
        member_head_image.setOnClickListener(this);
        if (memberScheam.getWebsite() != null && !memberScheam.getWebsite().equals(""))
            website_name.setOnClickListener(this);
        if (memberScheam.getTwitter() != null && !memberScheam.getTwitter().equals(""))
            twitter_name.setOnClickListener(this);
        if (memberScheam.getPsn() != null && !memberScheam.getPsn().equals(""))
            psn_name.setOnClickListener(this);
        if (memberScheam.getGithub() != null && !memberScheam.getGithub().equals(""))
            github_name.setOnClickListener(this);
        if (memberScheam.getBtc() != null && !memberScheam.getBtc().equals(""))
            btc_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        final int id = v.getId();
        switch (id)
        {
            case R.id.member_head_image:
                NetWork.GoToWeb(memberScheam.getUrl(), Global.activity);
                break;
            case R.id.website_name:
                break;
            case R.id.twitter_name:
                break;
            case R.id.psn_name:
                break;
            case R.id.github_name:
                break;
            case R.id.btc_name:
                break;
        }
        return;
    }
}
