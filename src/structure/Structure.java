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
	private Vector3f finalStart;
	private Vector3f finalEnd;
	public String name = "";
	public List<Structure> subStructure;
	public Structure myStructure;
	public Color color = null;
	public Vector3f location;
	public Vector3f rotation;
	public Vector3f start, end;
	public boolean forcedColor = false;
	public Structure() {
		location = new Vector3f();
		rotation = new Vector3f();
		subStructure = new ArrayList<Structure>();
		this.id = Structure.idCount++;
	}
	public int getId(){
		return id;
	}
	public Vector3f getFinalStart(){
		return finalStart;
	}
	public Vector3f getFinalEnd(){
		return finalEnd;
	}
	private void drawThis(Color color, Matrix4f transformationMatrix) {
		glPushMatrix();
		Vector4f finalStart4f = new Vector4f().add(start.x, start.y, start.z, 0).mul(transformationMatrix);
		Vector4f finalEnd4f = new Vector4f().add(end.x, end.y, end.z, 0).mul(transformationMatrix);
		finalStart = new Vector3f(finalStart4f.x, finalStart4f.y, finalStart4f.z);
		finalEnd = new Vector3f(finalEnd4f.x, finalEnd4f.y, finalEnd4f.z);
		glLineWidth(5);

		glBegin(GL_LINES);
		glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
		glVertex3f(finalStart);
		glVertex3f(finalEnd);
		glEnd();
		glPopMatrix();
	}

	public AdjustParameter draw(Color color, Matrix4f transformation) {
		// locate real object location
		Matrix4f newTransMatrix = new Matrix4f(transformation);
		newTransMatrix.translate(this.location);
		newTransMatrix.rotateXYZ(this.rotation.x,this.rotation.y,this.rotation.z);
		if (color == null && this.color != null || forcedColor)
			color = this.color;
		if (start != null && end != null)
			drawThis(color ==null ? Color.white: color, newTransMatrix);
		return new AdjustParameter(color, newTransMatrix);
	}

	public AdjustParameter draw(AdjustParameter param) {
		return draw(param.color, param.transformation);
	}

	private void glVertex3f(Vector3f vec) {
		GL11.glVertex3f(vec.x, vec.y, vec.z);
	}

}
