package com.cj186.opengltest;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import com.cj186.androidglkit.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int[] images = {R.drawable.die_1,
                R.drawable.die_2,
                R.drawable.die_3,
                R.drawable.die_4,
                R.drawable.die_5,
                R.drawable.die_6};
        GLSurfaceView glView = findViewById(R.id.surface_view);

        GLRenderManager renderer = new GLRenderManager(this, "FFF2BF", images, 0.75f);
        glView.setRenderer(renderer);

        //renderer.performRotation(720, 720, 5000);

        renderer.performRotation(
                180,
                180,
                500,
                ValueAnimator.RESTART,
                ValueAnimator.INFINITE,
                new LinearInterpolator()
        );

        /*ObjectAnimator rotateYAnimator = ObjectAnimator.ofFloat(renderer, "rotationY", 0, 720);
        rotateYAnimator.setDuration(5000);
        rotateYAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotateYAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateYAnimator.start();*/
    }
}