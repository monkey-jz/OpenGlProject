package com.jerry.openglproject.particles;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.jerry.openglproject.R;
import com.jerry.openglproject.particles.android.objects.ParticleShooter;
import com.jerry.openglproject.particles.android.objects.ParticleSystem;
import com.jerry.openglproject.particles.android.programs.ParticleShaderProgram;
import com.jerry.openglproject.particles.android.util.Geometry;
import com.jerry.openglproject.util.MatrixHelper;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/4
 */
public class ParticlesRenderer implements GLSurfaceView.Renderer {

    private final Context context;
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    private ParticleShaderProgram particleShaderProgram;
    private ParticleSystem particleSystem;
    private ParticleShooter redParticleShooter;
    private ParticleShooter greenParticleShooter;
    private ParticleShooter blueParticleShooter;
    private long globalStartTime;
    private int texture;

    public ParticlesRenderer(Context context) {
        this.context = context;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.w(ParticlesActivity.TAG,"onSurfaceCreated ===");
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE);

        particleShaderProgram = new ParticleShaderProgram(context);
        particleSystem = new ParticleSystem(10000);
        globalStartTime = System.nanoTime();
        final Geometry.Vector particleDirection = new Geometry.Vector(0f,0.5f,0f);
        final float angleVarianceDegrees = 5f;
        final float speedVariance = 1f;
        redParticleShooter = new ParticleShooter(
          new Geometry.Point(-1f,0f,0f),
          particleDirection,
          Color.rgb(255,50,5),
                angleVarianceDegrees,
                speedVariance
        );
        blueParticleShooter = new ParticleShooter(
                new Geometry.Point(0f,0f,0f),
                particleDirection,
                Color.rgb(25,255,25),
                angleVarianceDegrees,
                speedVariance
        );
        greenParticleShooter = new ParticleShooter(
                new Geometry.Point(1f,0f,0f),
                particleDirection,
                Color.rgb(5,50,255),
                angleVarianceDegrees,
                speedVariance
        );
        texture = TextureHelper.loadTexture(context, R.drawable.particle_texture);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.w(ParticlesActivity.TAG,"onSurfaceChanged ===");
        glViewport(0,0,width,height);
        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width / (float)height,1f,10f);
        setIdentityM(viewMatrix,0);
        translateM(viewMatrix,0,0f,-1.5f,-5f);
        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
        redParticleShooter.addParticles(particleSystem,currentTime,5);
        blueParticleShooter.addParticles(particleSystem,currentTime,5);
        greenParticleShooter.addParticles(particleSystem,currentTime,5);
        particleShaderProgram.useProgram();
        particleShaderProgram.setUniform(viewProjectionMatrix,currentTime,texture);
        particleSystem.bindData(particleShaderProgram);
        particleSystem.draw();

    }

}
