package com.example.rural_essential.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rural_essential.R;
import com.example.rural_essential.ui.model.Help;

import java.util.HashMap;
import java.util.List;

public class HelpExpandableListAdapter extends BaseExpandableListAdapter {

private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<Help>> expandableListDetail;

    public HelpExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<Help>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Help getChild(int listPosition, int expandedListPosition) {
        //get child information
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Help expandedListText = getChild(listPosition, expandedListPosition);
        // if no information
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.help_screen_list_item, null);
        }
        //Set up Text View and Image View of child
        TextView expandedListTextView = convertView
                .findViewById(R.id.help_expandable_list_string);
        ImageView expandedListImageView = convertView.findViewById(R.id.help_expandable_list_image);
        if(expandedListText.getImageID() != -1){
            expandedListImageView.setImageResource(expandedListText.getImageID());
        }
        else {
            expandedListImageView.setImageResource(0);
        }
        expandedListTextView.setText(expandedListText.getDescription());
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        //set up group title
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.help_screen_list_group, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.help_list_title);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }



}
