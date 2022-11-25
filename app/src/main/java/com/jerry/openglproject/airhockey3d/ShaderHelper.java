package com.jerry.openglproject.airhockey3d;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/8
 */
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }
    private static int compileShader(int type ,String shaderCode) {
        int shaderObjectId = glCreateShader(type);
        if (shaderObjectId == 0) {
            return 0;
        }
        glShaderSource(shaderObjectId,shaderCode);
        glCompileShader(shaderObjectId);
        int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);
        if (LoggerConfig.ON){
            Log.w(TAG,"Results of compiling source: \n " + shaderCode + "\n " + glGetShaderInfoLog(shaderObjectId));
        }
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId);
            if (LoggerConfig.ON) {
                Log.w(TAG,"Compilation of shader failed.");
            }
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId,int fragmentShaderId) {
        int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG,"Could not create new program.");
            }
        }
        glAttachShader(programObjectId,vertexShaderId);
        glAttachShader(programObjectId,fragmentShaderId);
        glLinkProgram(programObjectId);
        int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId,GL_LINK_STATUS,linkStatus,0);
        if (LoggerConfig.ON) {
            Log.w(TAG,"Results of linking program:" +"\n" + glGetProgramInfoLog(programObjectId));
        }
        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);
            if (LoggerConfig.ON) {
                Log.w(TAG,"Linking program failed.");
            }
            return 0;
        }
        return programObjectId;
    }

    public static boolean valideProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId,GL_VALIDATE_STATUS,validateStatus,0);
        Log.w(TAG,"Results of validating program: \n " + validateStatus[0] + "\n " + glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;

    }
}
