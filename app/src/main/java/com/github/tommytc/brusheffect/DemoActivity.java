package com.github.tommytc.brusheffect;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
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

    public void onClick1(View view){
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
}
