package structure;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Leg extends Structure {
	public UpperLeg upperLeg;
	public LowerLeg lowerLeg;
	public float motorAngle;
	public float getMotorAngle(){
		return motorAngle;
	}
	public void setMotorAngle(){
		
	}
	public Leg() {
		super();
		lowerLeg = new LowerLeg();
		lowerLeg.name = this.name+"_lowerLeg";
		subStructure.add(lowerLeg);
		upperLeg = lowerLeg.upperLeg;
	}
//	@Override
//	public AdjustParameter draw(Color color, Matrix4f transformation){
//		System.out.println(new Vector4f().mul(transformation));
//		return super.draw(color, transformation);
//	}
}
