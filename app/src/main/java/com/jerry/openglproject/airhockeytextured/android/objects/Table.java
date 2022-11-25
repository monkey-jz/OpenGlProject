package com.jerry.openglproject.airhockeytextured.android.objects;

import com.jerry.openglproject.airhockeytextured.android.data.VertexArray;
import com.jerry.openglproject.airhockeytextured.android.programs.TextureShaderProgram;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static com.jerry.openglproject.airhockeytextured.android.Constants.BYTES_PER_FLOAT;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/15
 */
public class Table {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    float [] VERTEX_DATA = {
            //X,Y,S,T
            //Trangle Fan
               0f,    0f, 0.5f, 0.5f,
            -0.5f, -0.8f,   0f, 0.9f,
             0.5f, -0.8f,   1f, 0.9f,
             0.5f,  0.8f,   1f, 0.1f,
            -0.5f,  0.8f,   0f, 0.1f,
            -0.5f, -0.8f,   0f, 0.9f,
    } ;

    private VertexArray vertexArray;

    public Table() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureShaderProgram) {
        vertexArray.setVertexAttribPointer(0,textureShaderProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,textureShaderProgram.getTextureCoordinatesLocation(),TEXTURE_COORDINATES_COMPONENT_COUNT,STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLE_FAN,0,6);
    }

}
