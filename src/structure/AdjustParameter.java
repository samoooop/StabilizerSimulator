package structure;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class AdjustParameter {
	public Color color;
	public Matrix4f transformation;

	public AdjustParameter(Color color, Matrix4f transformation) {
		this.color = color;
		this.transformation = transformation;
	}
}