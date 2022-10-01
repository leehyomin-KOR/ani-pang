package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Rank;

import java.util.ArrayList;

public class RankAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Rank> rank_data_list;

    public RankAdapter(Context context, ArrayList<Rank> rank_list) {
        this.mContext = context;
        this.rank_data_list = rank_list;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return rank_data_list.size();
    }

    @Override
    public Object getItem(int index) {
        return rank_data_list.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rank_list_view = mLayoutInflater.inflate(R.layout.rank_listview_res, null);

        TextView rank_NO = (TextView)rank_list_view.findViewById(R.id.rank_no);
        TextView rank_name = (TextView)rank_list_view.findViewById(R.id.rank_name);
        TextView rank_score = (TextView)rank_list_view.findViewById(R.id.rank_score);
        TextView rank_clear_date = (TextView)rank_list_view.findViewById(R.id.rank_clear_date);

        rank_NO.setText((Integer.toString(i+1)+"위"));
        rank_name.setText(rank_data_list.get(i).getPlayer_name());
        rank_score.setText(Integer.toString(rank_data_list.get(i).getScore()));
        rank_clear_date.setText(rank_data_list.get(i).getClear_date());


        return rank_list_view;
    }

}
