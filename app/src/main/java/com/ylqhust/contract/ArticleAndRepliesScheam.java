package com.ylqhust.contract;

/**
 * Created by apple on 15/10/1.
 */
public class ArticleAndRepliesScheam
{
    public static final String WRAPPER_ID = "Wrapper";
    public static final String WRAPPER_MAIN_BOX_CLASS = "box";
    //box1 主要是楼主帖子内容
    public static final String WRAPPER_MAIN_BOX1_HEADER_CLASS = "header";
    public static final String WRAPPER_MAIN_BOX1_HEADER_FR_CLASS = "fr";
    public static final String WRAPPER_MAIN_BOX1_HEADER_FR_AVATAR_CLASS = "avatar";
    public static final String WRAPPER_MAIN_BOX1_HEADER_FR_AVATAR_SRC_ATTR = "src";//楼主头像图片路径
    public static final String WRAPPER_MAIN_BOX1_HEADER_A_TAG = "a"; //帖子分类
    public static final String WRAPPER_MAIN_BOX1_HEADER_H1_TAG = "h1";//标题
    public static final String WRAPPER_MAIN_BOX1_HEADER_GRAY_CALSS = "gray";//帖子信息 作者.时间.点击次数
    public static final String WRAPPER_MAIN_BOX1_CELL_CLASS = "cell";//帖子内容

    public static final String NO_REPLAY = "目前尚无回复";
    //如果没有回复，那么BOX2节点下就一个inner class
    public static final String WRAPPER_MAIN_BOX2_INNER_CLASS = "inner";

    //如果有回复,那么BOX2节点至少会有一个cell class表示帖子的标签，至少会有一个inner class 表示最后一个发表的回复，其余的回复则用cell class表示
    public static final String WRAPPER_MAIN_BOX2_CELL_CLASS = "cell";

    //使用下面的这些时，必须检查box2_cell的总数是否为1，如果为1的话，不能使用
    public static final String WRAPPER_MAIN_BOX2_CELLOTHER_AVATAR_CLASS = "avatar";
    public static final String WRAPPER_MAIN_BOX2_CELLOTHER_AVATAR_SRC_ATTR = "src";//回帖者得图片链接
    public static final String WRAPPER_MAIN_BOX2_CELLOTHER_STRONG_TAG = "strong";//回帖者名字
    public static final String WRAPPER_MAIN_BOX2_CELLOTHER_FADESAMLL_CLASS = "fade";//回帖时间
    public static final String WRAPPER_MAIN_BOX2_CELLOTHER_REPLYCONTENT_CLASS = "reply_content";//回帖内容
    public static final String WRAPPER_MAIN_BOX2_CELLOTHER_NO_CLASS = "no";//回帖楼层
}
