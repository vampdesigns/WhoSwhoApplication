
package com.test.whoswhoapp.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.test.whoswhoapp.R;
import com.test.whoswhoapp.common.EmployeeObject;
import com.test.whoswhoapp.utils.VolleyInstance;

public class EmployeeAdapter extends ArrayAdapter<EmployeeObject> {

    private final int mResource;
    private final LayoutInflater mInflater;

    public EmployeeAdapter(Activity activity, int resource,
            List<EmployeeObject> objects) {
        super(activity, resource, objects);
        mResource = resource;
        mInflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EmployeeObject item = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(mResource, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.ivThumb = (NetworkImageView) convertView.findViewById(R.id.iv_thumb);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvBio = (TextView) convertView.findViewById(R.id.tv_bio);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(item.getName());
        holder.tvTitle.setText(item.getTitle());
        holder.tvBio.setText(item.getBio());

        // That's the volley way for image downloading.
        // images are cached for this adapter with BitmapLruCache
        ImageLoader imageLoader = VolleyInstance.getImageLoader();
        holder.ivThumb.setImageUrl(item.getImgLink(), imageLoader);
        holder.ivThumb.setErrorImageResId(R.drawable.thumb);
        holder.ivThumb.setDefaultImageResId(R.drawable.thumb);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public static class ViewHolder {
        public TextView tvName;
        public NetworkImageView ivThumb;
        public TextView tvTitle;
        public TextView tvBio;
    }
}
