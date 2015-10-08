package com.ylqhust.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ylqhust.contract.ArticleInfo;
import com.ylqhust.contract.ReplyInfo;
import com.ylqhust.image.AsyncDrawable;
import com.ylqhust.image.DownloadImageTask;
import com.ylqhust.utils.ImageUtils;
import com.ylqhust.v2ex.R;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apple on 15/10/1.
 */
public class ArticleContentAdapter extends BaseAdapter {
    private ArticleInfo articleInfo;
    private List<ReplyInfo> replyInfos;
    private LayoutInflater inflater;

    //帖子中的纯文本
    private List<String> PlainText = null;
    //帖子中的图片
    private List<String> Img = null;

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
            //将content分割成文本和图片链接
            SplitContent(articleInfo.getContent());
            //将文本中的非图片链接找到并添加点击事件

            //返回帖子的view
            View view = inflater.inflate(R.layout.article_content_firsthalf,null);
            TextView article_tag_container = (TextView) view.findViewById(R.id.article_tag_container);
            ImageView user_image = (ImageView) view.findViewById(R.id.user_image);
            TextView title = (TextView) view.findViewById(R.id.title);
            //帖子文本内容和图片资源的容器
            LinearLayout article_content = (LinearLayout) view.findViewById(R.id.article_content);

            TextView article_base_info = (TextView) view.findViewById(R.id.article_base_info);
            TextView reply_base_info = (TextView) view.findViewById(R.id.reply_base_info);

            article_tag_container.setText(articleInfo.getArticle_Tag());
            title.setText(articleInfo.getTitle());
            article_base_info.setText(articleInfo.getInfo());
            setArticleTextAndImage(article_content);
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

    //使用PlainText和Img中的数据构造好LinearLayout容器
    private void setArticleTextAndImage(LinearLayout article_content)
    {
        for(int i=0;i<PlainText.size();i++)
        {
            String text = PlainText.get(i);
            if (!IncludeTrashContent(text))
            {
                TextView textView = (TextView) inflater.inflate(R.layout.article_content_plaintext,null);
                textView.setText(text);
                article_content.addView(textView);
            }
            if(i != Img.size())
            {
                System.out.println(Img.get(i));
                ImageView imageView = (ImageView) inflater.inflate(R.layout.article_content_img,null);
                String image_url = Img.get(i);
                if (ImageUtils.cancelPotentialWork(image_url,imageView))
                {
                    DownloadImageTask downloadImageTask = new DownloadImageTask(imageView);
                    AsyncDrawable asyncDrawable = new AsyncDrawable(null,null,downloadImageTask);
                    imageView.setImageDrawable(asyncDrawable);
                    downloadImageTask.execute(image_url,"500","800");
                }
                article_content.addView(imageView);
            }
        }
    }

    //检测是否包含垃圾内容
    private boolean IncludeTrashContent(String text)
    {
        if("".equals(text))
            return true;
        return false;
    }


    //获得内容中的图片连接,将内容分割成文本内容和图片链接
    private void SplitContent(String content)
    {
        List<String> imgUrl = new ArrayList<String>();
        List<String> contents = new ArrayList<String>();
        List<String> finalImgUrl = new ArrayList<String>();
        String regx = "(!\\[.*\\]\\( )*(((http:)|(https:))*[0-9a-zA-Z_\\-/\\.,&=]*\\.((png)|(jpeg)|(bmp)|(tga)|(svg)|(psd)|(jpg)))(\\))*";
        //String regx = "((http:)|(https:))*[0-9a-zA-Z_\\-/\\.,&=]*\\.((png)|(jpeg)|(bmp)|(tga)|(svg)|(psd)|(jpg))";
        //用于获取图片
        Pattern pattern  = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find())
        {
            //String string = matcher.group();
            imgUrl.add(matcher.group());
        }
        //分割content成为两部分
        for(String img : imgUrl)
        {
            int index = content.indexOf(img);
            contents.add(content.substring(0,index));
            content = content.substring(index+img.length());
        }
        //将最后一部分文本添加进来，可能为空，但没关系
        contents.add(content);

        //将http添加到图片链接，并去掉![]( ),因为有的图片可能没有这个头
        String regx2 = "((http:)|(https:))*[0-9a-zA-Z_\\-/\\.,&=]*\\.((png)|(jpeg)|(bmp)|(tga)|(svg)|(psd)|(jpg))";
        for (String img : imgUrl)
        {
            Pattern patterns = Pattern.compile(regx2);
            Matcher matchers = patterns.matcher(img);
            if (matchers.find())
                img = matchers.group();
            if (!img.contains("http:"))
                    img = "http:"+img;
            finalImgUrl.add(img);
        }

        PlainText = contents;
        Img = finalImgUrl;
    }
}
