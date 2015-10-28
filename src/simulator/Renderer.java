package simulator;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private int WIDTH = 1024;
	private int HEIGHT = 600;
	private float pitch, roll, yaw, zoom = 3;
	Matrix4f projMatrix = new Matrix4f();
	Matrix4f viewMatrix = new Matrix4f();
	long firstTime = System.nanoTime();
	long frameCount = 0;
	private long window;
	FloatBuffer fb = BufferUtils.createFloatBuffer(16);
	public StabilizerLogic logic = new StabilizerLogic();

	private void init() {
		glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));
		if (glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");
		glfwDefaultWindowHints(); // optional, the current window hints are
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
		window = glfwCreateWindow(WIDTH, HEIGHT, "Renderer", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, GL_TRUE); // Closing program
				logic.keyPress(key, scancode, action, mods);
			}
		});

		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.asIntBuffer().get(0) - WIDTH) / 2,
				(vidmode.asIntBuffer().get(1) - HEIGHT) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glClearColor(0.1f, 0.0f, 0.0f, 0.0f);
	}

	public void run() {
		try {

			init();
			glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			glfwTerminate();
			errorCallback.release();
		}
	}
	private void startDraw() {
		for(ModuleInterface module:modules){
			module.run(this);
		}
		glViewport(0, 0, WIDTH, HEIGHT);
		projMatrix.setPerspective(45.0f, (float) WIDTH / HEIGHT, 0.01f, 100.0f).get(fb);
		glMatrixMode(GL_PROJECTION);
		glLoadMatrixf(fb);
		Vector3f eye = new Vector3f(0.0f,
				(float)(zoom * Math.cos(Math.atan(2))),
				(float)(zoom * Math.sin(Math.atan(2))));
		viewMatrix.setLookAt(eye.x,eye.y, -eye.z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		viewMatrix.rotateX(pitch)
			.rotateY(yaw)
			.rotateZ(roll)
			.get(fb);
		glMatrixMode(GL_MODELVIEW);
		glLoadMatrixf(fb);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	public float getPitch(){return pitch;}
	public float getRoll(){return roll;}
	public float getYaw(){return yaw;}
	public float getZoom(){return pitch;}
	public float setPitch(float pitch){
		this.pitch = pitch;
	}
	public float setRaw(float pitch){
		this.roll = roll;
	}
	public float setYaw(float pitch){
		this.yaw = yaw;
	}
	public float setZoom(float zoom){
		this.zoom = zoom;
	}
	public void drawLine(Vector3f start,Vector3f end,Color color){
		glPushMatrix();
		glLineWidth(5);
		glBegin(GL_LINES);
		glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
		glVertex3f(start);
		glVertex3f(end);
		glEnd();
		glPopMatrix();
	}

	public void endDraw(){
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

}
