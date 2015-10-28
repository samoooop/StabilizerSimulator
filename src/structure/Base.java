package structure;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Base extends Structure {
	public TriLeg triLeg;
	private float chamferLength = 0.8660254f, edgeLength = 3.81f;
	public Structure topChamfer = new Structure();
	public Structure rightChamfer = new Structure();
	public Structure leftChamfer = new Structure();
	public Structure leftEdge = new Structure();
	public Structure bottomEdge = new Structure();
	public Structure rightEdge = new Structure();
	private void init() {

		createBaseTriangle();
		triLeg = new TriLeg(topChamfer);
		subStructure.add(triLeg);
	}


	public Base() {
		super();
		init();
	}

	private void createBaseTriangle() {
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
		Matrix4f chamferRotation = new Matrix4f().rotateY((float) Math.toRadians(-120));

		chamferStart.mul(chamferRotation);
		chamferEnd.mul(chamferRotation);

		rightChamfer.start = new Vector3f(chamferStart.x, chamferStart.y, chamferStart.z);
		rightChamfer.end = new Vector3f(chamferEnd.x, chamferEnd.y, chamferEnd.z);

		chamferStart.mul(chamferRotation);
		chamferEnd.mul(chamferRotation);

		leftChamfer.start = new Vector3f(chamferStart.x, chamferStart.y, chamferStart.z);
		leftChamfer.end = new Vector3f(chamferEnd.x, chamferEnd.y, chamferEnd.z);
		
		subStructure.add(topChamfer);
		subStructure.add(rightChamfer);
		subStructure.add(leftChamfer);
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

}
