package structure;

import java.awt.Color;

import org.joml.Vector3f;

public class Axis extends Structure{
	private Structure xAxis,yAxis,zAxis;
	public Axis(){
		super();
		drawAxis();
	}
	
	private void drawAxis(){
		xAxis = new Structure();
		yAxis = new Structure();
		zAxis = new Structure();
		xAxis.start = new Vector3f();
		xAxis.end = new Vector3f(1.0f,0,0);
		yAxis.start = new Vector3f();
		yAxis.end = new Vector3f(0,1.0f,0);
		zAxis.start = new Vector3f();
		zAxis.end = new Vector3f(0,0,1.0f);
		xAxis.color = Color.RED;
		yAxis.color = Color.GREEN;
		zAxis.color = Color.BLUE;
		subStructure.add(xAxis);
		subStructure.add(yAxis);
		subStructure.add(zAxis);
	}
}

