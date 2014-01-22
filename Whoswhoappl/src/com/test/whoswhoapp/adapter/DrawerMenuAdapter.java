
package com.test.whoswhoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.whoswhoapp.R;


/**
 * @author MentorMate 
 */
/**
 * An Adapter that populates Navigation Drawer menu items.
 */
public class DrawerMenuAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;

    public DrawerMenuAdapter(Context context, String[] objects) {
        super(context, R.layout.drawer_list_item, android.R.id.text1, objects);
        mInflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        TextView txtLabel = null;
        v = mInflater.inflate(R.layout.drawer_list_item, parent, false);
        v.findViewById(R.id.drawer_item_header).setVisibility(View.GONE);
        txtLabel = (TextView) v.findViewById(android.R.id.text1);
        txtLabel.setText(getItem(position));
        txtLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        return v;
    }

}
