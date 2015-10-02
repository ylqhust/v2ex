package com.ylqhust.v2ex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ylqhust.adapter.ReplyContentAdapter;
import com.ylqhust.contract.ArticleInfo;
import com.ylqhust.image.AsyncDrawable;
import com.ylqhust.image.DownloadImageTask;
import com.ylqhust.json.ResolveLatestJSONArray;
import com.ylqhust.resolvehtml.ResolveOneArticle;
import com.ylqhust.utils.ImageUtils;

public class ShowArticleActivity extends AppCompatActivity implements ResolveOneArticle.UpdateUI{

    private TextView article_tag_container;
    private ImageView user_image;
    private TextView title;
    private TextView article_base_info;
    private TextView article_content;
    private TextView reply_base_info;
    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_content);
        initView();
        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        ResolveOneArticle resolveOneArticle = new ResolveOneArticle(url,null);
        resolveOneArticle.setUpdateUI(this);
        resolveOneArticle.resolve();
    }

    private void initView() {
        article_tag_container = (TextView) findViewById(R.id.article_tag_container);
        user_image = (ImageView) findViewById(R.id.user_image);
        title = (TextView) findViewById(R.id.title);
        article_content = (TextView) findViewById(R.id.article_content);
        article_base_info = (TextView) findViewById(R.id.article_base_info);
        reply_base_info = (TextView) findViewById(R.id.reply_base_info);
        listView = (ListView) findViewById(R.id.reply_content_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateUI(ArticleInfo articleInfo) {
        //进行UI跟新的操作
        if (articleInfo == null)
        {
            //没有缓存也没有网络
            Toast.makeText(this,"获取失败",Toast.LENGTH_SHORT).show();
            return;
        }
        //进行跟新操作

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

        //设置回复的listView
        if (articleInfo.getReplyInfos() != null)
        {
            ReplyContentAdapter replyContentAdapter = new ReplyContentAdapter(articleInfo.getReplyInfos(),getLayoutInflater());
            listView.setAdapter(replyContentAdapter);
        }
    }
}
