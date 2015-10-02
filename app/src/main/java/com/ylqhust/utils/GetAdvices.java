package com.ylqhust.utils;

import com.ylqhust.contract.LatestScheam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 15/9/30.
 * 用来获取补全字符串
 */
public class GetAdvices
{
    private static GetAdvices getAdvices = null;
    private GetAdvices(){};
    private List<LatestScheam> latestScheams = null;
    public static GetAdvices getInstance()
    {
        if (getAdvices == null)
        {
            getAdvices = new GetAdvices();
        }
        return getAdvices;
    }

    public void setLatestScheams(List<LatestScheam> latestScheams)
    {
        this.latestScheams = latestScheams;
    }


    //获取自动补全的字符串
    public List<String> getAdvice(String input)
    {
        if (this.latestScheams == null)
        {
            return null;
        }
        List<String> advices = new ArrayList<String>();
        for(LatestScheam latestScheam : latestScheams)
        {
            if (latestScheam.getTitle().contains(input))
                advices.add(latestScheam.getTitle());
            if (latestScheam.getUser_name().contains(input))
                advices.add(latestScheam.getUser_name());
            if (latestScheam.getNode_name().contains(input))
                advices.add(latestScheam.getNode_name());
        }
        //删除重复的选项
        for(int i=0;i<advices.size();i++)
        {
            for(int j=i+1;j<advices.size();j++)
                if (advices.get(i).equals(advices.get(j)))
                {
                    advices.remove(j);
                    j--;
                }

        }
        return advices;
    }


    //通过用户的输入获取可匹配的本地数据
    public  List<LatestScheam> FilterByInput(String input)
    {
        List<LatestScheam> la = new ArrayList<LatestScheam>();
        if (this.latestScheams == null)
            return la;
        for(LatestScheam latestScheam : latestScheams)
        {
            if (latestScheam.getTitle().contains(input) ||
                    latestScheam.getUser_name().contains(input) ||
                    latestScheam.getNode_name().contains(input))
             la.add(latestScheam);
        }
        return la;
    }
}
