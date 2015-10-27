package simulator;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;

import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import main.StabilizerControl;
import structure.AdjustParameter;
import structure.Axis;
import structure.Leg;
import structure.Stabilizer;
import structure.Structure;

public class StabilizerLogic {
	public Comm comm;
	public StabilizerControl window;
	private boolean[] registeredKey = new boolean[1024];
	private boolean[] isPressing = new boolean[1024];
	private float pitch, roll, yaw, zoom = 1;
	private float rotationSpeed = (float) (1.0 / 180.0 * Math.PI);
	public Structure structure;
	private Queue<Structure> renderQueue = new LinkedList<Structure>();
	private Queue<AdjustParameter> renderParameterQueue = new LinkedList<AdjustParameter>();
	private Stabilizer stabilizer;

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}

	public float getYaw() {
		return yaw;
	}

	public float getZoom() {
		return zoom;
	}

	private void registerKey() {
		registeredKey[GLFW_KEY_UP] = true;// Press arrow to rotate view
		registeredKey[GLFW_KEY_DOWN] = true;
		registeredKey[GLFW_KEY_LEFT] = true;
		registeredKey[GLFW_KEY_RIGHT] = true;
		registeredKey[GLFW_KEY_X] = true;
		registeredKey[GLFW_KEY_Z] = true;
		registeredKey[GLFW_KEY_SPACE] = true;
	}

	public StabilizerLogic() {
		registerKey();
		structure = new Structure();
		stabilizer = new Stabilizer();
		structure.subStructure.add(stabilizer);
		structure.subStructure.add(new Axis());
		StabilizerControl.main(this);
		comm = new Comm();
		comm.initialize();
//		
//		Thread t=new Thread() {
//			public void run() {
//
//
//
//			}
//		};
//		t.start();
	}

	public void keyPress(int key, int scancode, int action, int mods) {
		if (registeredKey[key]) {
			if (action == GLFW_PRESS)
				isPressing[key] = true;
			if (action == GLFW_RELEASE)
				isPressing[key] = false;
		}
	}

	void renderLines() {
		renderQueue.add(structure);
		renderParameterQueue.add(new AdjustParameter(null, new Matrix4f()));
		while (!renderQueue.isEmpty()) {
			Structure renderingStructure = renderQueue.poll();
			AdjustParameter renderingParameter = renderParameterQueue.poll();
			AdjustParameter adjustedParameter = renderingStructure.draw(renderingParameter);
			for (Structure subStructure : renderingStructure.subStructure) {
				renderQueue.add(subStructure);
				renderParameterQueue.add(adjustedParameter);
			}
		}
	}

	public void render() {
		renderLines();
	}

	int objCount = 0;
	private Structure lastSubStructure;
	public int[] currentParameter;

	public void updateControlInput() throws InterruptedException {
		int[] rawSliderData = window.getSliderData();
		float[] sliderData = new float[12];
		for (int i = 0; i < 12; i++) {
			sliderData[i] = rawSliderData[i] / 100.0f;
		}
		// for (int i = 0; i < 3; i++) {
		// Leg leg0, leg1;
		// leg0 = stabilizer.base.triLeg.leg[i * 2];
		// leg1 = stabilizer.base.triLeg.leg[i * 2 + 1];
		// leg0.setMotorAngleDegree(sliderData[i * 2], false);
		// leg1.setMotorAngleDegree(sliderData[i * 2 + 1], true);
		//
		// }
		if(stabilizer.platform.setPlatformRotation(new Vector3f((float) Math.toRadians(-(sliderData[6] - 45.0f)),
				(float) Math.toRadians(-(sliderData[8] - 45.0f)), (float) Math.toRadians(-(sliderData[7] - 45.0f))))
		&& stabilizer.platform.setPlatformTranslation(
				new Vector3f(sliderData[9] - 1.0f, sliderData[10] - 1.0f + 1.25f, sliderData[11] - 1.0f))
		){
			currentParameter = rawSliderData;
		}
		if(currentParameter != null){
//			window.setSliderData(currentParameter);
			
			try {
				for(int i=0;i<6;i++){
					String send = 
							i+ "" + (int)
							(Math.toDegrees(stabilizer.base.triLeg.leg[i].getMotorAngle())) + " ";
					comm.serialWrite(send);
				}

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void update() {
		if (window != null) {
			try {
				updateControlInput();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		if (isPressing[GLFW_KEY_UP])
			pitch -= rotationSpeed;
		if (isPressing[GLFW_KEY_DOWN])
			pitch += rotationSpeed;
		if (isPressing[GLFW_KEY_LEFT])
			yaw -= rotationSpeed;
		if (isPressing[GLFW_KEY_RIGHT])
			yaw += rotationSpeed;
		if (isPressing[GLFW_KEY_Z])
			zoom += 1.0f;
		if (isPressing[GLFW_KEY_X]) {
			if (zoom > 1)
				zoom -= 1.0f;
		}

		if (isPressing[GLFW_KEY_SPACE]) {
			if (lastSubStructure == null) {
				lastSubStructure = structure;
				lastSubStructure.end = new Vector3f();
			}
			Structure randLine = new Structure();
			randLine.name = objCount++ + "";
			randLine.location.add(lastSubStructure.end);
			randLine.start = new Vector3f();
			randLine.color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
			float trav = 10.0f * (float) Math.random();
			randLine.end = new Vector3f((float) Math.random() * trav - trav / 2, 0, 0);
			randLine.rotation = new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random());
			lastSubStructure.subStructure.add(randLine);
			lastSubStructure = randLine;
		}
	}
}