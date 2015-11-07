package structure;

import java.awt.Color;

import org.joml.AxisAngle4f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Platform extends Structure {
	private float chamferLength = 0.5196152f, edgeLength = 3.21f;
	
	public Structure topChamfer = new Structure();
	public Structure leftChamfer = new Structure();
	public Structure rightChamfer = new Structure();
	public Structure rightEdge = new Structure();
	public Structure bottomEdge = new Structure();
	public Structure leftEdge = new Structure();
	public Stabilizer stabilizer;
	private TriLeg triLeg;
	private Vector3f pRotation = new Vector3f(),pTranslation = new Vector3f(0.0f,1.5f,0.0f);
	public Vector3f getPlatformRotation(){
		return new Vector3f(pRotation);
	}
	public Vector3f getPlatformTranslation(){
		return new Vector3f(pTranslation);	
	}
	public boolean setPlatformRotation(Vector3f angles){
		pRotation = angles;
		return adjustTriLeg();
	}
	public boolean setPlatformTranslation(Vector3f translation){
		pTranslation = translation;
		return adjustTriLeg();
	}
	private double p2(double v){//power of 2
		return v*v;
	}
	private float getMotorAngle(Vector3f platform,float lLength,float hLength) throws MotorAdjustmentException{//plane-transformed(PlatformAngle - motor) input
		float finalMAngle;
		double lShadow,hShadow,shadow;
		double magicC;
		shadow = new Vector3f(platform.x,platform.y,0.0f).length();
		lShadow = lLength;
		hShadow = Math.sqrt(p2(hLength) - p2(platform.z));
		magicC  = (p2(shadow)+p2(lShadow)- p2(hShadow))/(shadow*2);
		if(shadow > lShadow + hShadow) throw new MotorAdjustmentException("Unextendable length");
//		System.out.println(platform.x+ " " + platform.y+ " " + platform.z);
//		System.out.println(lLength + " " +  hLength);
//		System.out.println(shadow + " " + lShadow + " " +  hShadow + " "+ " " + magicC);
//		System.out.println((float)Math.acos(magicC/lShadow));
		finalMAngle = (float)(Math.PI/2 - Math.acos(magicC/lShadow) + Math.asin(Math.abs(platform.x)/shadow));
		if(Float.isNaN(finalMAngle)) throw new MotorAdjustmentException("impossible rotation");
		return finalMAngle;
	}
	public boolean adjustTriLeg(){
		Vector3f reference[] = {
				topChamfer.getFinalStart(),
				topChamfer.getFinalEnd(),
				leftChamfer.getFinalStart(),
				leftChamfer.getFinalEnd(),
				rightChamfer.getFinalStart(),
				rightChamfer.getFinalEnd()
		}; 
		for(int i=0;i<6;i++){
			try{
				if(reference[i]==null)return false;
				Matrix3f rotMat120 = new Matrix3f().rotateY((float)Math.toRadians(-120*(i/2)));
				reference[i] = new Vector3f(reference[i]);
				Vector3f motor = new Vector3f(triLeg.leg[i].lowerLeg.getFinalStart());
				Vector3f diff = new Vector3f();
				reference[i].sub(motor, diff);
				float motorAngle = getMotorAngle(diff, 
						triLeg.leg[i].lowerLeg.getLength(), 
						triLeg.leg[i].upperLeg.getLength()
						);
				triLeg.leg[i].setMotorAngle(motorAngle,i%2==1);
				reference[i].sub(triLeg.leg[i].upperLeg.getFinalStart(),triLeg.leg[i].upperLeg.end);
				triLeg.leg[i].upperLeg.end.mul(rotMat120);
				triLeg.leg[i].upperLeg.rotation.z = -triLeg.leg[i].rotation.z;
//				System.out.println(i + ":"+motorAngle+":" + triLeg.leg[i].upperLeg.getActualLength());
			}catch(MotorAdjustmentException failMovement){
				System.out.println(failMovement + triLeg.leg[i].name);
				return false;
			}
		}

		return true;

	}
	@Override
	public AdjustParameter updateTransformation(Color color,Matrix4f transformMatrix){
		if(pRotation==null||pTranslation==null){
			return super.updateTransformation(color,transformMatrix);
		}
		rotation = pRotation;
		location = pTranslation;
		return super.updateTransformation(color,transformMatrix);
	}
	public Platform(Stabilizer stabilizer) {
		super();
		this.triLeg = stabilizer.base.triLeg;
		this.stabilizer = stabilizer;
		createPlatformTriangle();
	}
	private void createPlatformTriangle() {
		myStructure = new Structure();
		subStructure.add(myStructure);
		drawChamfer();
		drawEdge();
	}
	
	
	
	private void drawChamfer() {
		float cornerToCenter = (float) (edgeLength / Math.sqrt(3));
		// float edgeToCenter =cornerToCenter/2;
		float eatenEdge = 2 * chamferLength / (float) Math.sqrt(3);



		topChamfer.start = new Vector3f(-eatenEdge / 2, 0.0f, cornerToCenter - chamferLength);
		topChamfer.end = new Vector3f(eatenEdge / 2, 0.0f, cornerToCenter - chamferLength);

		Vector4f chamferStart = new Vector4f(topChamfer.start, 0);
		Vector4f chamferEnd = new Vector4f(topChamfer.end, 0);
		Matrix4f chamferRotation = new Matrix4f().rotateY((float) Math.toRadians(120));

		chamferStart.mul(chamferRotation);
		chamferEnd.mul(chamferRotation);

		leftChamfer.start = new Vector3f(chamferStart.x, chamferStart.y, chamferStart.z);
		leftChamfer.end = new Vector3f(chamferEnd.x, chamferEnd.y, chamferEnd.z);

		chamferStart.mul(chamferRotation);
		chamferEnd.mul(chamferRotation);

		rightChamfer.start = new Vector3f(chamferStart.x, chamferStart.y, chamferStart.z);
		rightChamfer.end = new Vector3f(chamferEnd.x, chamferEnd.y, chamferEnd.z);
		
		subStructure.add(topChamfer);
		subStructure.add(leftChamfer);
		subStructure.add(rightChamfer);
	}
	
	private void drawEdge(){
		leftEdge = new Structure();
		bottomEdge = new Structure();
		rightEdge = new Structure();
		leftEdge.start = new Vector3f(topChamfer.end);
		leftEdge.end = new Vector3f(leftChamfer.start);
		bottomEdge.start = new Vector3f(leftChamfer.end);
		bottomEdge.end = new Vector3f(rightChamfer.start);
		rightEdge.start = new Vector3f(rightChamfer.end);
		rightEdge.end = new Vector3f(topChamfer.start);
		
		subStructure.add(rightEdge);
		subStructure.add(leftEdge);
		subStructure.add(bottomEdge);
		
	}
	@SuppressWarnings("serial")
	public class MotorAdjustmentException extends Exception{
		public MotorAdjustmentException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}
	}
}
