package structure;

import java.io.Console;
import java.util.Random;

import org.joml.Matrix3f;
import org.joml.Vector3f;

import simulator.Renderer;
import simulator.ModuleInterface;

public class MotorTester {

	public MotorTester() {
		// TODO Auto-generated constructor stub
	}
	
	private static boolean floatIsEqual(float a, float b, float esp) { return Math.abs(a-b) < esp; }
	
	private static class MyModule implements ModuleInterface{

		private boolean init = true;
		private Vector3f hornLocation, platformLocation;
		
		public MyModule(Vector3f hornLocation, Vector3f platformLocation) {
			super();
			this.hornLocation = hornLocation;
			this.platformLocation = platformLocation;
		}

		@Override
		public void run(Renderer app) {
			if(init){
				
				//app.logic.structure.subStructure.add()
				init = false;
			}
		}
		
	}//*/

	public static void main(String[] args) {

		float hornLength = 4.f,
				endRodLength = 12.f;
		
		Motor motor = new MotorModel2(hornLength, endRodLength);
		
		/// Core Calculation tester
		
		boolean works = true;
		
		for(float xOffset = -1.5f; xOffset < 1.5f; xOffset+=0.001f){
			float yOffset = (float)(Math.random()*3-1.5f),
					zOffset = (float)(Math.random()*3-1.5f);
			Vector3f handleLocation = new Vector3f(
					0f + xOffset,
					(float)(4*Math.cos(Math.PI/4) + 12*Math.cos(  Math.asin(Math.sin(Math.PI/4) / 3f)  )) + yOffset,
					0f + zOffset
					//(float)(Math.random()*3-1.5f),
					//(float)(Math.random()*3-1.5f),
					//(float)(Math.random()*3-1.5f)
					);
	
			System.out.println("Input Handle Location" + handleLocation);
			
			float motorAngle = 
				//(float)Math.PI/4;
				motor.getRotationAngle(handleLocation);
			
			Vector3f hornLocation = new Vector3f(hornLength, 0, 0);
			Matrix3f rotationMatMotorAngle = new Matrix3f();
			rotationMatMotorAngle.rotateXYZ(0f, 0f, motorAngle);
			hornLocation.mul(rotationMatMotorAngle);
			
			System.out.println("motorAngle " + (motorAngle / Math.PI * 180f) );
			System.out.println("hornLocation " + hornLocation);
			System.out.println("handleLocation " + handleLocation);
			
			System.out.println("distance between handleLocation and hornLocation is " + handleLocation.distance(hornLocation));
			if(!floatIsEqual(handleLocation.distance(hornLocation), endRodLength, 0.0001f)){
				System.out.println("It does NOT work. Distance should be "+endRodLength);
				works = false;
				break;
			}
		}
		if(works)
			System.out.println("It works");
		
		/*// Run Visual Simulation
		
		Renderer sim = new Renderer();
		ModuleInterface myModule = new MyModule(hornLocation, handleLocation);
		sim.addModule(myModule);
		sim.run();
		
		//*/
	}

}
