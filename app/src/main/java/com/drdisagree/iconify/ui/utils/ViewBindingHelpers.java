package com.drdisagree.iconify.ui.utils;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Objects;

public class ViewBindingHelpers {

    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url.replace("http://", "https://")).apply(centerCropTransform()).transition(withCrossFade()).into(imageView);
    }

    public static void setDrawable(ImageView imageView, Drawable drawable) {
        Glide.with(imageView.getContext()).load(drawable).into(imageView);
    }

    public static void setDrawable(ViewGroup viewGroup, Drawable drawable) {
        Glide.with(viewGroup.getContext()).load(drawable).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
                viewGroup.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    public static void disableNestedScrolling(ViewPager2 viewPager) {
        RecyclerView recyclerView = null;

        for (int i = 0; i < viewPager.getChildCount(); i++) {
            if (viewPager.getChildAt(i) instanceof RecyclerView) {
                recyclerView = (RecyclerView) viewPager.getChildAt(i);
                break;
            }
        }

        if (recyclerView != null) {
            recyclerView.setNestedScrollingEnabled(false);
        }
    }

    public static void setHeader(Context context, CollapsingToolbarLayout collapsing_toolbar, Toolbar toolbar, int title) {
        collapsing_toolbar.setTitle(context.getResources().getString(title));
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) context).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}