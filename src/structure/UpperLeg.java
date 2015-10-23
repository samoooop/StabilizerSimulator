package structure;

import java.awt.Color;

import org.joml.Vector3f;

public class UpperLeg extends Structure{
	private float length;
	public float getLength(){
		return length;
	}
	public void setLength(float length){
		this.length = length;
		this.end.y = length;
	}
	public UpperLeg(){
		super();
		this.color = Color.PINK;
		this.forcedColor = true;
		start = new Vector3f();
		end = new Vector3f(0.0f, 1.0f, 0.0f);
		
	}
}
