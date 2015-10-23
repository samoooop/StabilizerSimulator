package structure;

import org.joml.Vector3f;

public abstract class Motor {

	public float hornLength, rodEndLength;
	
	public Motor(float hornLength, float rodEndLength) {
		super();
		this.hornLength = hornLength;
		this.rodEndLength = rodEndLength;
	}
	
	public abstract float getRotationAngle(Vector3f locataionOfHandle);

}
