package simulator;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWKeyCallback;

import main.StabilizerControl;
import simulator.Comm.Block;
import structure.AdjustParameter;
import structure.Axis;
import structure.Stabilizer;
import structure.Structure;

public class StabilizerLogic {
	public Comm comm;
	public StabilizerControl window;
	private boolean[] isPressing = new boolean[1024];
	private float rotationSpeed = (float) (1.0 / 180.0 * Math.PI);
	public Structure structure;
	private Queue<Structure> physicQueue = new LinkedList<Structure>();
	private Queue<Structure> renderQueue = new LinkedList<Structure>();
	private Queue<AdjustParameter> physicParameterQueue = new LinkedList<AdjustParameter>();
	private Stabilizer stabilizer;
	private Renderer renderer;

	public StabilizerLogic() {
		structure = new Structure();
		stabilizer = new Stabilizer();
		structure.subStructure.add(stabilizer);
		structure.subStructure.add(new Axis());
		StabilizerControl.main(this);
		comm = new Comm();
		comm.initialize();
		renderer = new Renderer();
		
	}

	public void run() {
		renderer.init(new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, GL_TRUE); // Closing
																// program
				keyPress(key, scancode, action, mods);
			}
		});

		while (!renderer.windowShouldClose()) {
			update();
			updatePhysic();
			postPhysicUpdate();
			renderer.startDraw();
			render();
			renderer.endDraw();
		}
		renderer.destroy();
	}

	public void keyPress(int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS)
			isPressing[key] = true;
		if (action == GLFW_RELEASE)
			isPressing[key] = false;
	}

	void updatePhysic() {
		physicQueue.add(structure);
		physicParameterQueue.add(new AdjustParameter(null, new Matrix4f()));
		while (!physicQueue.isEmpty()) {
			Structure renderingStructure = physicQueue.poll();
			AdjustParameter renderingParameter = physicParameterQueue.poll();
			AdjustParameter adjustedParameter = renderingStructure.updateTransformation(renderingParameter);
			for (Structure subStructure : renderingStructure.subStructure) {
				physicQueue.add(subStructure);
				physicParameterQueue.add(adjustedParameter);
			}
		}
	}
	void postPhysicUpdate(){
		stabilizer.platform.adjustTriLeg();
	}
	void render(){
		renderQueue.add(structure);
		while(!renderQueue.isEmpty()){
			Structure renderingStructure = renderQueue.poll();
			if (renderingStructure.isDrawable()) {
				renderer.drawLine(renderingStructure.getFinalStart(), renderingStructure.getFinalEnd(),
						renderingStructure.getFinalColor());
			}
			for(Structure subStructure:renderingStructure.subStructure){
				renderQueue.add(subStructure);
			}
		}
	}
	int objCount = 0;
	private Structure lastSubStructure;
	public int[] currentParameter;

	public void updateControlInput() throws InterruptedException {
//		int[] rawSliderData = window.getSliderData();
//		float[] sliderData = new float[12];
//		for (int i = 0; i < 12; i++) {
//			sliderData[i] = rawSliderData[i] / 100.0f;
//		}
//		// for (int i = 0; i < 3; i++) {
//		// Leg leg0, leg1;
//		// leg0 = stabilizer.base.triLeg.leg[i * 2];
//		// leg1 = stabilizer.base.triLeg.leg[i * 2 + 1];
//		// leg0.setMotorAngleDegree(sliderData[i * 2], false);
//		// leg1.setMotorAngleDegree(sliderData[i * 2 + 1], true);
//		//
//		// }
//		stabilizer.platform.setPlatformRotation(new Vector3f((float) Math.toRadians(-(sliderData[6] - 45.0f)),
//				(float) Math.toRadians(-(sliderData[8] - 45.0f)), (float) Math.toRadians(-(sliderData[7] - 45.0f))));
//		stabilizer.platform.setPlatformTranslation(
//				new Vector3f(sliderData[9] - 1.0f, sliderData[10] - 1.0f + 1.25f, sliderData[11] - 1.0f));
//		currentParameter = rawSliderData;
//		if (currentParameter != null) {
//			// window.setSliderData(currentParameter);
//
//			try {
//				for (int i = 0; i < 6; i++) {
//					String send = i + "" + (int) (Math.toDegrees(stabilizer.base.triLeg.leg[i].getMotorAngle())) + " ";
//					comm.serialWrite(send);
//				}
//
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	public void update() {
		if (window != null) {
			try {
				updateControlInput();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		while (comm.stringBlockReady()) {
			Block block = comm.dequeueStringBlock();
			String data = block.string;
			if('m' == (data.charAt(0))){
				System.out.println(data.substring(1));
			}
			if('g' == (data.charAt(0))){
				System.out.println("Gyro data:\n" + data.substring(1));
			}
		}

		if (isPressing[GLFW_KEY_UP])
			renderer.setPitch(renderer.getPitch() - rotationSpeed);
		if (isPressing[GLFW_KEY_DOWN])
			renderer.setPitch(renderer.getPitch() + rotationSpeed);
		if (isPressing[GLFW_KEY_LEFT])
			renderer.setYaw(renderer.getYaw() - rotationSpeed);
		if (isPressing[GLFW_KEY_RIGHT])
			renderer.setYaw(renderer.getYaw() + rotationSpeed);
		if (isPressing[GLFW_KEY_Z])
			renderer.setZoom(renderer.getZoom() - 0.1f);
		if (isPressing[GLFW_KEY_X]) {
			if (renderer.getZoom() > 1)
				renderer.setZoom(renderer.getZoom() + 0.1f);
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
