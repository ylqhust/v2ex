package com.ylqhust.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ylqhust.v2ex.R;

import java.util.List;

/**
 * Created by apple on 15/9/30.
 */
public class AutoCompleteAdapter extends BaseAdapter {
    private List<String> lists;
    private LayoutInflater inflater;
    private AutoCompleteText autoCompleteText;


    public AutoCompleteAdapter(List<String> lists,LayoutInflater inflater)
    {
        this.lists = lists;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return lists==null?0:lists.size();
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (this.inflater == null || this.lists == null)
            return null;
        View view = inflater.inflate(R.layout.text,null);
        TextView textView = (TextView) view.findViewById(R.id.auto_complete_text);
        textView.setText(lists.get(position));
        if (this.autoCompleteText == null)
        {
            throw new IllegalStateException("AutoComplete未实现");
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteText.complete(lists.get(position));
            }
        });
        return view;
    }


    public interface AutoCompleteText {
        public void complete(String string);
    }

    public void setAutoCompleteText(AutoCompleteText autoCompleteText)
    {
        this.autoCompleteText = autoCompleteText;
    }

}
