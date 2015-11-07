package structure;

import java.awt.Color;

import org.joml.Vector3f;

public class UpperLeg extends Structure{
	private float length;
	public float getLength(){
		return length;
	}
	public float getActualLength(){
		return new Vector3f(end).sub(start).length();
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
		end = new Vector3f();
		setLength(1.2f);
	}
}
