package kz.telecom.happydrive.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kz.telecom.happydrive.R;
import kz.telecom.happydrive.data.BackgroundCategory;
import kz.telecom.happydrive.data.BackgroundPhotos;

/**
 * Created by szholdiyarov on 5/13/16.
 */
public class BackgroundGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<BackgroundPhotos> list;

    public BackgroundGridViewAdapter(Context c, List<BackgroundPhotos> list) {
        mContext = c;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(list.get(position).photo).placeholder(R.drawable.progress_animation).into(imageView);

        return imageView;
    }

}