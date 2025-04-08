package com.cj186.androidglkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.cj186.androidglkit.exceptions.FaceImageMismatchException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Cube {

    // Buffers for textures and vertices
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer texBuffer;

    // Cube size percentage of one.
    float size = 1;

    // Cube vertices
    private final float[] vertices = {
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            -1.0f,  1.0f, 0.0f,
            1.0f,  1.0f, 0.0f
    };

    float[] textureCoordinates = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    // The number of faces.
    private final int numFaces = 6;

    // The texture drawables and the bitmaps they will become.
    private final int[] textureIDs = new int[numFaces];
    private final Bitmap[] bitmap = new Bitmap[numFaces];

    public Cube(Context ctx, int[] ids, float size) {
        this.size = size;

        if(ids.length != numFaces){
            // Make sure we have enough images for our sides.
            throw new FaceImageMismatchException("Amount of images (" + (ids.length) + ") is not equal to number of faces (" + numFaces +  ")");
        }

        // Set up our vertices byte buffer, with vertices multiplied by four to fit our floats.
        ByteBuffer verticesByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        verticesByteBuffer.order(ByteOrder.nativeOrder());

        // Set our float vertices buffer.
        vertexBuffer = verticesByteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // Setup texture byte buffer, making space for our six sides.
        ByteBuffer textureByteBuffer = ByteBuffer.allocateDirect(textureCoordinates.length * 4 * numFaces);
        textureByteBuffer.order(ByteOrder.nativeOrder());

        // Setup our texture float buffer.
        texBuffer = textureByteBuffer.asFloatBuffer();
        // Loop over our faces, to ensure we have texture coordinates for each image.
        for(int i = 0; i < numFaces; i++)
            texBuffer.put(textureCoordinates);
        texBuffer.position(0);

        // Turn our drawable images into bitmaps.
        for(int face = 0; face < numFaces; face++){
            bitmap[face] = BitmapFactory.decodeResource(ctx.getResources(), ids[face]);
        }
    }

    public void draw(GL10 gl){
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // Render our faces with a loop to prevent super duper repetitive code.
        for(int face = 0; face < numFaces; face++){
            // Set our texture coordinate pointer.
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
            gl.glPushMatrix();
            // Set the scale and texture of the face.
            gl.glScalef(size, size, size);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[face]);

            // Rotate the face depending on which face it is.
            switch (face) {
                case 0: // Front face / One on a dice.
                    break;
                case 1: // Left face / Four on a dice.
                    gl.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
                    break;
                case 2: // Back face / Three on a dice.
                    gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
                    break;
                case 3: // Right face / Two on a dice.
                    gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
                    break;
                case 4: // Top face / Six on a dice (Y Axis).
                    gl.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
                    break;
                case 5: // Bottom face / Five on a dice (Y Axis)
                    gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                    break;
            }
            // Move the face to the center.
            gl.glTranslatef(0.0f, 0.0f, 1.0f);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            gl.glPopMatrix();
        }

        // Set the client state.
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }

    public void loadTexture(GL10 gl) {
        // Generate texture-ID array
        gl.glGenTextures(numFaces, textureIDs, 0);

        // Loop over our faces and set texture filters and texture ids.
        for(int face = 0; face < numFaces; face++){
            // Bind to texture ID
            // Set up texture filters
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[face]);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap[face], 0);
            bitmap[face].recycle();
        }
    }
}
