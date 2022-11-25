package com.jerry.openglproject.skybox.android.programs;

import android.content.Context;

import com.jerry.openglproject.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/22
 */
public class ParticleShaderProgram extends ShaderProgram {
    private int uMatrixLocation;
    private int uTimeLocation;
    private int aPositionLocation;
    private int aColorLocation;
    private int aDirectionVectorLocation;
    private int aParticleStartTimeLocation;
    private int uTextureLocation;
    public ParticleShaderProgram(Context context) {
        super(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTimeLocation = glGetUniformLocation(program, U_TIME);
        uTextureLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR);
        aParticleStartTimeLocation = glGetAttribLocation(program, A_PARTICLE_START_TIME);
    }

    public void setUniform(float[] matrix,float elapsedTime,int textureId) {
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        glUniform1f(uTimeLocation,elapsedTime);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,textureId);
        glUniform1i(uTextureLocation,0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }

    public int getDirectionVectorAttributeLocation() {
        return aDirectionVectorLocation;
    }

    public int getParticleStartTimeAttributeLocation() {
        return aParticleStartTimeLocation;
    }
}
