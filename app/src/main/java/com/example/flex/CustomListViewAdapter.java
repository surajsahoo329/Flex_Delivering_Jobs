package com.example.flex;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomListViewAdapter extends ArrayAdapter<Company> {

    private Context context;

    CustomListViewAdapter(Context context, int resourceId,
                          List<Company> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtCompany;
        TextView txtTimings;
        TextView txtAddress;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        Company company =  getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_slot, null);
            holder = new ViewHolder();
            holder.txtCompany = (TextView) convertView.findViewById(R.id.tvCompany);
            holder.txtTimings = (TextView) convertView.findViewById(R.id.tvTimings);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.imageView = (ImageView) convertView.findViewById(R.id.ivImage);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        assert company != null;
        holder.txtCompany.setText(company.getCompany());
        holder.txtTimings.setText(company.getTimings());
        holder.txtAddress.setText(company.getAddress());
        holder.imageView.setImageResource(company.getImageId());

        return convertView;
    }
}
