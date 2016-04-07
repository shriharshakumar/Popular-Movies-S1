package com.example.sri_harsha.project1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sri Harsha on 4/7/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> urll;

    public ImageAdapter(Context c, List<String> urll) {
        mContext = c;
        this.urll = urll;
    }

    public int getCount() {
        return urll.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageview;
        if(convertView == null){
            imageview = new ImageView(mContext);
        }else{
            imageview = (ImageView) convertView;
        }
        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w185/" + urll.get(position))
                .into(imageview);
        return imageview;
    }


}
