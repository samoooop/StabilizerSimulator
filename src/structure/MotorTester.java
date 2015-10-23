package structure;

import java.io.Console;
import java.util.Random;

import org.joml.Matrix3f;
import org.joml.Vector3f;

public class MotorTester {

	public MotorTester() {
		// TODO Auto-generated constructor stub
	}
	
	private static boolean floatIsEqual(float a, float b, float esp) { return Math.abs(a-b) < esp; }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector3f handleLocation = new Vector3f(
				0f,
				0f,
				4*(float)Math.cos(Math.PI/4) + 12*(float)(Math.cos(  Math.asin(Math.sin(Math.PI/4) / 3f)  ))
				//(float)(Math.random()*3-1.5f),
				//(float)(Math.random()*3-1.5f),
				//(float)(Math.random()*3-1.5f)
				);

		System.out.println("Input Handle Location" + handleLocation);
		
		float hornLength = 4.f,
				endRodLength = 12.f;
		
		Motor motor = new MotorModel1(hornLength, endRodLength);
		float motorAngle = 
			(float)Math.PI/4;
			//motor.getRotationAngle(handleLocation);
		
		Vector3f hornLocation = new Vector3f(hornLength, 0, 0);
		Matrix3f rotationMatMotorAngle = new Matrix3f();
		rotationMatMotorAngle.rotateXYZ(0f, -motorAngle, 0f);
		hornLocation.mul(rotationMatMotorAngle);
		
		System.out.println("motorAngle " + (motorAngle / Math.PI * 180f) );
		System.out.println("hornLocation " + hornLocation);
		
		System.out.println("distance between handleLocation and hornLocation is " + handleLocation.distance(hornLocation));
		if(floatIsEqual(handleLocation.distance(hornLocation), endRodLength, 0.0001f))
			System.out.println("It works");
		else
			System.out.println("It does NOT work. Distance should be "+endRodLength);
	}

}
