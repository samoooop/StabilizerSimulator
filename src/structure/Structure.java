package structure;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

//Currently support only line;
public class Structure {
	private int id;
	private static int idCount=0;
	public String name = "";
	public List<Structure> subStructure;
	public Structure myStructure;
	public Color color = null;
	public Vector3f location;
	public Vector3f rotation;
	public Vector3f start, end;
	public Structure() {
		location = new Vector3f();
		rotation = new Vector3f();
		subStructure = new ArrayList<Structure>();
		this.id = Structure.idCount++;
	}
	public int getId(){
		return id;
	}
	private void drawThis(Color color, Matrix4f transformationMatrix) {
		glPushMatrix();
		Vector4f finalStart = new Vector4f().add(start.x, start.y, start.z, 0).mul(transformationMatrix);
		Vector4f finalEnd = new Vector4f().add(end.x, end.y, end.z, 0).mul(transformationMatrix);
		glBegin(GL_LINES);
		glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
		glVertex3f(new Vector3f(finalStart.x, finalStart.y, finalStart.z));
		glVertex3f(new Vector3f(finalEnd.x, finalEnd.y, finalEnd.z));
		glEnd();
		glPopMatrix();
	}

	public AdjustParameter draw(Color color, Matrix4f transformation) {
		// locate real object location
		System.out.println(this.getClass().getSimpleName() + ":" + new Vector4f().mul(transformation));
		transformation.translate(this.location);
		transformation.rotateXYZ(this.rotation.x,this.rotation.y,this.rotation.z);
		if (this.color != null)
			color = this.color;
		if (color == null)
			color = Color.WHITE;
		if (start != null && end != null)
			drawThis(color, transformation);

		return new AdjustParameter(color, transformation);
	}

	public AdjustParameter draw(AdjustParameter param) {
		return draw(param.color, param.transformation);
	}

	private void glVertex3f(Vector3f vec) {
		GL11.glVertex3f(vec.x, vec.y, vec.z);
	}

}
