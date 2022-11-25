package com.jerry.openglproject.airhockeywithbettermallets;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.jerry.openglproject.R;
import com.jerry.openglproject.airhockeywithbettermallets.android.objects.Mallet;
import com.jerry.openglproject.airhockeywithbettermallets.android.objects.Puck;
import com.jerry.openglproject.airhockeywithbettermallets.android.objects.Table;
import com.jerry.openglproject.airhockeywithbettermallets.android.programs.ColorShaderProgram;
import com.jerry.openglproject.airhockeywithbettermallets.android.programs.TextureShaderProgram;
import com.jerry.openglproject.airhockeywithbettermallets.android.util.Geometry;
import com.jerry.openglproject.util.MatrixHelper;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;
import static androidx.core.math.MathUtils.clamp;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/4
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private  final float[] projectionMatrix = new float[16];
    private  final float[] viewMatrix = new float[16];
    private  final float[] modelMatrix = new float[16];
    private  final float[] viewProjectionMatrix = new float[16];
    private  final float[] modelViewProjectionMatrix = new float[16];
    private  final float[] invertedViewProjectionMatrix = new float[16];
    private final Context context;
    private Table table;
    private Mallet mallet;
    private Puck puck;
    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;
    private int texture;
    private boolean malletPressed = false;
    private Geometry.Point blueMalletPosition;
    private final float leftBound = -0.5f;
    private final float rightBound = 0.5f;
    private final float farBound = -0.8f;
    private final float nearBound = 0.8f;
    private Geometry.Point previousBlueMalletPosition;
    private Geometry.Point puckPosition;
    private Geometry.Vector puckVector;

    public AirHockeyRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.w(AirHockeyActivity.TAG,"onSurfaceCreated ===");
        glClearColor(0.6f,0.6f,0.3f,0.5f);
        table = new Table();
        mallet = new Mallet(0.08f,0.08f,32);
        puck = new Puck(0.06f,0.02f,32);
        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgram = new ColorShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.mipmap.air_hockey_surface);
        blueMalletPosition = new Geometry.Point(0f, mallet.height / 2, 0.4f);

        puckPosition = new Geometry.Point(0f,puck.height / 2f,0f);
        puckVector = new Geometry.Vector(0f,0f,0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.w(AirHockeyActivity.TAG,"onSurfaceChanged ===");
        glViewport(0,0,width,height);
        MatrixHelper.perspectiveM(projectionMatrix,45,(float) width / (float) height,1f,10f);
        setLookAtM(viewMatrix,0,0f,1.2f,2.5f,0f,0f,0f,0f,1f,0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //Log.w(AirHockeyActivity.TAG,"onDrawFrame ===");
        glClear(GLES20.GL_COLOR_BUFFER_BIT);

        puckPosition = puckPosition.translate(puckVector);
        if (puckPosition.x < leftBound + puck.radius || puckPosition.x > rightBound - puck.radius) {
            puckVector = new Geometry.Vector(-puckVector.x,puckVector.y,puckVector.z);
            puckVector = puckVector.scale(0.9f);
        }
        if (puckPosition.z < farBound + puck.radius || puckPosition.z > nearBound - puck.radius) {
            puckVector = new Geometry.Vector(puckVector.x,puckVector.y,-puckVector.z);
            puckVector = puckVector.scale(0.9f);
        }
        puckPosition = new Geometry.Point(
                clamp(puckPosition.x, leftBound + puck.radius,rightBound - puck.radius),
                mallet.height / 2f,
                clamp(puckPosition.z, farBound + puck.radius, nearBound - puck.radius));

        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);
        invertM(invertedViewProjectionMatrix,0,viewProjectionMatrix,0);

        positionTableInScene();
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(modelViewProjectionMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();


        positionObjectInScene(0f,mallet.height / 2f,-0.4f);
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(modelViewProjectionMatrix,1f,0f,0f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

        positionObjectInScene(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix,0f,0f,1f);
        mallet.draw();

        positionObjectInScene(puckPosition.x,puckPosition.y,puckPosition.z);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix,0.8f,0.8f,1f);
        puck.bindData(colorShaderProgram);
        puck.draw();
        puckVector = puckVector.scale(0.99f);
    }

    private void positionObjectInScene(float x, float y, float z) {
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix,0,x,y,z);
        multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
    }

    private void positionTableInScene() {
        setIdentityM(modelMatrix,0);
        rotateM(modelMatrix,0,-90f,1f,0f,0f);
        multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {
        Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
        Geometry.Sphere sphere = new Geometry.Sphere(new Geometry.Point(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z), mallet.height / 2f);
        boolean intersects = Geometry.intersects(sphere, ray);
        if (intersects) {
            malletPressed = true;
        }
    }

    public void handleTouchDrag(float normalizedX, float normalizedY) {
        if (malletPressed) {
            Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
            Geometry.Plane plane = new Geometry.Plane(new Geometry.Point(0, 0, 0), new Geometry.Vector(0, 1, 0));
            Geometry.Point touchedPoint = Geometry.intersectionPoint(ray,plane);
            previousBlueMalletPosition = blueMalletPosition;
            blueMalletPosition = new Geometry.Point(
                    clamp(touchedPoint.x, leftBound + mallet.radius,rightBound - mallet.radius),
                    mallet.height / 2f,
                    clamp(touchedPoint.z, 0f + mallet.radius, nearBound - mallet.radius));
            float distance = Geometry.vectorBetween(blueMalletPosition,puckPosition).length();
            if (distance <= puck.radius + mallet.radius) {
                puckVector = Geometry.vectorBetween(blueMalletPosition,puckPosition).scale(0.3f);
            }
        }
    }

    private Geometry.Ray convertNormalized2DPointToRay(float normalizedX,float normalizedY) {
        final float[] nearPointNdc = {normalizedX,normalizedY,-1,1};
        final float[] farPointNdc = {normalizedX,normalizedY,1,1};
        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];
        multiplyMV(nearPointWorld,0,invertedViewProjectionMatrix,0,nearPointNdc,0);
        multiplyMV(farPointWorld,0,invertedViewProjectionMatrix,0,farPointNdc,0);
        divideByW(nearPointWorld);
        divideByW(farPointWorld);
        Geometry.Point nearPoint = new Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Geometry.Point farPoint = new Geometry.Point(farPointWorld[0],farPointWorld[1],farPointWorld[2]);
        return new Geometry.Ray(nearPoint,Geometry.vectorBetween(nearPoint,farPoint));
    }

    private void divideByW(float[] vector){
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }

    private float clamp(float value,float min,float max){
        return Math.min(max,Math.max(value,min));
    }
}
