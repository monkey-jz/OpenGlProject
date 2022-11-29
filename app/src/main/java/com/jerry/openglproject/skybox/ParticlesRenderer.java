package com.jerry.openglproject.skybox;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.jerry.openglproject.R;
import com.jerry.openglproject.skybox.android.objects.ParticleShooter;
import com.jerry.openglproject.skybox.android.objects.ParticleSystem;
import com.jerry.openglproject.skybox.android.objects.Skybox;
import com.jerry.openglproject.skybox.android.programs.ParticleShaderProgram;
import com.jerry.openglproject.skybox.android.programs.SkyboxShaderProgram;
import com.jerry.openglproject.skybox.android.util.Geometry;
import com.jerry.openglproject.util.MatrixHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
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
    private SkyboxShaderProgram skyboxShaderProgram;
    private int cubeMapTexture;
    private Skybox skybox;
    private float xRotation, yRotation;

    public ParticlesRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.w(SkyboxActivity.TAG,"onSurfaceCreated ===");
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        skybox = new Skybox();
        skyboxShaderProgram = new SkyboxShaderProgram(context);
        cubeMapTexture = TextureHelper.loadCubeMap(context, new int[]{
                R.drawable.left, R.drawable.right, R.drawable.bottom,
                R.drawable.top, R.drawable.front, R.drawable.back,});

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
                new Geometry.Point(0f,-0.8f,0f),
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
        Log.w(SkyboxActivity.TAG,"onSurfaceChanged ===");
        glViewport(0,0,width,height);
        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width / (float)height,1f,10f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        drawSkybox();
        drawParticle();
    }

    private void drawSkybox() {
        setIdentityM(viewMatrix,0);
        rotateM(viewMatrix,0,-yRotation,1f,0f,0f);
        rotateM(viewMatrix,0,-xRotation,0f,1f,0f);
        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);
        skyboxShaderProgram.useProgram();
        skyboxShaderProgram.setUniforms(viewProjectionMatrix,cubeMapTexture);
        skybox.bindData(skyboxShaderProgram);
        skybox.draw();
    }

    private void drawParticle() {
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
        redParticleShooter.addParticles(particleSystem,currentTime,5);
        blueParticleShooter.addParticles(particleSystem,currentTime,5);
        greenParticleShooter.addParticles(particleSystem,currentTime,5);

        setIdentityM(viewMatrix,0);
        rotateM(viewMatrix,0,-yRotation,1f,0f,0f);
        rotateM(viewMatrix,0,-xRotation,0f,1f,0f);
        translateM(viewMatrix,0,0f,-1.5f,-7f);
        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE);

        particleShaderProgram.useProgram();
        particleShaderProgram.setUniform(viewProjectionMatrix,currentTime,texture);
        particleSystem.bindData(particleShaderProgram);
        particleSystem.draw();

        glDisable(GL_BLEND);
    }

    public void onDragEvent(float deltaX, float deltaY) {
        xRotation += deltaX / 16f;
        yRotation += deltaY / 16f;

        if (yRotation < -90) {
            yRotation = -90;
        }else if (yRotation > 90) {
            yRotation = 90;
        }

    }
}
