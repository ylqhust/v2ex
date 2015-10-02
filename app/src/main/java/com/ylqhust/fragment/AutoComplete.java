package com.ylqhust.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ylqhust.adapter.AutoCompleteAdapter;
import com.ylqhust.v2ex.R;

import java.util.ArrayList;
import java.util.List;


public class AutoComplete extends Fragment implements AutoCompleteAdapter.AutoCompleteText{

    private List<String> complete = null;
    private AutoCompleteText2 autoCompleteText2;

    public AutoComplete(List<String> complete)
    {
        this.complete = complete;
        if (this.complete == null)
            this.complete = new ArrayList<String>();
        if (this.complete.size() == 0)
        {
            this.complete.add("No Result");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.auto_complete,container,false);
        ListView listView = (ListView) view.findViewById(R.id.auto_complete_listview);
        AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(complete,inflater);
        autoCompleteAdapter.setAutoCompleteText(this);
        listView.setAdapter(autoCompleteAdapter);
        return view;
    }


    @Override
    public void complete(String string)
    {
        if (this.autoCompleteText2 == null)
        {
            throw new IllegalStateException("AutoCompleteText2未实现");
        }
        this.autoCompleteText2.complete(string);
    }

    public interface AutoCompleteText2{
        public void complete(String string);
    }

    public void setAutoCompleteText2(AutoCompleteText2 autoCompleteText2)
    {
        this.autoCompleteText2 = autoCompleteText2;
    }
}
