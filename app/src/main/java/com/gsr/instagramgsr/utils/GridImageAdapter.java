package com.gsr.instagramgsr.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gsr.instagramgsr.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class GridImageAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String append;
    private ArrayList<String> imageUrls;

    public GridImageAdapter(@NonNull Context context, int layoutResource, String append, ArrayList<String> imageUrls) {
        super(context, layoutResource, imageUrls);
        this.mContext = context;
        // get the LayoutInflater to provide the layoutResource to convertView
        this.mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = layoutResource;
        this.append = append;
        this.imageUrls = imageUrls;
    }

    private static class ViewHolder{
        SquareImageView image;
        ProgressBar mProgressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        /**
         * Viewholder build pattern (Similar to recyclerview)
         */
        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.image  = (SquareImageView) convertView.findViewById(R.id.gridImage);
            holder.mProgressBar = convertView.findViewById(R.id.gridImageProgressBar);

            convertView.setTag(holder);
        }else{

            holder = (ViewHolder) convertView.getTag();
        }

        String imgURL = getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgURL, holder.image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(holder.mProgressBar != null ){
                    holder.mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(holder.mProgressBar != null ){
                    holder.mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(holder.mProgressBar != null ){
                    holder.mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(holder.mProgressBar != null ){
                    holder.mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        return convertView;
    }
}
