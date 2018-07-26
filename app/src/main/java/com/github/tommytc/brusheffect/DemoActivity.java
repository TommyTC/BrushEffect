package com.github.tommytc.brusheffect;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

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
        parent.setStartColor(Color.CYAN);
        parent.setEndColor(Color.MAGENTA);
        parent.brush();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view instanceof TextView)
                    ((TextView) view).setText(String.valueOf(System.currentTimeMillis()));
                parent.hide();
            }
        },1000);
    }
}
