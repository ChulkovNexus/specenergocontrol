package com.specenergocontrol.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.specenergocontrol.R;
import com.specenergocontrol.model.StreetEntity;

import java.util.List;

/**
 * Created by Комп on 08.07.2015.
 */
public class TasksStreetAdapter extends BaseExpandableListAdapter {


    private final Context context;
    private final List<StreetEntity> streets;
    private final int expandedItemColor;
    private final Drawable arrowUp;
    private final Drawable arrowDown;

    public TasksStreetAdapter(Context context, List<StreetEntity> streets) {
        this.context = context;
        this.streets = streets;

        expandedItemColor = context.getResources().getColor(R.color.building_item_color);

        arrowUp = context.getResources().getDrawable(R.drawable.arrow_up);
        arrowDown = context.getResources().getDrawable(R.drawable.arrow_down);

    }

    @Override
    public int getGroupCount() {
        return streets.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return streets.get(groupPosition).getChildEntityArray().size();
    }

    @Override
    public StreetEntity getGroup(int groupPosition) {
        return streets.get(groupPosition);
    }

    @Override
    public StreetEntity getChild(int groupPosition, int childPosition) {
        return streets.get(groupPosition).getChildEntityArray().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder groupHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_street_item, parent, false);
            groupHolder = new ViewHolder(convertView, false);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolder) convertView.getTag();
        }

        groupHolder.populateView(isExpanded, getGroup(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder groupHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_street_item, parent, false);
            groupHolder = new ViewHolder(convertView, true);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolder) convertView.getTag();
        }

        groupHolder.populateView(false, getChild(groupPosition, childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class ViewHolder {

        private final ImageView confirmedIcon;
        private final TextView textvew;
        private final ImageView expandedIcon;

        public ViewHolder(View view, boolean isChild) {
            confirmedIcon = (ImageView) view.findViewById(R.id.street_item_confirmed);
            textvew = (TextView) view.findViewById(R.id.street_item_text);
            expandedIcon = (ImageView) view.findViewById(R.id.street_item_expanded_icon);

            if (isChild) {
                ((CardView) view.findViewById(R.id.street_item_card)).setCardBackgroundColor(expandedItemColor);
            }
        }

        public void populateView(boolean isExpanded, StreetEntity entity) {
            if (isExpanded) {
                expandedIcon.setImageDrawable(arrowUp);
            } else {
                expandedIcon.setImageDrawable(arrowDown);
            }
            textvew.setText(entity.getEntityTitle());
            if (entity.isComplited()) {
                confirmedIcon.setVisibility(View.VISIBLE);
            } else {
                confirmedIcon.setVisibility(View.INVISIBLE);
            }
        }
    }
}
