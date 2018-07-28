package com.github.tommytc.brusheffect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class Page extends Fragment {
    private int backgroundResId = 0;
    private float scale = 1f;
    private float rotation = 0f;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(backgroundResId);
        view.setRotation(rotation);
        view.setScaleX(scale);
        view.setScaleY(scale);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(container.getMeasuredWidth()/2,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        view.setLayoutParams(lp);

        FrameLayout contentView = new FrameLayout(container.getContext());
        contentView.addView(view);
        return contentView;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public void setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
