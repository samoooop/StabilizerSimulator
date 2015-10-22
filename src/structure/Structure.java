package structure;

import java.awt.Color;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

//Currently support only line;
public class Structure {
	public String name;
	public List<Structure> subStructure;
	public Color color = null;
	public Vector3f location;
	public Vector3f rotation;
	public Vector3f start, end;

	public Structure() {
		location = new Vector3f();
		rotation = new Vector3f();
		subStructure = new ArrayList<Structure>();
	}

	private void drawThis(Color color, Vector3f location, Matrix4f rotation) {
		Vector3f finalStart = new Vector3f();
		Vector3f finalEnd = new Vector3f();
		glPushMatrix();
		// glRotatef((float)Math.random() * 360,1.0f,0.0f,0.0f);
		// glRotatef(rotation.m13,0.0f,1.0f,0.0f);
		// glRotatef(rotation.m23,0.0f,0.0f,1.0f);
		// System.out.println(location.toString());
		glBegin(GL_LINES);
		glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
		glVertex3f(finalStart.add(start).add(location));
		glVertex3f(finalEnd.add(end).add(location));
		glEnd();
		glPopMatrix();
	}

	public AdjustParameter draw(Color color, Vector3f location, Matrix4f rotation) {
		// locate real object location
		location.add(this.location);
		// rotation =
		// rotation.rotateXYZ(this.rotation.x,this.rotation.y,this.rotation.z);
		if (this.color != null)
			color = this.color;
		if (color == null)
			color = Color.WHITE;
		if (start != null && end != null)
			drawThis(color, location, rotation);
		return new AdjustParameter(color, location, rotation);
	}

	public AdjustParameter draw(AdjustParameter param) {
		return draw(param.color, param.location, param.rotation);
	}

	private void glVertex3f(Vector3f vec) {
		GL11.glVertex3f(vec.x, vec.y, vec.z);
	}


}
