package com.example.marko.myholidays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by marko on 02/08/2016.
 */
public class CustomAdapter extends BaseAdapter {
    Context context;

    protected List<Holiday> holidayList;
    LayoutInflater inflater;

    public CustomAdapter(Context _context, List<Holiday> _holidayList) {
        this.holidayList = _holidayList;
        this.inflater = LayoutInflater.from(_context);
        this.context = _context;
    }

    private class ViewHolder {
        TextView rowTitleView;
        TextView rowPlaceView;
        TextView rowDateView;
        ImageView rowImageView;
    }

    public int getCount() {
        return holidayList.size();
    }

    @Override
    public Holiday getItem(int position) {
        return holidayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.row, parent, false);
            holder.rowTitleView = (TextView) convertView.findViewById(R.id.textViewTitle);
            holder.rowPlaceView = (TextView) convertView.findViewById(R.id.textViewPlace);
            holder.rowDateView = (TextView) convertView.findViewById(R.id.textViewDate);
            holder.rowImageView = (ImageView) convertView.findViewById(R.id.imageViewHoliday);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Holiday holiday = holidayList.get(position);
        holder.rowTitleView.setText(holiday.getTitle());
        holder.rowPlaceView.setText(holiday.getPlace());
        holder.rowDateView.setText(holiday.getDate());

//        if (holiday.getImage().equals("")) {
//            Bitmap image = BitmapFactory.decodeFile(holiday.getImage());
//            holder.rowImageView.setImageBitmap(image);
//        }

        return convertView;
    }
}
