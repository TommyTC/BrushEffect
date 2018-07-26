package com.github.tommytc.brusheffect;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tommytc.lib.brusheffect.BrushEffectLayout;
import com.github.tommytc.lib.brusheffect.BrushEffectLayoutKt;

/**
 * //    ╔╦╗╔═╗╔╦╗╔╦╗╦ ╦  2018/7/26
 * //     ║ ║ ║║║║║║║╚╦╝  com.android.tommy@gmail.com
 * //     ╩ ╚═╝╩ ╩╩ ╩ ╩   ═══════════════════════════
 */
public class DemoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    public void onClick(final View view){
        final BrushEffectLayout parent = (BrushEffectLayout) view.getParent();
        parent.setInterpolator(new AccelerateDecelerateInterpolator());
        parent.setEndColor(0xFFDDDD00);
        parent.setStartColor(Color.YELLOW);
        parent.setDuration(1000);
        parent.setOrientation(BrushEffectLayoutKt.ORIENTATION_HORIZONTAL);
//        parent.setReverse(true);
        parent.setListener(new BrushEffectLayout.Listener() {
            @Override
            public void onStart() {
                Toast.makeText(DemoActivity.this,"Start",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCover() {
                Toast.makeText(DemoActivity.this,"Cover",Toast.LENGTH_SHORT).show();
//                view.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                        if (view instanceof TextView)
                            ((TextView) view).setText(String.valueOf(System.currentTimeMillis()));
                        parent.hide();
//                    }
//                },100);
            }

            @Override
            public void onFinish() {
                Toast.makeText(DemoActivity.this,"Finish",Toast.LENGTH_SHORT).show();
            }
        });
        parent.brush();

    }
}
