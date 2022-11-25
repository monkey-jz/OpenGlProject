package com.jerry.openglproject.airhockey;

import android.content.Context;
import android.location.Location;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.jerry.openglproject.R;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/4
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static int aPositionLocation;
    private static int aColorLocation;
    private static int uMatrixLocation;
    private static int POSITION_COMPONENT_COUNT = 4;
    private static int COLOR_COMPONENT_COUNT = 3;
    private static int BYTES_PER_FLOAT = 4;
    private static int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT ;
    private final FloatBuffer vertexData;
    private Context context;
    private int program;
    private  final float[] projectionMatrix = new float[16];

    public AirHockeyRenderer(Context context) {
        this.context = context;

        float [] tableVerticesWithTriangles = {

                //X,Y,Z,W,R,G,B

                //Trangle Fan
                   0f,    0f, 0f, 1.5f, 1f, 1f, 1f,
                -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                 0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                 0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
                -0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,

                //Line
                -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
                 0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,

                 //Mallets
                 0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
                 0f,  0.4f, 0f, 1.75f, 1f, 0f, 0f
        } ;

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();//从底层申请本地内存
        vertexData.put(tableVerticesWithTriangles);//把数据从Dalvik复制到本地内存
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.w(AirHockeyActivity.TAG,"onSurfaceCreated ===");
        glClearColor(0.6f,0.6f,0.3f,0.5f);
        String vertexShaderResource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String frameShaderResource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderResource);
        int fragmentShader = ShaderHelper.compileFragmentShader(frameShaderResource);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        if (LoggerConfig.ON) {
            ShaderHelper.valideProgram(program);
        }
        glUseProgram(program);
        aColorLocation = glGetAttribLocation(program,A_COLOR);
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        glEnableVertexAttribArray(aPositionLocation);
        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.w(AirHockeyActivity.TAG,"onSurfaceChanged ===");
        glViewport(0,0,width,height);
        final float aspectRatio = width > height ?
                (float) width / (float) height : (float) height / (float) width;
        if (width > height) {
            //landscape
            orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f);
        }else {
            //portrait or square
            orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.w(AirHockeyActivity.TAG,"onDrawFrame ===");
        glClear(GLES20.GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(uMatrixLocation,1,false,projectionMatrix,0);
        glDrawArrays(GL_TRIANGLE_FAN,0,6);
        glDrawArrays(GL_LINES,6,2);
        glDrawArrays(GL_POINTS,8,1);
        glDrawArrays(GL_POINTS,9,1);

    }
}
