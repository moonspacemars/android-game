/*
This is custom adapter for displaying on list view.
 */

package com.example.ballongame;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LVAdapter extends BaseAdapter {
    public ArrayList<Record> recordGroup;
    Activity activity;
    public LVAdapter(Activity activity, ArrayList<Record> group){
        super();
        this.activity=activity;
        this.recordGroup=group;
    }

    @Override
    public int getCount() {
        return recordGroup.size();
    }

    @Override
    public Object getItem(int position) {
        return recordGroup.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    private class ViewHolder{
        TextView rank_no;
        TextView rank_name;
        TextView rank_score;
        TextView rank_date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView==null){
            convertView = inflater.inflate(R.layout.row_lv, null);
            holder = new ViewHolder();
            holder.rank_no = (TextView)convertView.findViewById(R.id.tv_rNo);
            holder.rank_name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.rank_score = (TextView)convertView.findViewById(R.id.tv_score);
            holder.rank_date = (TextView)convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        Record record = recordGroup.get(position);

        holder.rank_no.setText(record.getRank_no().toString());
        holder.rank_name.setText(record.getPlayer_name().toString());
        holder.rank_score.setText(record.getScore().toString());
        holder.rank_date.setText(record.getDate().toString());
        return  convertView;
    }

}
