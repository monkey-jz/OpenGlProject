package com.jerry.openglproject.airhockeytextured.android.programs;

import android.content.Context;

import com.jerry.openglproject.airhockeytextured.ShaderHelper;
import com.jerry.openglproject.airhockeytextured.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/15
 */
public class ShaderProgram {

    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected  final int program;

    protected ShaderProgram(Context context, int vextureShaderResourceId,int fragmentShaderResourceId) {
        this.program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context,vextureShaderResourceId),
                TextResourceReader.readTextFileFromResource(context,fragmentShaderResourceId));
    }

    public void useProgram() {
        glUseProgram(program);
    }
}
