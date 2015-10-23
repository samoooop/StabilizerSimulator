package structure;

import org.joml.Vector3f;

public class MotorModel2 extends Motor {

	public MotorModel2(float hornLength, float rodEndLength) {
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
		float x =
				(float)
				(Math.asin( c3 / Math.sqrt(pow2(c1) + pow2(c2)))
				-
				Math.atan( c2 / c1 ));
		return x;
	}

}
