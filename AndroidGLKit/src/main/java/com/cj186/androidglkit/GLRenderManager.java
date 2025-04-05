package com.cj186.androidglkit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class GLRenderManager implements GLSurfaceView.Renderer{

    private Cube cube;

    float[] backgroundColorArray;
    String backgroundColorString;

    private float rotationX = 0f;  // X-axis rotation
    private float rotationY = 0f;  // Y-axis rotation
    private float rotationZ = 0f;  // Z-axis rotation
    // Constructor
    public GLRenderManager(Context ctx, String backgroundColor, int[] images, float size) {
        backgroundColorString = backgroundColor;
        cube = new Cube(ctx, images, size);
    }

    public void performRotation(int X, int Y, int durationInMs){
        // Setup ObjectAnimator to animate rotations
        ObjectAnimator resetX = ObjectAnimator.ofFloat(this, "rotationX", 0f, 0);
        resetX.setDuration(durationInMs); // Duration in milliseconds
        resetX.start();

        ObjectAnimator resetY = ObjectAnimator.ofFloat(this, "rotationY", 0f, 0);
        resetY.setDuration(durationInMs);
        resetY.start();

        ObjectAnimator rotateXAnimator = ObjectAnimator.ofFloat(this, "rotationX", 0f, Y);
        rotateXAnimator.setDuration(2000); // Duration in milliseconds
        rotateXAnimator.start();

        ObjectAnimator rotateYAnimator = ObjectAnimator.ofFloat(this, "rotationY", 0f, X);
        rotateYAnimator.setDuration(2000);
        rotateYAnimator.start();
    }

    private void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    private void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    private void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    public static float[] getRGB(final String rgb) {
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
        gl.glClearColor(backgroundColorArray[0], backgroundColorArray[1], backgroundColorArray[2], 1);
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

        // Setup Texture, each time the surface is created
        cube.loadTexture(gl);    // Load image into Texture
        gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear color and depth buffers
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // ----- Render the Cube -----
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glRotatef(rotationX, 1f, 0f, 0f);
        gl.glRotatef(rotationY, 0f, 1f, 0f);
        gl.glRotatef(rotationZ, 0f, 0f, 1f);
        cube.draw(gl);
    }
}
