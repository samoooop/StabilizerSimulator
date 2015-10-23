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
		c1 = locataionOfHandle.y;
		c2 = locataionOfHandle.x;
		c3 = (
				  pow2(locataionOfHandle.x) 
				+ pow2(locataionOfHandle.y) 
				+ pow2(locataionOfHandle.z)
				+ pow2(hornLength)
				- pow2(rodEndLength)
			) / (2 * hornLength);

		System.out.println("c1 "+c1);
		System.out.println("c2 "+c2);
		System.out.println("c3 "+c3);
		
		float x =
				(float)
				(Math.asin( c3 / Math.sqrt(pow2(c1) + pow2(c2)))
				-
				Math.atan( c2 / c1 ));
		
		System.out.println(Math.asin( c3 / Math.sqrt(pow2(c1) + pow2(c2))));
		System.out.println(Math.atan( c2 / c1 ));
		return x;
	}

}
