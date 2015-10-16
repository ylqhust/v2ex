package com.ylqhust.resolvehtml;

import com.ylqhust.contract.ArticleAndRepliesScheam;
import com.ylqhust.contract.ArticleInfo;
import com.ylqhust.contract.ReplyInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 15/10/1.
 */

@Deprecated
public class ResolveOneArticle implements GetHtmlTask.DoAfter
{
    private String url = null;
    private InputStream is = null;
    private File file = null;
    private String htmlString = null;
    private UpdateUI<ArticleInfo> updateUI = null;
    public ResolveOneArticle(String url, String noneed)
    {
        this.url = url;
    }
    public ResolveOneArticle(InputStream is)
    {
        this.is = is;
    }
    public ResolveOneArticle(File file)
    {
        this.file = file;
    }
    public ResolveOneArticle(String htmlString)
    {
        this.htmlString = htmlString;
    }

    public void resolve()
    {
        if (this.url != null)
        {
            resolveUrl();
        }
        else if (this.file != null)
        {
            resolveFile();
        }
        else if (this.is != null)
        {
            resolveInputStream();
        }
        else if (this.htmlString != null)
        {
            resolveHtmlString();
        }
        else
        {
            throw new IllegalArgumentException("没有可被解析的对象");
        }
    }

    //解析字符串
    private void resolveHtmlString()
    {
        if (this.htmlString == null)
        {
            //html为空，说明本地没有缓存也没有网络，回调到ShowArticleActivity中，传递一个NULL参数
            this.updateUI.updateUI(null);
            return;
        }

        ArticleInfo articleInfo = new ArticleInfo();

        Document doc = Jsoup.parse(this.htmlString);
        Element wrapper = doc.getElementById(ArticleAndRepliesScheam.WRAPPER_ID);

        Elements boxs = null;
        try
        {
            boxs = wrapper.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX_CLASS);
        }
        catch (Exception e)
        {
            this.updateUI.updateUI(null);
        }

        Element box1 = null;
        Element box2 = null;
        try{
            box1 = boxs.first();
            box2 = boxs.get(1);
        }
        catch(Exception e)
        {
            this.updateUI.updateUI(null);
            return;
        }
        if (box1 == null || box2 == null)
        {
            this.updateUI.updateUI(null);
            return;
        }

        Element head = box1.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX1_HEADER_CLASS).first();
        try{
            Element head_fr = head.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX1_HEADER_FR_CLASS).first();
            //发帖人头像
            String src = null;
            if (head_fr == null)
            {
                //表示无法获取主题
                this.updateUI.updateUI(null);
                return;
            }

            else
            {
                Element head_fr_avatar = head_fr.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX1_HEADER_FR_AVATAR_CLASS).first();
                src =  "http:"+head_fr_avatar.attr(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX1_HEADER_FR_AVATAR_SRC_ATTR);
            }
            articleInfo.setAuthor_image(src);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.updateUI.updateUI(null);
        }


        //帖子分类
        Elements aElements = head.getElementsByTag(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX1_HEADER_A_TAG);
        List<String> lists = new ArrayList<String>();
        for(Element element : aElements)
                lists.add(element.text());
        articleInfo.setArticle_class(lists);
        //帖子标题
        String title = null;
        if (head.getElementsByTag(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX1_HEADER_H1_TAG).first() == null)
        {
            title = "NULL";
        }
        else
        {
            title = head.getElementsByTag(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX1_HEADER_H1_TAG).first().text();
        }
        articleInfo.setTitle(title);

        //帖子信息
        String head_gray = null;
        if (head.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX1_HEADER_GRAY_CALSS).first() == null)
        {
            head_gray = "NULL";
        }
        else
        {
            head_gray = head.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX1_HEADER_GRAY_CALSS).first().text();
        }
        articleInfo.setInfo(head_gray);

        //帖子内容
        //帖子可能没有内容
        Elements eles = box1.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX1_CELL_CLASS);
        if (eles == null || eles.size() == 0)
        {
            articleInfo.setContent("没有内容");
        }
        else
        {
            String box1_cell = eles.first().text();
            articleInfo.setContent(box1_cell);
        }

        if (box2.text().equals(ArticleAndRepliesScheam.NO_REPLAY))
        {
            //没有回复
            System.out.println("No Replay");
            this.updateUI.updateUI(articleInfo);
            return;
        }
        else
        {
            List<ReplyInfo> replyInfos = new ArrayList<ReplyInfo>();

            Elements cells = box2.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX2_CELL_CLASS);
            //回复的基本信息
            Element cell1 = cells.first();

            articleInfo.setReply_base_info(cell1.text());

            if (cells.size() > 1)
            {
                for(int i=1;i<cells.size();i++)
                {
                    Element cellOther = cells.get(i);
                    replyInfos.add(getReplyInfo(cellOther));
                }
            }

            Element inner = box2.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX2_INNER_CLASS).first();
            replyInfos.add(getReplyInfo(inner));
            articleInfo.setReplyInfos(replyInfos);
        }
        this.updateUI.updateUI(articleInfo);
        return;
    }
    //解析文件
    private void resolveFile()
    {
        try {
            is = new FileInputStream(file);
            if (is == null)
                throw new IllegalArgumentException("无效的文件");
            resolveInputStream();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //解析流
    private void resolveInputStream()
    {
        try {
            readInputStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //解析url
    private void resolveUrl()
    {
        GetHtmlTask getHtmlTask = new GetHtmlTask();
        getHtmlTask.setDoAfter(this);
        getHtmlTask.execute(url);
    }

    private void readInputStream(InputStream instream) throws Exception
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[]  buffer = new byte[1204];
        int len = 0;
        while ((len = instream.read(buffer)) != -1)
        {
            outStream.write(buffer,0,len);
        }
        instream.close();
        this.htmlString = new String(outStream.toByteArray());
        resolveHtmlString();
    }

    private ReplyInfo getReplyInfo(Element cellOther)
    {
        ReplyInfo replyInfo = new ReplyInfo();
        String image_url = "http:"+cellOther.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX2_CELLOTHER_AVATAR_CLASS).first().
                attr(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX2_CELLOTHER_AVATAR_SRC_ATTR);
        replyInfo.setImage_url(image_url);
        String name = cellOther.getElementsByTag(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX2_CELLOTHER_STRONG_TAG).first().text();
        replyInfo.setReply_name(name);
        String time = cellOther.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX2_CELLOTHER_FADESAMLL_CLASS).first().text();
        replyInfo.setReply_time(time);
        String reply_content = cellOther.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX2_CELLOTHER_REPLYCONTENT_CLASS).first().text();
        replyInfo.setReply_content(reply_content);
        String no = cellOther.getElementsByClass(ArticleAndRepliesScheam.WRAPPER_MAIN_BOX2_CELLOTHER_NO_CLASS).first().text();
        replyInfo.setReply_number(no);
        return replyInfo;
    }


    @Override
    public void doAfter(String htmlString)
    {
        this.htmlString = htmlString;
        resolveHtmlString();
    }

    public void setUpdateUI(UpdateUI<ArticleInfo> updateUI)
    {
        this.updateUI = updateUI;
    }

}
