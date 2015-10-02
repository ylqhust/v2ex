package com.ylqhust.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ylqhust.contract.ArticleInfo;
import com.ylqhust.contract.ReplyInfo;
import com.ylqhust.image.AsyncDrawable;
import com.ylqhust.image.DownloadImageTask;
import com.ylqhust.utils.ImageUtils;
import com.ylqhust.v2ex.R;


import java.util.List;

/**
 * Created by apple on 15/10/1.
 */
public class ArticleContentAdapter extends BaseAdapter {
    private ArticleInfo articleInfo;
    private List<ReplyInfo> replyInfos;
    private LayoutInflater inflater;
    public ArticleContentAdapter(ArticleInfo articleInfo, LayoutInflater inflater) {
        this.inflater = inflater;
        this.articleInfo = articleInfo;
        this.replyInfos = articleInfo.getReplyInfos();
    }

    @Override
    public int getCount() {
        if (articleInfo.getReplyInfos() == null)
            return 1;
        return articleInfo.getReplyInfos().size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //将帖子内容的view和恢复的view都放到一个listView中
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 0)
        {
            //返回帖子的view
            View view = inflater.inflate(R.layout.article_content_firsthalf,null);
            TextView article_tag_container = (TextView) view.findViewById(R.id.article_tag_container);
            ImageView user_image = (ImageView) view.findViewById(R.id.user_image);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView article_content = (TextView) view.findViewById(R.id.article_content);
            TextView article_base_info = (TextView) view.findViewById(R.id.article_base_info);
            TextView reply_base_info = (TextView) view.findViewById(R.id.reply_base_info);

            article_tag_container.setText(articleInfo.getArticle_Tag());
            title.setText(articleInfo.getTitle());
            article_base_info.setText(articleInfo.getInfo());
            article_content.setText(articleInfo.getContent());
            reply_base_info.setText(articleInfo.getReply_base_info());
            String image_url = articleInfo.getAuthor_image();
            if (ImageUtils.cancelPotentialWork(image_url,user_image))
            {
                DownloadImageTask downloadImageTask = new DownloadImageTask(user_image);
                AsyncDrawable asyncDrawable = new AsyncDrawable(null,null,downloadImageTask);
                user_image.setImageDrawable(asyncDrawable);
                downloadImageTask.execute(image_url,"100","100");//100,100代表尺寸
            }
            return view;
        }
        else
        {
            //返回恢复的view
            View view = inflater.inflate(R.layout.reply_content,null);
            ImageView replyer_image = (ImageView) view.findViewById(R.id.replyer_image);
            TextView replyer_name = (TextView) view.findViewById(R.id.replyer_name);
            TextView this_reply_base_info = (TextView) view.findViewById(R.id.this_reply_base_info);
            TextView this_reply_content = (TextView) view.findViewById(R.id.this_reply_content);
            TextView no = (TextView) view.findViewById(R.id.no);

            //由于将article_content 算了进去，所以这里要减去1
            position = position -1;
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
}
