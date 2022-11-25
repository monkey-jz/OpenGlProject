package com.jerry.openglproject.particles;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/8
 */
public class TextResourceReader {

    public static String readTextFileFromResource(Context context,int resId){
        StringBuilder body = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append("\n");
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Could not open resource: " + resId,ioe);

        }catch (Resources.NotFoundException nfe) {
            throw new RuntimeException("Resource not found: " + resId,nfe);
        }
        return body.toString();
    }
}
