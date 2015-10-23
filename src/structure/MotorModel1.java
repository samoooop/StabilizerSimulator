package structure;

import org.joml.Vector3f;

public class MotorModel1 extends Motor {

	public MotorModel1(float hornLength, float rodEndLength) {
		super(hornLength, rodEndLength);
	}

	private static float pow2 (float num){
		return num*num;
	}
	
	@Override
	public float getRotationAngle(Vector3f locataionOfHandle) {
		float c1,c2,c3;
		c1 = locataionOfHandle.x;
		c2 = locataionOfHandle.y;
		c3 = (
				-pow2(rodEndLength) + 
				pow2(locataionOfHandle.x) + 
				pow2(locataionOfHandle.y) + 
				pow2(locataionOfHandle.z) +
				pow2(hornLength)
			) / (2 * hornLength);
		float angle = 
				(float)(
						2 * Math.atan(
								(  c2 - Math.sqrt(pow2(c1) + pow2(c2) - pow2(c3))  ) // Subtract
								/
								(c1 + c3)
								)
						);
		if(angle < 0){
			angle = 
				(float)(
						2 * Math.atan(
								(  c2 + Math.sqrt(pow2(c1) + pow2(c2) - pow2(c3))  ) // Add
								/
								(c1 + c3)
								)
						);
		}
		return angle;
	}

}
