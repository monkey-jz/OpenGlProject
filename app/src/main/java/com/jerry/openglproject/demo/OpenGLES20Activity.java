package com.jerry.openglproject.demo;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/2
 */
public class OpenGLES20Activity extends Activity {
    public static final String TAG = "OpenGLES20Activity";
    private MyGLSurfaceView gLView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }
}
