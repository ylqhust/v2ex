package com.ylqhust.v2ex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ylqhust.adapter.ArticleContentAdapter;
import com.ylqhust.contract.ArticleInfo;
import com.ylqhust.image.AsyncDrawable;
import com.ylqhust.image.DownloadImageTask;
import com.ylqhust.resolvehtml.ResolveOneArticle;
import com.ylqhust.utils.ImageUtils;

public class ShowArticleActivity extends AppCompatActivity implements ResolveOneArticle.UpdateUI{


    private ListView listView;
    private String url;
    private String contentWithImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_content);
        initView();
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");
        contentWithImage = intent.getStringExtra("contentWithImage");
        initGlobal();
        initArticleContent();
    }

    private void initGlobal()
    {
        Global.progressDialogSpinner2 = new ProgressDialog(this);
        Global.progressDialogSpinner2.setTitle("提示信息");
        Global.progressDialogSpinner2.setMessage("正在获取数据...");
        Global.progressDialogSpinner2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void initArticleContent() {
        ResolveOneArticle resolveOneArticle = new ResolveOneArticle(url,null);
        resolveOneArticle.setUpdateUI(this);
        resolveOneArticle.resolve();
    }

    private void initView() {

        listView = (ListView) findViewById(R.id.article_content_container);
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
        if (id == R.id.action_refresh) {
            initArticleContent();
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
            Toast.makeText(this,"无法获取主题...",Toast.LENGTH_SHORT).show();
            return;
        }
        //设置数据
        articleInfo.setContent(contentWithImage);
        ArticleContentAdapter replyContentAdapter = new ArticleContentAdapter(articleInfo,getLayoutInflater());
        listView.setAdapter(replyContentAdapter);
    }
}
