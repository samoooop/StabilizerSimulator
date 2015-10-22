package simulator;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Renderer {
	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private int WIDTH = 640;
	private int HEIGHT = 480;
	// The window handle
	private long window;
	FloatBuffer fb = BufferUtils.createFloatBuffer(16);
	StabilizerLogic logic = new StabilizerLogic();

	public void run() {
		try {

			init();
			loop();

			glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			glfwTerminate();
			errorCallback.release();
		}
	}

	private void init() {
		glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));

		if (glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
												// after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, GL_TRUE); // We will detect
																// this in our
																// rendering
				logic.keyPress(key, scancode, action, mods);
			}
		});

		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.asIntBuffer().get(0) - WIDTH) / 2,
				(vidmode.asIntBuffer().get(1) - HEIGHT) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);
	}

	Matrix4f projMatrix = new Matrix4f();
	Matrix4f viewMatrix = new Matrix4f();
	long firstTime = System.nanoTime();
	long frameCount = 0;
	private void drawLogic() {
		logic.update();
		glViewport(0, 0, WIDTH, HEIGHT);

		projMatrix.setPerspective(45.0f, (float) WIDTH / HEIGHT, 0.01f, 100.0f).get(fb);
		glMatrixMode(GL_PROJECTION);
		glLoadMatrixf(fb);
		
		viewMatrix.setLookAt(0.0f, 1.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.0f);

		viewMatrix.rotateX(logic.getPitch()).rotateY(logic.getYaw()).rotateZ(logic.getRoll()).get(fb);
		glMatrixMode(GL_MODELVIEW);
		glLoadMatrixf(fb);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		logic.render();
		
	}

	private void loop() {
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glClearColor(0.1f, 0.0f, 0.0f, 0.0f);

		while (glfwWindowShouldClose(window) == GL_FALSE) {
			drawLogic();
			glfwSwapBuffers(window);

			glfwPollEvents();
		}
	}

}
