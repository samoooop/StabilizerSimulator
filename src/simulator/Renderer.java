package simulator;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Renderer {
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private int WIDTH = 1024;
	private int HEIGHT = 600;
	private float pitch=0, roll=0, yaw=0, zoom = 3;
	Matrix4f projMatrix = new Matrix4f();
	Matrix4f viewMatrix = new Matrix4f();
	long frameCount = 0;
	private long window;
	FloatBuffer fb = BufferUtils.createFloatBuffer(16);
	public void init(GLFWKeyCallback keyCallback) {
		glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));
		if (glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");
		glfwDefaultWindowHints(); // optional, the current window hints are
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // the window will be resizable
		window = glfwCreateWindow(WIDTH, HEIGHT, "Renderer", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		glfwSetKeyCallback(window,this.keyCallback = keyCallback);

		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.asIntBuffer().get(0) - WIDTH) / 2,
				(vidmode.asIntBuffer().get(1) - HEIGHT) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);
		
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glClearColor(0.1f,0.0f,0.f,1.0f);
	}

	public boolean windowShouldClose(){
		return GL_TRUE==glfwWindowShouldClose(window);
	}
	public void startDraw() {
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
	public float getZoom(){return zoom;}
	public void setPitch(float pitch){
		this.pitch = pitch;
	}
	public void setRoll(float roll){
		this.roll = roll;
	}
	public void setYaw(float yaw){
		this.yaw = yaw;
	}
	public void setZoom(float zoom){
		this.zoom = zoom;
	}
	public void drawLine(Vector3f start,Vector3f end,Color color){
		glPushMatrix();
		glLineWidth(5);
		glBegin(GL_LINES);
		if(color==null)color=Color.white;
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

	private void glVertex3f(Vector3f vec) {
		GL11.glVertex3f(vec.x, vec.y, vec.z);
	}
	
	public void destroy(){
		try {
			glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			glfwTerminate();
			errorCallback.release();
		}
	}
}


