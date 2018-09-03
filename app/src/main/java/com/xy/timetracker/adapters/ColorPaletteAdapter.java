package com.xy.timetracker.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.xy.timetracker.R;
import com.xy.timetracker.util.Constants;
import com.xy.timetracker.view.CheckablePalette;

public class ColorPaletteAdapter extends BaseAdapter {
    private Context mContext;

    public ColorPaletteAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mColors.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckablePalette buttonView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            buttonView = new CheckablePalette(mContext, mColors[position]);
            int pixWidth = mContext.getResources().getDimensionPixelSize(R.dimen.color_palette_dimen);
            int pixHeight = (int) (pixWidth * 0.6);
            buttonView.setLayoutParams(new GridView.LayoutParams(pixWidth, pixHeight));
        } else {
            buttonView = (CheckablePalette) convertView;
        }


        return buttonView;
    }

    // references to our images
    private int[] mColors = Constants.MATERIAL_COLORS_PRIMARY;

}