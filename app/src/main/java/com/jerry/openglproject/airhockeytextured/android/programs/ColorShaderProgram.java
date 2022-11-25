package com.jerry.openglproject.airhockeytextured.android.programs;

import android.content.Context;
import com.jerry.openglproject.R;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/16
 */
public class ColorShaderProgram extends ShaderProgram{

    private final int aPositionLocation;
    private final int aColorLocation;
    private final int uMatrixLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        aColorLocation = glGetAttribLocation(program,A_COLOR);
    }

    public void setUniforms(float[] matrix) {
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
    }

    public int getaPositionLocation() {
        return aPositionLocation;
    }

    public int getaColorLocation() {
        return aColorLocation;
    }
}
