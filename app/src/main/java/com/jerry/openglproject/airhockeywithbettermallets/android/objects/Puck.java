package com.jerry.openglproject.airhockeywithbettermallets.android.objects;

import com.jerry.openglproject.airhockeywithbettermallets.android.data.VertexArray;
import com.jerry.openglproject.airhockeywithbettermallets.android.programs.ColorShaderProgram;
import com.jerry.openglproject.airhockeywithbettermallets.android.util.Geometry;
import java.util.List;

/**
 * @author: JerryZhu
 * @datetime: 2022/11/17
 */
public class Puck {
    private static final int POSITION_COMPONENT_COUNT = 3;
    public float radius;
    public float height;
    private VertexArray vertexArray;
    private List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius, float height, int numPoinsAroundPuck) {
        this.radius = radius;
        this.height = height;
        ObjectBuilder.GeneratedData puck = ObjectBuilder.createPuck(new Geometry.Cylinder(new Geometry.Point(0, 0, 0), radius, height), numPoinsAroundPuck);
        vertexArray = new VertexArray(puck.vertexData);
        drawList = puck.drawList;
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, colorShaderProgram.getaPositionAtteibuteLocation(),POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }


}
