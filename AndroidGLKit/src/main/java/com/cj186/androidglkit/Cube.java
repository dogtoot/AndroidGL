package com.cj186.androidglkit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import android.content.Context;

import com.cj186.androidglkit.Exceptions.CubeTooLargeException;
import com.cj186.androidglkit.Exceptions.FaceImageMismatchException;

import javax.microedition.khronos.opengles.GL10;

public class Cube {
    private FloatBuffer vertexBuffer;
    private FloatBuffer texBuffer;

    float size = 1;

    private float[] vertices = {
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            -1.0f,  1.0f, 0.0f,
            1.0f,  1.0f, 0.0f
    };

    float[] texCoords = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    private int numFaces = 6;
    private int[] imageFileIds;

    private int[] textureIDs = new int[numFaces];
    private Bitmap[] bitmap = new Bitmap[numFaces];

    public Cube(Context ctx, int[] ids, float size) throws FaceImageMismatchException {
        if(ids.length != numFaces){
            throw new FaceImageMismatchException("Amount of images (" + (ids.length) + ") is not equal to number of faces (" + numFaces +  ")");
        }

        if(size > 1){
            throw new CubeTooLargeException("Size must be between 0 and 1, size inputted was:" + size + ".");
        }

        this.size = size;

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        imageFileIds = ids;

        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4 * numFaces);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asFloatBuffer();
        for(int i = 0; i < numFaces; i++)
            texBuffer.put(texCoords);
        texBuffer.position(0);

        for(int face = 0; face < numFaces; face++){
            bitmap[face] = BitmapFactory.decodeResource(ctx.getResources(), imageFileIds[face]);
        }
    }

    public void draw(GL10 gl){
        gl.glFrontFace(GL10.GL_CCW);
        //gl.glEnable(GL10.GL_CULL_FACE);
        //gl.glCullFace(GL10.GL_BACK);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        for(int face = 0; face < numFaces; face++){
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
            gl.glPushMatrix();
            gl.glScalef(size, size, size);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[face]);

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
            gl.glTranslatef(0.0f, 0.0f, 1.0f);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            gl.glPopMatrix();
        }

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }

    public void loadTexture(GL10 gl) {
        gl.glGenTextures(numFaces, textureIDs, 0); // Generate texture-ID array

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
