package com.ylqhust.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ylqhust.adapter.LatestListViewAdapter;
import com.ylqhust.contract.LatestScheam;
import com.ylqhust.v2ex.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalHistory extends Fragment {

    private List<LatestScheam> latestScheams;
    private ListView listView;
    public LocalHistory(List<LatestScheam> latestScheams) {
        this.latestScheams = latestScheams;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_history, container, false);
        listView = (ListView) view.findViewById(R.id.local_history);
        LatestListViewAdapter latestListViewAdapter = new LatestListViewAdapter(inflater,latestScheams);
        listView.setAdapter(latestListViewAdapter);
        return view;
    }

}
