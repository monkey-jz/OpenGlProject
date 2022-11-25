package com.jerry.openglproject.airhockeytextured.android.objects;

import com.jerry.openglproject.airhockeytextured.android.data.VertexArray;
import com.jerry.openglproject.airhockeytextured.android.programs.ColorShaderProgram;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.jerry.openglproject.airhockeytextured.android.Constants.BYTES_PER_FLOAT;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/15
 */
public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            //X,Y,R,G,B
            0f, -0.4f, 0f, 0f, 1f,
            0f,  0.4f, 1f, 0f, 0f,
    };

    private final VertexArray vertexArray;

    public Mallet() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttribPointer(0,colorShaderProgram.getaPositionLocation(),POSITION_COMPONENT_COUNT,STRIDE);
        vertexArray.setVertexAttribPointer(0,colorShaderProgram.getaColorLocation(),COLOR_COMPONENT_COUNT,STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_POINTS,0,2);
    }
}
