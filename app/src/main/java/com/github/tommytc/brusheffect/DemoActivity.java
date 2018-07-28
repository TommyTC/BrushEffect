package com.github.tommytc.brusheffect;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tommytc.lib.brusheffect.BrushEffectLayout;

/**
 * //    ╔╦╗╔═╗╔╦╗╔╦╗╦ ╦  2018/7/26
 * //     ║ ║ ║║║║║║║╚╦╝  com.android.tommy@gmail.com
 * //     ╩ ╚═╝╩ ╩╩ ╩ ╩   ═══════════════════════════
 */
public class DemoActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final BrushEffectLayout layoutTitle = findViewById(R.id.layoutTitle);
        final TextView tvTitle = findViewById(R.id.tvTitle);
        final BrushEffectLayout layoutDescription = findViewById(R.id.layoutDescription);
        final TextView tvDescription = findViewById(R.id.tvDescription);
        final BrushEffectLayout layoutImage = findViewById(R.id.layoutImage);
        final TextView tvImageDescription = findViewById(R.id.tvImageDescription);
        final ImageView ivImage = findViewById(R.id.ivImage);
        final ViewPager viewpager = findViewById(R.id.viewpager);


        final Data[] data = new Data[2];
        Data item = new Data();
        item.title = "Java";
        item.description = "Java is a general-purpose computer-programming language that is concurrent, class-based, object-oriented,[15] and specifically designed to have as few implementation dependencies as possible";
        item.imageResId = R.mipmap.img_james;
        item.imageDescription = "James Gosling, the creator of Java";
        data[0] = item;
        item = new Data();
        item.title = "Golang";
        item.description = "Go is an open source programming language that makes it easy to build simple, reliable, and efficient software.";
        item.imageResId = R.mipmap.img_google;
        item.imageDescription = "created at Google in 2009 by Robert Griesemer, Rob Pike, and Ken Thompson";
        data[1] = item;

        layoutTitle.setListener(new BrushEffectLayout.Listener() {
            @Override
            public void onStart() {
                layoutDescription.setStartColor(layoutTitle.getStartColor());
                layoutDescription.setEndColor(layoutTitle.getEndColor());
                layoutDescription.setReverse(layoutTitle.isReverse());
                layoutDescription.brush(100);
            }

            @Override
            public void onCover() {
                int position = viewpager.getCurrentItem();
                tvTitle.setText(data[position].title);
                tvTitle.setTextColor(layoutTitle.getEndColor());
                layoutTitle.hide();
            }

            @Override
            public void onFinish() {

            }
        });

        layoutDescription.setListener(new BrushEffectLayout.Listener() {
            @Override
            public void onStart() {
                layoutImage.setStartColor(layoutTitle.getStartColor());
                layoutImage.setEndColor(layoutTitle.getEndColor());
                layoutImage.setReverse(layoutTitle.isReverse());
                layoutImage.brush(100);
            }

            @Override
            public void onCover() {
                int position = viewpager.getCurrentItem();
                tvDescription.setText(data[position].description);
                tvDescription.setTextColor(layoutTitle.getEndColor());
                layoutDescription.hide();
            }

            @Override
            public void onFinish() {

            }
        });

        layoutImage.setListener(new BrushEffectLayout.Listener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCover() {
                int position = viewpager.getCurrentItem();
                ivImage.setImageResource(data[position].imageResId);
                tvImageDescription.setText(data[position].imageDescription);
                tvImageDescription.setBackgroundColor(layoutTitle.getEndColor());
                layoutImage.hide();
            }

            @Override
            public void onFinish() {

            }
        });

        final Page[] pagers = new Page[2];
        Page page = new Page();
        page = new Page();
        page.setBackgroundResId(R.mipmap.img_coffee);
        page.setRotation(45f);
        page.setScale(2f);
        pagers[0] = page;
        page = new Page();
        page.setBackgroundResId(R.mipmap.img_gogher);
        page.setScale(4f);
        pagers[1] = page;
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return pagers[position];
            }

            @Override
            public int getCount() {
                return pagers.length;
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    layoutTitle.setStartColor(0xFF39788b);
                    layoutTitle.setEndColor(0xFF3F51B5);
                    layoutTitle.setReverse(false);
                } else {
                    layoutTitle.setStartColor(0xFF3F51B5);
                    layoutTitle.setEndColor(0xFF39788b);
                    layoutTitle.setReverse(true);
                }
                layoutTitle.brush();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onClick1(View view) {
        final BrushEffectLayout parent = (BrushEffectLayout) view.getParent();
        parent.setStartColor(Color.BLACK);
        parent.setEndColor(Color.WHITE);
        parent.setReverse(false);
        parent.setListener(new BrushEffectLayout.Listener() {
            @Override
            public void onStart() {
                Toast.makeText(DemoActivity.this, "Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCover() {
                Toast.makeText(DemoActivity.this, "Cover", Toast.LENGTH_SHORT).show();
                parent.hide();
            }

            @Override
            public void onFinish() {
                Toast.makeText(DemoActivity.this, "Finish", Toast.LENGTH_SHORT).show();
            }
        });
        parent.brush();
    }

    public void onClick(final View view) {
        final BrushEffectLayout parent = (BrushEffectLayout) view.getParent();
        parent.setListener(new BrushEffectLayout.Listener() {
            @Override
            public void onStart() {
                Toast.makeText(DemoActivity.this, "Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCover() {
                Toast.makeText(DemoActivity.this, "Cover", Toast.LENGTH_SHORT).show();
                parent.hide();
            }

            @Override
            public void onFinish() {
                Toast.makeText(DemoActivity.this, "Finish", Toast.LENGTH_SHORT).show();
            }
        });
        parent.brush();

    }

    class Data {
        public String title;
        public String description;
        public int imageResId;
        public String imageDescription;
    }
}
