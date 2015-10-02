package com.ylqhust.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylqhust.contract.ReplyInfo;
import com.ylqhust.image.AsyncDrawable;
import com.ylqhust.image.DownloadImageTask;
import com.ylqhust.utils.ImageUtils;
import com.ylqhust.v2ex.R;


import java.util.List;

/**
 * Created by apple on 15/10/1.
 */
public class ReplyContentAdapter extends BaseAdapter {
    private List<ReplyInfo> replyInfos;
    private LayoutInflater inflater;
    public ReplyContentAdapter(List<ReplyInfo> replyInfos,LayoutInflater inflater) {
        this.inflater = inflater;
        this.replyInfos = replyInfos;
    }

    @Override
    public int getCount() {
        return replyInfos.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.reply_content,null);
        ImageView replyer_image = (ImageView) view.findViewById(R.id.replyer_image);
        TextView replyer_name = (TextView) view.findViewById(R.id.replyer_name);
        TextView this_reply_base_info = (TextView) view.findViewById(R.id.this_reply_base_info);
        TextView this_reply_content = (TextView) view.findViewById(R.id.this_reply_content);
        TextView no = (TextView) view.findViewById(R.id.no);

        //设置view
        replyer_name.setText(replyInfos.get(position).getReply_name());
        this_reply_base_info.setText(replyInfos.get(position).getReply_time());
        this_reply_content.setText(replyInfos.get(position).getReply_content());
        no.setText(replyInfos.get(position).getReply_number());

        String image_url = replyInfos.get(position).getImage_url();
        if (ImageUtils.cancelPotentialWork(image_url,replyer_image))
        {
            DownloadImageTask downloadImageTask = new DownloadImageTask(replyer_image);
            AsyncDrawable asyncDrawable = new AsyncDrawable(null,null,downloadImageTask);
            replyer_image.setImageDrawable(asyncDrawable);
            downloadImageTask.execute(image_url,"50","50");
        }
        return view;
    }
}
