package structure;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class AdjustParameter {
	public Color color;
	public Vector3f location;
	public Matrix4f rotation;

	public AdjustParameter(Color color, Vector3f location, Matrix4f rotation) {
		this.color = color;
		this.location = location;
		this.rotation = rotation;
	}
}