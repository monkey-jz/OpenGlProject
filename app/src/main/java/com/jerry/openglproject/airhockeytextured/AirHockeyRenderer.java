package com.jerry.openglproject.airhockeytextured;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.jerry.openglproject.R;
import com.jerry.openglproject.airhockeytextured.android.objects.Mallet;
import com.jerry.openglproject.airhockeytextured.android.objects.Table;
import com.jerry.openglproject.airhockeytextured.android.programs.ColorShaderProgram;
import com.jerry.openglproject.airhockeytextured.android.programs.TextureShaderProgram;
import com.jerry.openglproject.util.MatrixHelper;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/4
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private  final float[] projectionMatrix = new float[16];
    private  final float[] modelMatrix = new float[16];
    private final Context context;
    private Table table;
    private Mallet mallet;
    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;
    private int texture;

    public AirHockeyRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.w(AirHockeyActivity.TAG,"onSurfaceCreated ===");
        glClearColor(0.6f,0.6f,0.3f,0.5f);
        table = new Table();
        mallet = new Mallet();
        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgram = new ColorShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.mipmap.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.w(AirHockeyActivity.TAG,"onSurfaceChanged ===");
        glViewport(0,0,width,height);
        MatrixHelper.perspectiveM(projectionMatrix,45,(float) width / (float) height,1f,10f);
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix,0,0f,0f,-3f);
        rotateM(modelMatrix,0,-60f,1f,0f,0f);
        float[] temp = new float[16];
        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.w(AirHockeyActivity.TAG,"onDrawFrame ===");
        glClear(GLES20.GL_COLOR_BUFFER_BIT);
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(projectionMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();

        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

    }
}
