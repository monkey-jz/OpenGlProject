package com.jerry.openglproject.skybox;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/4
 */
public class SkyboxActivity extends Activity {

    public static final String TAG = "AirHockeyActivity";
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    private ConfigurationInfo deviceConfigurationInfo;
    private ParticlesRenderer particlesRenderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        deviceConfigurationInfo = activityManager.getDeviceConfigurationInfo();
        //检查真机设备是否支持OpenGL2.0
        boolean supportEs2 = isSupportOpenGL20();
        if (supportEs2) {
             glSurfaceView.setEGLContextClientVersion(2);
            particlesRenderer = new ParticlesRenderer(this);
            glSurfaceView.setRenderer(particlesRenderer);
             rendererSet = true;
        }else {
            Toast.makeText(this, "此设备不支持OpenGL ES 2.0.", Toast.LENGTH_SHORT).show();
        }
        setContentView(glSurfaceView);

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            float previousX, previousY,currentX,currentY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    currentX = event.getX();
                    currentY = event.getY();
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        previousX = currentX;
                        previousY = currentY;
                    }else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        float deltaX = currentX - previousX;
                        float deltaY = previousY - previousY;
                        previousX = currentX;
                        previousY = currentY;
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                particlesRenderer.onDragEvent(deltaX,deltaY);
                            }
                        });
                    }
                    return true;
                }else {

                    return false;
                }


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet) {
            glSurfaceView.onResume();
        }
    }

    //检查模设备是否支持OpenGL2.0
    private boolean isSupportOpenGL20(){
        return deviceConfigurationInfo.reqGlEsVersion >= 0x20000
                        || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")));
    }

}
