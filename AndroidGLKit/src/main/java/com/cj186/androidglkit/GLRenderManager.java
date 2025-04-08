package com.cj186.androidglkit;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.animation.Interpolator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderManager implements GLSurfaceView.Renderer{

    private final Cube cube;

    float[] backgroundColorArray;
    String backgroundColorString;

    private float rotationX = 0f;  // X-axis rotation
    private float rotationY = 0f;  // Y-axis rotation
    private float rotationZ = 0f;  // Z-axis rotation

    private boolean renderCube = true;

    public GLRenderManager(Context ctx, String backgroundColor, int[] images, float size) {
        backgroundColorString = backgroundColor;
        cube = new Cube(ctx, images, size);
    }

    public void performRotation(int X, int Y, int durationInMs, int repeatMode, int repeatCount){
        // Set up two object animators for the X and Y axis.
        // Degrees and speed are passed through the method.
        ObjectAnimator rotateXAnimator = ObjectAnimator.ofFloat(this, "rotationX", rotationX, Y);
        rotateXAnimator.setDuration(durationInMs);
        rotateXAnimator.setRepeatMode(repeatMode);
        rotateXAnimator.setRepeatCount(repeatCount);
        rotateXAnimator.start();

        ObjectAnimator rotateYAnimator = ObjectAnimator.ofFloat(this, "rotationY", rotationY, X);
        rotateYAnimator.setDuration(durationInMs);
        rotateYAnimator.setRepeatMode(repeatMode);
        rotateYAnimator.setRepeatCount(repeatCount);
        rotateYAnimator.start();
    }

    public void performRotation(int X, int Y, int durationInMs){
        // Set up two object animators for the X and Y axis.
        // Degrees and speed are passed through the method.
        ObjectAnimator rotateXAnimator = ObjectAnimator.ofFloat(this, "rotationX", rotationX, Y);
        rotateXAnimator.setDuration(durationInMs);
        rotateXAnimator.start();

        ObjectAnimator rotateYAnimator = ObjectAnimator.ofFloat(this, "rotationY", rotationY, X);
        rotateYAnimator.setDuration(durationInMs);
        rotateYAnimator.start();
    }

    public void performRotation(int X, int Y, int durationInMs, int repeatMode, int repeatCount, Interpolator interpolator){
        // Set up two object animators for the X and Y axis.
        // Degrees and speed are passed through the method.
        ObjectAnimator rotateXAnimator = ObjectAnimator.ofFloat(this, "rotationX", rotationX, Y);
        rotateXAnimator.setDuration(durationInMs);
        rotateXAnimator.setInterpolator(interpolator);
        rotateXAnimator.setRepeatMode(repeatMode);
        rotateXAnimator.setRepeatCount(repeatCount);
        rotateXAnimator.start();

        ObjectAnimator rotateYAnimator = ObjectAnimator.ofFloat(this, "rotationY", rotationY, X);
        rotateYAnimator.setDuration(durationInMs);
        rotateYAnimator.setInterpolator(interpolator);
        rotateYAnimator.setRepeatMode(repeatMode);
        rotateYAnimator.setRepeatCount(repeatCount);
        rotateYAnimator.start();
    }

    public void performRotation(int X, int Y, int durationInMs, Interpolator interpolator){
        // Set up two object animators for the X and Y axis.
        // Degrees and speed are passed through the method.
        ObjectAnimator rotateXAnimator = ObjectAnimator.ofFloat(this, "rotationX", rotationX, Y);
        rotateXAnimator.setDuration(durationInMs);
        rotateXAnimator.setInterpolator(interpolator);
        rotateXAnimator.start();

        ObjectAnimator rotateYAnimator = ObjectAnimator.ofFloat(this, "rotationY", rotationY, X);
        rotateYAnimator.setDuration(durationInMs);
        rotateYAnimator.setInterpolator(interpolator);
        rotateYAnimator.start();
    }

    // Setters so that our ObjectAnimators work.
    private void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    private void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    private void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    private static float[] getRGB(String rgb) {
        // Parser for string colors like "FFFFFF" so that we don't have to figure out the
        // numerical values in an activity.
        int r = Integer.parseInt(rgb.substring(0, 2), 16); // 16 for hex
        int g = Integer.parseInt(rgb.substring(2, 4), 16); // 16 for hex
        int b = Integer.parseInt(rgb.substring(4, 6), 16); // 16 for hex
        return new float[] {r, g, b};
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        backgroundColorArray = getRGB(backgroundColorString);
        // Convert colors to percentages of 255.
        backgroundColorArray[0] /= 255.0F;
        backgroundColorArray[1] /= 255.0F;
        backgroundColorArray[2] /= 255.0F;
        // Set the background color.
        gl.glClearColor(backgroundColorArray[0], backgroundColorArray[1], backgroundColorArray[2], 1);
        // Set depth's clear-value to farthest.
        gl.glClearDepthf(1.0f);
        // Enables depth-buffer for hidden surface removal.
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // The type of depth testing to do.
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        // Enable smooth shading of color.
        gl.glShadeModel(GL10.GL_SMOOTH);

        // Load textures.
        cube.loadTexture(gl);
        // Enable textures.
        gl.glEnable(GL10.GL_TEXTURE_2D);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;

        // Set the viewport (display area) to fill its parent view.
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection.
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        // Select model-view matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if(renderCube){
            // Clear color and depth buffers
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            // ----- Render the Cube -----
            gl.glLoadIdentity();
            gl.glTranslatef(0.0f, 0.0f, -6.0f);

            // Perform the rotations as indicated by performRotation.
            gl.glRotatef(rotationX, 1f, 0f, 0f);
            gl.glRotatef(rotationY, 0f, 1f, 0f);
            gl.glRotatef(rotationZ, 0f, 0f, 1f);
            cube.draw(gl);
        }
    }

    public void setRenderCube(boolean renderCube){
        this.renderCube = renderCube;
    }
}
