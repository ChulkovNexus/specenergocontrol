package com.specenergocontrol.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.specenergocontrol.R;
import com.specenergocontrol.model.StreetEntity;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by Комп on 29.11.2015.
 */
public class TasksAppartmentsAdapter extends ArrayAdapter {

    private final Context context;
    private final RealmList<StreetEntity> entities;
    private final int expandedItemColor;

    public TasksAppartmentsAdapter(Context context, RealmList<StreetEntity> entities) {
        super(context, R.layout.layout_street_item);
        this.context = context;
        this.entities = entities;
        expandedItemColor = context.getResources().getColor(R.color.app_blue_color);
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public StreetEntity getItem(int position) {
        return entities.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder groupHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_street_item, parent, false);
            groupHolder = new ViewHolder(convertView, false);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolder) convertView.getTag();
        }

        groupHolder.populateView(getItem(position));

        return convertView;
    }

    private class ViewHolder {

        private final ImageView confirmedIcon;
        private final TextView textvew;
        private final ImageView expandedIcon;

        public ViewHolder(View view, boolean isChild) {
            confirmedIcon = (ImageView) view.findViewById(R.id.street_item_confirmed);
            textvew = (TextView) view.findViewById(R.id.street_item_text);
            expandedIcon = (ImageView) view.findViewById(R.id.street_item_expanded_icon);
            expandedIcon.setImageResource(R.drawable.arrow_right);

            ((CardView) view.findViewById(R.id.street_item_card)).setCardBackgroundColor(expandedItemColor);
            ((TextView) view.findViewById(R.id.street_item_text)).setTextColor(Color.WHITE);
        }

        public void populateView(StreetEntity entity) {
                textvew.setText(context.getString(R.string.appartments_number, entity.getEntityTitle(), entity.getAccount()));
//            if (entity.isComplited()) {
//                confirmedIcon.setVisibility(View.VISIBLE);
//            } else {
//                confirmedIcon.setVisibility(View.INVISIBLE);
//            }

        }
    }
}
