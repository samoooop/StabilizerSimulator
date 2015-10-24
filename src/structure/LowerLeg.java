package structure;

import org.joml.Vector3f;

public class LowerLeg extends Structure {
	protected UpperLeg upperLeg;
	private float length;
	public float getLength(){
		return length;
	}
	public void setLength(float length){
		this.length = length;
		this.end.y = length;
	}
	public LowerLeg() {
		super();
		start = new Vector3f();
		end = new Vector3f();
		setLength(0.7f);
		upperLeg = new UpperLeg();
		upperLeg.location = end;
		subStructure.add(upperLeg);
	}
}
