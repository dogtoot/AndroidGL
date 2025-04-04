package com.cj186.opengltest;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.cj186.androidglkit.*;


public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glView = new GLSurfaceView(this);           // Allocate a GLSurfaceView
        glView.setRenderer(new GLRenderManager(this));
        setContentView(glView);
    }
}