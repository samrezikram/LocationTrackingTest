package com.crescentcatcher.locationtracking;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by samrezikram on 11/13/17.
 */

public class RoutesAdapter extends ArrayAdapter<Location> {

    private ArrayList<Location> dataSet;
    Context mContext;
    View parent;

    // View lookup cache
    private static class ViewHolder {
        TextView txtRouteName;
        TextView txtRouteData1;
        TextView btnRouteData2;
    }

    public RoutesAdapter(ArrayList<Location> data, Context context, View parentView) {
        super(context, R.layout.list_routes, data);
        this.dataSet = data;
        this.mContext=context;
        this.parent = parentView;

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Nullable
    @Override
    public Location getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        Location dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_routes, parent, false);
            viewHolder.txtRouteName = (TextView) convertView.findViewById(R.id.routes_name);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtRouteName.setText(dataModel.getRoutename());
         // Return the completed view to render on screen
        return convertView;

    }
}
