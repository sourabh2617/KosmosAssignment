package com.kosmos.articles.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.kosmos.articles.R;
import com.squareup.picasso.Picasso;

public class DataBindAdapter {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_article)
                .into(view);
    }
}
