package com.example.rural_essential.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rural_essential.R;
import com.example.rural_essential.ui.adapters.HelpExpandableListAdapter;
import com.example.rural_essential.ui.model.Help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpFragment extends Fragment {
    private ExpandableListView helpListView;
    private ExpandableListAdapter helpListViewAdapter;
    private List<String> helpListTitle;
    private HashMap<String, List<Help>> helpListDetail;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_help, container, false);
        //inflate views
        helpListView = root.findViewById(R.id.help_expandable_list);
        helpListDetail = HelpExpandableListDataPump.getData();
        helpListTitle = new ArrayList<String>(helpListDetail.keySet());
        helpListViewAdapter = new HelpExpandableListAdapter(getContext(), helpListTitle, helpListDetail);
        helpListView.setAdapter(helpListViewAdapter);

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
