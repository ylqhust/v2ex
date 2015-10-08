package com.ylqhust.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylqhust.image.AsyncDrawable;
import com.ylqhust.image.DownloadImageTask;
import com.ylqhust.contract.LatestScheam;
import com.ylqhust.utils.ImageUtils;
import com.ylqhust.utils.NetWork;
import com.ylqhust.v2ex.Global;
import com.ylqhust.v2ex.R;
import com.ylqhust.v2ex.ShowArticleActivity;

import java.util.List;


/**
 * Created by apple on 15/9/29.
 */
public class LatestListViewAdapter extends BaseAdapter implements View.OnClickListener{
    private List<LatestScheam> lists;
    private LayoutInflater inflater;

    public LatestListViewAdapter(LayoutInflater inflater, List<LatestScheam> lists) {
        this.inflater = inflater;
        this.lists = lists;
    }


    @Override
    public int getCount() {
        return lists==null?0:lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = inflater.inflate(R.layout.latest,null);
        ImageView user_head = (ImageView) view.findViewById(R.id.user_head_image);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView node_name = (TextView) view.findViewById(R.id.node_name);
        TextView author = (TextView) view.findViewById(R.id.author);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView replies = (TextView) view.findViewById(R.id.replies);

        LatestScheam latestScheam = lists.get(position);
        //String user_head_url_mini = latestScheam.getUser_head_image_mini_url();
        String user_head_url_large = latestScheam.getUser_head_image_large_url();
        if (ImageUtils.cancelPotentialWork(user_head_url_large,
                user_head))
        {
            final DownloadImageTask downloadImageTask = new DownloadImageTask(user_head);
            final AsyncDrawable asyncDrawable  = new AsyncDrawable(null,null,downloadImageTask);
            user_head.setImageDrawable(asyncDrawable);
            downloadImageTask.execute(user_head_url_large,"100","100");
        }
        //设置点击事件
        user_head.setOnClickListener(this);
        title.setOnClickListener(this);
        //设置位置
        user_head.setContentDescription(""+position);
        title.setContentDescription(""+position);

        title.setText(latestScheam.getTitle());
        node_name.setText(latestScheam.getNode_name());
        author.setText(latestScheam.getUser_name());
        long currentTime = System.currentTimeMillis();
        long createdTime = latestScheam.getCreateTime();
        int delt = (int)(currentTime/1000 - createdTime)/60;
        time.setText("发表于"+delt+"分钟前");
        //为了避免setText直接去string.xml找id，添加一个+“”
        replies.setText(latestScheam.getReplies()+"");
        return view;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch(id)
        {
            case R.id.user_head_image:
                int position = Integer.valueOf(v.getContentDescription().toString());
                NetWork.GoToWeb(Global.MEMBER_URL+lists.get(position).getUser_name(), Global.activity);
                break;
            case R.id.title:
                int position2 = Integer.valueOf(v.getContentDescription().toString());
                String url = lists.get(position2).getUrl();
                //String url = "http://www.v2ex.com/t/225506";
                String contentWithImage = lists.get(position2).getContent();
                Intent intent = new Intent();
                intent.setClass(Global.activity, ShowArticleActivity.class);
                intent.putExtra("URL", url);
                intent.putExtra("contentWithImage",contentWithImage);
                intent.putExtra("title",lists.get(position2).getTitle());
                intent.putExtra("head_image_url",lists.get(position2).getUser_head_image_large_url());
                Global.activity.startActivity(intent);
                break;
        }
    }
}
