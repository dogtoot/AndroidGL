package com.cj186.opengltest;

import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import com.cj186.androidglkit.*;


public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int[] images = {com.cj186.androidglkit.R.drawable.die_1,
                com.cj186.androidglkit.R.drawable.die_2,
                com.cj186.androidglkit.R.drawable.die_3,
                com.cj186.androidglkit.R.drawable.die_4,
                com.cj186.androidglkit.R.drawable.die_5,
                com.cj186.androidglkit.R.drawable.die_6};
        glView = findViewById(R.id.surface_view);

        GLRenderManager renderer = new GLRenderManager(this, "DFF2BF", images, 0.75f);
        glView.setRenderer(renderer);

        renderer.performRotation(180, 0);
    }
}