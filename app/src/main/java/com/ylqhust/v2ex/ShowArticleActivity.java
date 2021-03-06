package com.ylqhust.v2ex;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.ylqhust.adapter.ArticleContentAdapter;
import com.ylqhust.contract.ArticleInfo;
import com.ylqhust.resolvehtml.ResolveOneArticleS;
import com.ylqhust.resolvehtml.UpdateUI;

import cz.msebera.android.httpclient.Header;

public class ShowArticleActivity extends AppCompatActivity implements UpdateUI<ArticleInfo>,View.OnClickListener{


    private ListView listView;
    private String url;
    private String contentWithImage;
    private String head_image_url;
    private String title_;
    private FloatingActionButton button_reply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_content);
        initView();
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");
        contentWithImage = intent.getStringExtra("contentWithImage");
        head_image_url = intent.getStringExtra("head_image_url");
        title_ = intent.getStringExtra("title");
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
        ResolveOneArticleS resolveOneArticleS = new ResolveOneArticleS(url);
        resolveOneArticleS.setUpdateUI(this);
        resolveOneArticleS.resolve();
        /**
        ResolveOneArticle resolveOneArticle = new ResolveOneArticle(url,null);
        resolveOneArticle.setUpdateUI(this);
        resolveOneArticle.resolve();
         **/
    }

    private void initView() {

        listView = (ListView) findViewById(R.id.article_content_container);
        button_reply = (FloatingActionButton) findViewById(R.id.article_content_floatbutton_reply);
        if (!Global.isLogin)
        {
            button_reply.setVisibility(View.GONE);
            button_reply.setClickable(false);
        }
        else {
            button_reply.setOnClickListener(this);
        }
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
            //使用contentWithImage和title和headImageUrl构造一个
            ArticleInfo tmp = new ArticleInfo();
            tmp.setContent(contentWithImage);
            tmp.setAuthor_image(head_image_url);
            tmp.setTitle(title_);
            ArticleContentAdapter tmpadapter = new ArticleContentAdapter(tmp,getLayoutInflater());
            listView.setAdapter(tmpadapter);
            return;
        }
        //设置数据
        articleInfo.setContent(contentWithImage);
        articleInfo.setAuthor_image(head_image_url);
        articleInfo.setTitle(title_);
        ArticleContentAdapter replyContentAdapter = new ArticleContentAdapter(articleInfo,getLayoutInflater());
        listView.setAdapter(replyContentAdapter);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        switch (id)
        {
            case R.id.article_content_floatbutton_reply:
                final View view = LayoutInflater.from(this).inflate(R.layout.reply_edittext,null);
                new AlertDialog.Builder(this).
                        setTitle("输入回复信息").
                        setView(view).
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final EditText editText = (EditText) view.findViewById(R.id.reply_edittext_et);
                                String content = editText.getText().toString();
                                System.out.println("CONTENT:"+content);
                                Global.v2EXManager.Reply(url, content, new TextHttpResponseHandler() {
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        Toast.makeText(ShowArticleActivity.this,"回复过于频繁,请1800秒后再试",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                        Toast.makeText(ShowArticleActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                                        initArticleContent();
                                    }
                                });
                            }
                        }).
                        setNegativeButton("取消",null).create().show();
                break;
            default:
                break;
        }

    }
}
