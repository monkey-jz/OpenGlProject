package com.jerry.openglproject.airhockeywithbettermallets.android.objects;

import com.jerry.openglproject.airhockeywithbettermallets.android.data.VertexArray;
import com.jerry.openglproject.airhockeywithbettermallets.android.programs.ColorShaderProgram;
import com.jerry.openglproject.airhockeywithbettermallets.android.util.Geometry;
import java.util.List;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/15
 */
public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private final VertexArray vertexArray;
    public float radius;
    public float height;
    private List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius, float height, int numPoinsAroundMallet) {
        this.radius = radius;
        this.height = height;
        ObjectBuilder.GeneratedData mallet = ObjectBuilder.createMallet(new Geometry.Point(0, 0, 0), radius, height, numPoinsAroundMallet);
        vertexArray = new VertexArray(mallet.vertexData);
        drawList = mallet.drawList;
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttribPointer(0,colorShaderProgram.getaPositionAtteibuteLocation(),POSITION_COMPONENT_COUNT,0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
